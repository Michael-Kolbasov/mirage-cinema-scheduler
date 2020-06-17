package com.example.scheduler.tg.service;

import com.example.scheduler.core.CinemaScheduleParser;
import com.example.scheduler.exception.SchedulerException;
import com.example.scheduler.model.Cinema;
import com.example.scheduler.model.City;
import com.example.scheduler.model.Show;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class UpdateDispatcherService implements DispatcherServiceI {
    private static final String START = "/start";
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final String CHOOSE_START = "Чтобы начать, введите /start";
    private static final List<City> CITIES = Arrays.asList(City.values());
    private static final List<Cinema> CINEMAS = Arrays.asList(Cinema.values());
    private static final int ROWS_PER_SCREEN = 3;
    private static final ReplyKeyboard CITIES_KEYBOARD = createCitiesKeyboard();
    private static final Map<City, ReplyKeyboard> CINEMAS_KEYBOARD = createCinemasKeyboard();

    @Autowired
    private TelegramLongPollingBot bot;

    private final CinemaScheduleParser parser = new CinemaScheduleParser();
    private final Map<Integer, Cinema> chosenCinemas = new ConcurrentHashMap<>();

    @Override
    public void dispatch(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String text = message.getText();
            if (text.equals(START)) {
                showCities(update);
            } else {
                sendMsg(message, CHOOSE_START);
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            if (CITIES.contains(City.byName(data))) {
                showCinemas(update);
            } else if (CINEMAS.contains(Cinema.byName(data))) {
                showsDates(update);
            } else if (DATE_PATTERN.matcher(data).matches()) {
                showSchedule(update);
            } else {
                sendMsg(callbackQuery, CHOOSE_START);
            }
        } else {
            sendMsg(update.getMessage(), CHOOSE_START);
        }
    }

    private void showCities(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), "Выберите город:");
        sendMessage.setReplyMarkup(CITIES_KEYBOARD);
        sendMsg(sendMessage);
    }

    private void showCinemas(Update update) {
        SendMessage sendMessage = new SendMessage(update.getCallbackQuery().getMessage().getChatId(), "Выберите кинотеатр:");
        City city = City.byName(update.getCallbackQuery().getData());
        sendMessage.setReplyMarkup(CINEMAS_KEYBOARD.get(city));
        sendMsg(sendMessage);
    }

    private void showsDates(Update update) {
        chosenCinemas.put(update.getCallbackQuery().getFrom().getId(), Cinema.byName(update.getCallbackQuery().getData()));

        SendMessage sendMessage = new SendMessage(update.getCallbackQuery().getMessage().getChatId(), "Выберите дату:");
        LocalDate today = LocalDate.now();
        ReplyKeyboard dates = createDates(today);
        sendMessage.setReplyMarkup(dates);
        sendMsg(sendMessage);
    }

    private void showSchedule(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        List<Show> schedule;
        try {
            schedule = parser.parseScheduleByDateAndLink(chosenCinemas.get(update.getCallbackQuery().getFrom().getId()), LocalDate.parse(update.getCallbackQuery().getData()));
        } catch (SchedulerException e) {
            sendMessage.setText(e.getMessage());
            sendMsg(sendMessage);
            return;
        }

        if (schedule.size() == 0) {
            sendMessage.setText("Сеансов нет.");
        } else {
            String shows = schedule.stream()
                    .map(Show::toString)
                    .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));
            sendMessage.setText(shows);
        }

        sendMsg(sendMessage);
    }

    private static ReplyKeyboard createDates(LocalDate today) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        LocalDate date = today;
        if (date.getDayOfWeek().compareTo(DayOfWeek.THURSDAY) >= 0) {
            do {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(date.toString());
                button.setCallbackData(date.toString());
                buttons1.add(button);
                date = date.plusDays(1);
            } while (date.getDayOfWeek() != DayOfWeek.MONDAY);
        } else {
            do {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(date.toString());
                button.setCallbackData(date.toString());
                buttons1.add(button);
                date = date.plusDays(1);
            } while (date.getDayOfWeek() != DayOfWeek.THURSDAY);
        }
        buttons.add(buttons1);
        return new InlineKeyboardMarkup(buttons);
    }

    private static ReplyKeyboard createCitiesKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int citiesSize = CITIES.size();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        for (int i = 0, j = 0; i < citiesSize; i++, j++) {
            if (i != 0 && (i % ROWS_PER_SCREEN == 0 || i == citiesSize - 1)) {
                buttons.add(buttons1);
                buttons1 = new ArrayList<>();
            }
            String cityName = CITIES.get(j).getName();
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(cityName);
            button.setCallbackData(cityName);
            buttons1.add(button);
        }

        return new InlineKeyboardMarkup(buttons);
    }

    private static Map<City, ReplyKeyboard> createCinemasKeyboard() {
        Map<City, ReplyKeyboard> citiesWithCinemas = new EnumMap<>(City.class);
        for (City city : CITIES) {
            List<Cinema> cinemas = city.getCinemas();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            int totalCinemas = cinemas.size();
            boolean isManyCinemas = totalCinemas > ROWS_PER_SCREEN;

            List<InlineKeyboardButton> buttons1 = new ArrayList<>();
            if (isManyCinemas) {
                for (int i = 0, j = 0; i < totalCinemas; i++, j++) {
                    boolean isLast = i == totalCinemas - 1;
                    boolean isDivisibleByN = i % ROWS_PER_SCREEN == 0;
                    boolean isFirst = i == 0;
                    if (!isFirst && (isDivisibleByN || isLast)) {
                        buttons.add(buttons1);
                        buttons1 = new ArrayList<>();
                    }
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(cinemas.get(j).getSimpleName());
                    button.setCallbackData(cinemas.get(j).getFormalName());
                    buttons1.add(button);
                    if (isLast) {
                        buttons.add(buttons1);
                    }
                }
            } else {
                for (int i = 0, j = 0; i < totalCinemas; i++, j++) {
                    InlineKeyboardButton button = new InlineKeyboardButton();
                    button.setText(cinemas.get(j).getSimpleName());
                    button.setCallbackData(cinemas.get(j).getFormalName());
                    buttons1.add(button);
                }
                buttons.add(buttons1);
            }

            InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(buttons);
            citiesWithCinemas.put(city, keyboard);
        }

        return citiesWithCinemas;
    }

    private synchronized void sendMsg(BotApiMethod<?> sendMessage) {
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage(message.getChatId(), text);
        sendMessage.enableMarkdown(true);
        sendMsg(sendMessage);
    }

    private void sendMsg(CallbackQuery query, String text) {
        SendMessage sendMessage = new SendMessage(query.getMessage().getChatId(), text);
        sendMessage.enableMarkdown(true);
        sendMsg(sendMessage);
    }
}
