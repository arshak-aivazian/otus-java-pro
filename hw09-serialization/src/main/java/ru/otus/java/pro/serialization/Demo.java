package ru.otus.java.pro.serialization;

import ru.otus.java.pro.serialization.csvstrategy.ChatOutputCsvStrategy;
import ru.otus.java.pro.serialization.dto.ChatInput;
import ru.otus.java.pro.serialization.dto.ChatOutput;
import ru.otus.java.pro.serialization.handler.ChatOutputSerializationHandler;
import ru.otus.java.pro.serialization.mapper.ChatMapper;
import ru.otus.java.pro.serialization.serializer.CsvSerializer;
import ru.otus.java.pro.serialization.serializer.JsonSerializer;
import ru.otus.java.pro.serialization.serializer.XmlSerializer;
import ru.otus.java.pro.serialization.serializer.YamlSerializer;

import java.util.List;

public class Demo {
    public void run() {
        String path = "hw09-serialization/src/main/resources/input/sms.json";

        var jsonSerializer = new JsonSerializer();

        var input = jsonSerializer.deserialize(path, ChatInput.class);

        var chatOutput = ChatMapper.INSTANCE.map(input);

        ChatOutputSerializationHandler handler = new ChatOutputSerializationHandler(
                List.of(
                        new JsonSerializer(),
                        new XmlSerializer(),
                        new YamlSerializer(),
                        new CsvSerializer(new ChatOutputCsvStrategy(chatOutput))
                )
        );
        handler.handle(chatOutput);
    }
}
