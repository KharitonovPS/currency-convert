//package org.kps.currency.controller;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.kps.currency.common.validation.CodeISOValidator;
//import org.kps.currency.domain.currency.entity.CurrencyEntity;
//import org.kps.currency.domain.currency.repository.CurrencyRepo;
//import org.kps.currency.domain.users.repository.UserRepository;
//import org.kps.currency.domain.users.service.UserService;
//import org.kps.currency.service.GoogleAuthorizationService;
//import org.kps.currency.web.controller.CurrencyController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@Tag("fast")
//@Tag("controller")
//@WebMvcTest(CurrencyController.class)
//@Import({CodeISOValidator.class, CurrencyRepo.class})
//@RunWith(SpringRunner.class)
//@ComponentScan(basePackages = {"org.kps.currency", "org.kps.currency.controller"})
//class CurrencyControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CodeISOValidator validator;
//
//    @MockBean
//    private CurrencyRepo repo;
//
//    @MockBean
//    GoogleAuthorizationService authorizationService;
//
//    @MockBean
//    private UserRepository repository;
//
//    @MockBean
//    private UserService userService;
//
//
//    @Test
//    @Disabled
//    void serviceShouldReturnListOfAllQuotes() throws Exception {
//        CurrencyEntity entity = new CurrencyEntity(1L, "USD", 840, "Dollar", new BigDecimal("1"), Instant.now());
//        CurrencyEntity entity2 = new CurrencyEntity(2L, "EUR", 111, "EURO", new BigDecimal("2"), Instant.now());
//
//        List<CurrencyEntity> entities = List.of(entity, entity2);
//
//        when(repo.findByCharCode("USD")).thenReturn(Optional.of(entity));
//        when(repo.findAll()).thenReturn(entities);
//        when(validator.isValid(any(), any())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/v1/pairs")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"quote\" : \"USD\"}"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Disabled
//    void serviceShouldConvertCurrencyForQuote() throws Exception {
//        CurrencyEntity entity = new CurrencyEntity(1L, "USD", 840, "Dollar", new BigDecimal("4"), Instant.now());
//        CurrencyEntity entity2 = new CurrencyEntity(2L, "EUR", 111, "EURO", new BigDecimal("2"), Instant.now());
//        List<CurrencyEntity> entities = List.of(entity, entity2);
//
//        when(repo.findByCharCode("USD")).thenReturn(Optional.of(entity));
//        when(repo.findByCharCode("EUR")).thenReturn(Optional.of(entity2));
//        when(repo.findAll()).thenReturn(entities);
//        when(validator.isValid(any(), any())).thenReturn(true);
//
//        this.mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/v1/convert")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{ \"base\" : \"USD\", \"quote\" :  \"EUR\", \"value\" :  10}"))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//}