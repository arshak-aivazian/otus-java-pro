package com.example.httpsuuidgenerator.dto;

import java.util.UUID;

public class UuidDto {
    private final UUID uuid;

    public UuidDto(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
