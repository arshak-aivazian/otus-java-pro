package com.example.httpsuuidclient.service;

import com.example.httpsuuidclient.dto.UuidDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class UuidRestClient {

    @Value("${uuid.service.url}")
    private String uuidServiceUrl;

    private final RestTemplate restTemplate;

    public UuidRestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UUID getUUID() {
        UuidDto response = restTemplate.getForObject(uuidServiceUrl, UuidDto.class);
        return response.getUuid();
    }
}
