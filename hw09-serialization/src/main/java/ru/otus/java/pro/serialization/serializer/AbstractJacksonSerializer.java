package ru.otus.java.pro.serialization.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.java.pro.serialization.serializer.exception.DeserializeException;
import ru.otus.java.pro.serialization.serializer.exception.SerializeException;

import java.io.File;
import java.io.IOException;

public abstract class AbstractJacksonSerializer implements Serializer {
    private final ObjectMapper objectMapper;

    public AbstractJacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void serialize(String filePath, Object object) {
        try {
            objectMapper.writeValue(new File(filePath), object);
        } catch (IOException e) {
            throw new SerializeException(e);
        }
    }

    @Override
    public <T> T deserialize(String filePath, Class<T> clazz) {
        try {
            return objectMapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new DeserializeException(e);
        }
    }
}
