package com.example.httpsuuidgenerator.service;

import com.example.httpsuuidgenerator.dto.UuidDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidGenerator {
    public UuidDto generate() {
        return new UuidDto(UUID.randomUUID());
    }
}
