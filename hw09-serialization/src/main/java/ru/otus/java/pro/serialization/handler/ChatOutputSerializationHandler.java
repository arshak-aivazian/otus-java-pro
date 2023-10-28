package ru.otus.java.pro.serialization.handler;

import ru.otus.java.pro.serialization.dto.ChatOutput;
import ru.otus.java.pro.serialization.serializer.Serializer;

import java.util.List;

public class ChatOutputSerializationHandler implements ChatOutputHandler {
    private static final String FILE_PATH_TEMPLATE = "hw09-serialization/src/main/resources/output/test.%s";

    private final List<Serializer> serializers;

    public ChatOutputSerializationHandler(List<Serializer> serializers) {
        this.serializers = serializers;
    }

    @Override
    public void handle(ChatOutput chatOutput) {
        serializers.forEach(serializer -> {
            String filePath = String.format(FILE_PATH_TEMPLATE, serializer.getFileFormat().getValue());

            System.out.println(serializer.getClass() + " serialize into file " + filePath + " object: " + chatOutput);
            serializer.serialize(filePath, chatOutput);

            ChatOutput deserialize = serializer.deserialize(filePath, ChatOutput.class);
            System.out.println(serializer.getClass() + " deserialize " + deserialize);
            System.out.println("serialized and deserialized objects are equal = " + chatOutput.equals(deserialize));
        });
    }
}
