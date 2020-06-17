package com.example.scheduler.tg.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface DispatcherServiceI {
    void dispatch(Update update);
}
