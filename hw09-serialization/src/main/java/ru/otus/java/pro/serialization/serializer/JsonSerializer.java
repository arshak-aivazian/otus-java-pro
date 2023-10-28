package ru.otus.java.pro.serialization.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer extends AbstractJacksonSerializer {

    public JsonSerializer() {
        super(new ObjectMapper());
    }

    @Override
    public FileFormat getFileFormat() {
        return FileFormat.JSON;
    }
}
