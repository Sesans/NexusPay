package com.nexuspay.ledger.web;

import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.infra.security.TokenService;
import com.nexuspay.ledger.application.dto.TransferRequestDTO;
import com.nexuspay.ledger.domain.model.Account;
import com.nexuspay.ledger.domain.model.CurrencyCode;
import com.nexuspay.ledger.domain.model.TransactionCondition;
import com.nexuspay.ledger.domain.repository.AccountRepository;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionControllerITTest {
    @LocalServerPort
    private int port;
    private TransferRequestDTO dto;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenService tokenService;
    String token;

    @BeforeEach
    void setup(){
        RestAssured.port = port;
        Account acc1 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
        Account acc2 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
        acc1.credit(1000L);
        acc2.credit(500L);
        accountRepository.saveAll(List.of(acc1, acc2));
        dto = new TransferRequestDTO(acc1.getId(), acc2.getId(), UUID.randomUUID(), "test", 100L);

        User user = userRepository.save(
                new User("test", "02030394084", "test@gmail.com", 20, "test@Password", "123456", LocalDateTime.now())
        );
        user.setVerified();

        token = tokenService.generateToken(user);
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Test
    @DisplayName("Should use the transfer endpoint in the controller and submit a transaction successfully")
    void transferRequest(){
        RestAssured.given()
                .auth().oauth2(token)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .post("/api/v1/ledger/transfer")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("correlationId", Matchers.equalTo(dto.correlationId().toString()))
                .body("status", Matchers.equalTo(TransactionCondition.PROCESSED.toString()));

        Account updatedSender = accountRepository.findById(dto.sourceId()).get();
        Account updatedReceiver = accountRepository.findById(dto.destinationId()).get();

        assertEquals(900L, updatedSender.getBalanceCents());
        assertEquals(600L, updatedReceiver.getBalanceCents());
    }
}