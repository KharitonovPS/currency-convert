package org.kps.currency.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.kps.currency.mapper.CurrencyApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.http.HttpClient;

@RunWith(SpringRunner.class)
@SpringBootTest
class CurrencyClientImplTest {

    @MockBean
    private HttpClient httpClient;

    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private CurrencyApiMapper apiMapper;

    @Autowired
    CurrencyClientImpl client;

    @Test
    void contextLoad() {
    }

    ;
}