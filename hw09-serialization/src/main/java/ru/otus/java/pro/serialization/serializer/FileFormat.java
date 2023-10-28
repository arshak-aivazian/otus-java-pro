package ru.otus.java.pro.serialization.serializer;

import lombok.Getter;

@Getter
public enum FileFormat {
    JSON("json"),
    XML("xml"),
    CSV("csv"),
    YAML("yaml");

    private final String value;

    FileFormat(String value) {
        this.value = value;
    }
}
