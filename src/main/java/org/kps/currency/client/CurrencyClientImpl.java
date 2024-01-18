package org.kps.currency.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.CurrencyEntity;
import org.kps.currency.domain.dto.CurrencyApiDTO;
import org.kps.currency.mapper.CurrencyApiMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Getter
@Setter
@Service
@Slf4j
public class CurrencyClientImpl implements CurrencyClient {

    @Value("${client.properties.url}")
    private String url;

    @Value("${client.properties.key}")
    private String key;

    private final ObjectMapper mapper;
    private final HttpClient client;
    private final CurrencyApiMapper currencyApiMapper;


    public CurrencyClientImpl(CurrencyApiMapper currencyApiMapper) {
        this.currencyApiMapper = currencyApiMapper;
        this.mapper = new ObjectMapper();
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(1000))
                .build();
    }

    public List<CurrencyEntity> getDataFromAPI() {
        HttpRequest getRequest = createGetRequest();
        String response = sendRequest(getRequest);
        CurrencyApiDTO apiDTO = deserialize(response);
        return currencyApiMapper.currencyDtoToEntity(apiDTO);
    }


    private HttpRequest createGetRequest() {
        try {
            String URI = url + key;
            log.info("Sending request to API -> {}", URI);
            return HttpRequest.newBuilder()
                    .uri(new URI(URI))
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String sendRequest(HttpRequest request) {
        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                log.error("HTTP request failed with status code: {}, {}  ",
                        response.statusCode(), response.body());
                throw new RuntimeException();
            }

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public CurrencyApiDTO deserialize(String json) {
        try {
            return mapper.readValue(json, CurrencyApiDTO.class);
        } catch (JsonProcessingException exception) {
            log.info("Failed to deserialize response: " +
                            exception.getMessage(),
                    exception);
            throw new RuntimeException();
        }
    }
}