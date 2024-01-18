package org.kps.currency.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.kps.currency.client.CurrencyClientImpl;
import org.kps.currency.repository.CurrencyRepo;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class SchemaInitServiceTest {

    @Mock
    private CurrencyRepo repo;

    @Mock
    private CurrencyClientImpl client;

    private SchemaInitService schemaInitService;

    @BeforeEach
    void setUp() {
        schemaInitService = new SchemaInitService(repo, client);

    }

    @Test
    void initSchemaOnStartupWhenRepoIsNotEmpty() {
        when(repo.count()).thenReturn(1L);

        schemaInitService.initSchemaOnStartup();

        verify(client, never()).getDataFromAPI();
        verify(repo, never()).saveAll(any());
    }
}
