package com.example.httpsuuidgenerator.controller;

import com.example.httpsuuidgenerator.dto.UuidDto;
import com.example.httpsuuidgenerator.service.UuidGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/uuid")
public class UuidController {
    private final UuidGenerator uuidGenerator;

    public UuidController(UuidGenerator uuidGenerator) {
        this.uuidGenerator = uuidGenerator;
    }

    @GetMapping
    public UuidDto geyUUID() {
        return uuidGenerator.generate();
    }
}
