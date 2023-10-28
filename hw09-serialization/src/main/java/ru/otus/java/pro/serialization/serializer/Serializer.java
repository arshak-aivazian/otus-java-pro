package ru.otus.java.pro.serialization.serializer;

public interface Serializer {
    void serialize(String filePath, Object object);
    <T> T deserialize(String filePath, Class<T> clazz);
    FileFormat getFileFormat();
}
