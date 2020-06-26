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
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class UpdateDispatcherService implements DispatcherServiceI {
    private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String START = "/start";
    private static final String CHOOSE_START = "Чтобы начать, введите /start";
    private static final String CHOOSE_CITY = "Выберите город:";
    private static final String CHOOSE_CINEMA = "Выберите кинотеатр:";
    private static final String CHOOSE_DATE = "Выберите дату:";
    private static final String NO_FILMS = "Сеансов нет.";
    private static final List<City> CITIES = Arrays.asList(City.values());
    private static final List<Cinema> CINEMAS = Arrays.asList(Cinema.values());
    private static final int OPTIONS_PER_LINE = 3;
    private static final int DATES_PER_LINE = 2;
    private static final ReplyKeyboard CITIES_KEYBOARD = createCitiesKeyboard();
    private static final Map<City, ReplyKeyboard> CINEMAS_KEYBOARD = createCinemasKeyboard();

    @Autowired
    private TelegramLongPollingBot bot;

    private final CinemaScheduleParser parser = new CinemaScheduleParser();
    private final Map<Long, Cinema> chosenCinemas = new ConcurrentHashMap<>();

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
            } else if (data.matches(DATE_PATTERN)) {
                showSchedule(update);
            } else {
                sendMsg(callbackQuery, CHOOSE_START);
            }
        } else {
            sendMsg(update.getMessage(), CHOOSE_START);
        }
    }

    private void showCities(Update update) {
        SendMessage sendMessage = new SendMessage(update.getMessage().getChatId(), CHOOSE_CITY);
        sendMessage.setReplyMarkup(CITIES_KEYBOARD);
        sendMsg(sendMessage);
    }

    private void showCinemas(Update update) {
        SendMessage sendMessage = new SendMessage(update.getCallbackQuery().getMessage().getChatId(), CHOOSE_CINEMA);
        City city = City.byName(update.getCallbackQuery().getData());
        sendMessage.setReplyMarkup(CINEMAS_KEYBOARD.get(city));
        sendMsg(sendMessage);
    }

    private void showsDates(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        Cinema cinema = Cinema.byName(update.getCallbackQuery().getData());
        chosenCinemas.put(chatId, cinema);

        SendMessage sendMessage = new SendMessage(chatId, CHOOSE_DATE);
        LocalDate today = LocalDate.now(cinema.getCity().getTimeZone());
        ReplyKeyboard dates = createDates(today);
        sendMessage.setReplyMarkup(dates);
        sendMsg(sendMessage);
    }

    private void showSchedule(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String date = update.getCallbackQuery().getData();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        List<Show> schedule;
        try {
            schedule = parser.parseScheduleByDateAndLink(chosenCinemas.get(chatId), LocalDate.parse(date));
        } catch (SchedulerException e) {
            sendMessage.setText(e.getMessage());
            sendMsg(sendMessage);
            return;
        }

        chosenCinemas.remove(chatId);

        if (schedule.size() == 0) {
            sendMessage.setText(NO_FILMS);
        } else {
            String shows = schedule.stream()
                    .map(Show::toString)
                    .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));
            sendMessage.setText(shows);
        }

        sendMsg(sendMessage);
    }

    private static ReplyKeyboard createDates(LocalDate today) {
        List<List<InlineKeyboardButton>> buttons = fillDates(
                today,
                day -> day.getDayOfWeek() == DayOfWeek.MONDAY,
                day -> day.getDayOfWeek() == DayOfWeek.THURSDAY);
        return new InlineKeyboardMarkup(buttons);
    }

    private static List<List<InlineKeyboardButton>> fillDates(LocalDate date, Predicate<LocalDate> stopConditionAfterThursday, Predicate<LocalDate> stopConditionBeforeThursday) {
        if (date.getDayOfWeek().compareTo(DayOfWeek.THURSDAY) >= 0) {
            return iterateUntil(date, stopConditionAfterThursday);
        } else {
            return iterateUntil(date, stopConditionBeforeThursday);
        }
    }

    private static List<List<InlineKeyboardButton>> iterateUntil(LocalDate date, Predicate<LocalDate> stopCondition) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> line = new ArrayList<>();
        do {
            if (line.size() == DATES_PER_LINE) {
                buttons.add(line);
                line = new ArrayList<>();
            }
            date = addDateToButtonsAndIncrement(line, date);
        } while (!stopCondition.test(date));

        buttons.add(line);
        return buttons;
    }

    private static LocalDate addDateToButtonsAndIncrement(List<InlineKeyboardButton> line, LocalDate date) {
        InlineKeyboardButton button = createInlineKeyboardButton(date.toString(), date.toString());
        line.add(button);
        return date.plusDays(1);
    }

    private static ReplyKeyboard createCitiesKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        int citiesSize = CITIES.size();
        List<InlineKeyboardButton> line = new ArrayList<>();
        for (int i = 0, j = 0; i < citiesSize; i++, j++) {
            String cityName = CITIES.get(j).getName();
            InlineKeyboardButton button = createInlineKeyboardButton(cityName, cityName);
            line.add(button);

            if (line.size() == OPTIONS_PER_LINE) {
                buttons.add(line);
                line = new ArrayList<>();
            }
        }

        return new InlineKeyboardMarkup(buttons);
    }

    private static Map<City, ReplyKeyboard> createCinemasKeyboard() {
        Map<City, ReplyKeyboard> citiesWithCinemas = new EnumMap<>(City.class);
        for (City city : CITIES) {
            List<Cinema> cinemas = city.getCinemas();
            List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
            int totalCinemas = cinemas.size();
            boolean isManyCinemas = totalCinemas > OPTIONS_PER_LINE;

            List<InlineKeyboardButton> line = new ArrayList<>();
            if (isManyCinemas) {
                for (int i = 0, j = 0; i < totalCinemas; i++, j++) {
                    boolean isLast = i == totalCinemas - 1;
                    boolean isDivisibleByN = i % OPTIONS_PER_LINE == 0;
                    boolean isFirst = i == 0;
                    if (!isFirst && (isDivisibleByN || isLast)) {
                        buttons.add(line);
                        line = new ArrayList<>();
                    }
                    InlineKeyboardButton button = createInlineKeyboardButton(cinemas.get(j).getSimpleName(), cinemas.get(j).getFormalName());
                    line.add(button);
                    if (isLast) {
                        buttons.add(line);
                    }
                }
            } else {
                for (int i = 0, j = 0; i < totalCinemas; i++, j++) {
                    InlineKeyboardButton button = createInlineKeyboardButton(cinemas.get(j).getSimpleName(), cinemas.get(j).getFormalName());
                    line.add(button);
                }
                buttons.add(line);
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

    private static InlineKeyboardButton createInlineKeyboardButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData);
        return button;
    }
}
