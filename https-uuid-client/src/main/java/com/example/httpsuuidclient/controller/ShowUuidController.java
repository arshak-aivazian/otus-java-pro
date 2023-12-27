package com.example.httpsuuidclient.controller;

import com.example.httpsuuidclient.service.UuidRestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/show-uuid")
public class ShowUuidController {

    private final UuidRestClient client;

    public ShowUuidController(UuidRestClient client) {
        this.client = client;
    }

    @GetMapping
    public String showUuid() {
        UUID uuid = client.getUUID();
        return "Your UUID: " + uuid;
    }
}
