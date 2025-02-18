package org.kps.currency.web.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.currency.dto.CurrencyRequestDTO;
import org.kps.currency.domain.currency.dto.CurrencyRequestDTOConvertImpl;
import org.kps.currency.domain.currency.dto.CurrencyRequestDTOGetListImpl;
import org.kps.currency.domain.currency.dto.CurrencyResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.List;

@Component
@Data
@RequiredArgsConstructor
@Slf4j
public class CurrencyClientTestImpl implements CurrencyClient {

    private ObjectMapper mapper;

    private String BASE_URI;

    private HttpClient client;

    public CurrencyClientTestImpl(int port, int timeoutSeconds) {
        this.mapper = new ObjectMapper();
        BASE_URI = "http://localhost:" + port + "/api/v1";
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(timeoutSeconds))
                .build();
    }

    private HttpRequest createPostRequest(String path, CurrencyRequestDTO dto) {
        try {
            String URI = BASE_URI + path;
            String body = mapper.writeValueAsString(dto);
            log.info("Sending request to CurrencyController -> {} , {}", URI, body);
            return HttpRequest.newBuilder()
                    .uri(new URI(URI))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
        } catch (URISyntaxException | JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<List<CurrencyResponseDTO>> getListForQuote(
            CurrencyRequestDTOGetListImpl dto
    ) {
        HttpRequest postRequest = createPostRequest("/pairs", dto);
        String response = sendRequest(client, postRequest);
        List<CurrencyResponseDTO> currencyResponseDTOS = deserializeList(response);
        return new ResponseEntity<>(currencyResponseDTOS, HttpStatus.OK);
    }

    public ResponseEntity<String> convert(CurrencyRequestDTOConvertImpl dto) {
        HttpRequest postRequest = createPostRequest("/convert", dto);
        String response = sendRequest(client, postRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private List<CurrencyResponseDTO> deserializeList(String json) {
        try {
            log.info("JSON to deserialize: {}", json);
            return mapper.readValue(
                    json,
                    new TypeReference<>() {
                    });
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize response: {} ",
                    e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
