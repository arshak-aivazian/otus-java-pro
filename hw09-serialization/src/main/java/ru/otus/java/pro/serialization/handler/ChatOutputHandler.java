package ru.otus.java.pro.serialization.handler;

import ru.otus.java.pro.serialization.dto.ChatOutput;

public interface ChatOutputHandler {
    void handle(ChatOutput chatOutput);
}
