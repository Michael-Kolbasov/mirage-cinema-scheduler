package com.example.scheduler.tg.bot;

import com.example.scheduler.tg.service.DispatcherServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class SchedulerBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerBot.class);

    @Value("${bot.username}")
    private String username;
    @Value("${bot.token}")
    private String token;

    @Autowired
    private DispatcherServiceI dispatcherService;

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("{}", update);
        dispatcherService.dispatch(update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
