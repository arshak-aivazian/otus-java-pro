package com.example.httpsuuidclient.dto;

import java.util.UUID;

public class UuidDto {
    private UUID uuid;

    public UuidDto() {
    }

    public UuidDto(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
