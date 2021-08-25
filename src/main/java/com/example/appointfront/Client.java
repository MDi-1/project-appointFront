package com.example.appointfront;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Getter
@RequiredArgsConstructor
public class Client {

    private final RestTemplate restTemplate;
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public List<Dto> getResponse() {
        URI url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/v1/test/getAll").build().encode().toUri();
        try {
            Dto[] response = restTemplate.getForObject(url, Dto[].class);
            return Optional.ofNullable(response)
                    .map(Arrays::asList)
                    .orElse(Collections.emptyList());
        } catch (RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }
}
