package com.nexuspay.ledger.web;

import com.nexuspay.ledger.api.dto.TransferRequestDTO;
import com.nexuspay.ledger.domain.valueobject.CurrencyCode;
import com.nexuspay.ledger.internal.domain.model.Account;
import com.nexuspay.ledger.internal.domain.model.TransactionCondition;
import com.nexuspay.ledger.internal.infra.AccountRepository;
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

import java.util.List;
import java.util.UUID;

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

    @BeforeEach
    void setup(){
        RestAssured.port = port;
        Account acc1 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
        Account acc2 = new Account(UUID.randomUUID(), CurrencyCode.BRL);
        acc1.credit(1000L);
        acc2.credit(500L);
        accountRepository.saveAll(List.of(acc1, acc2));
        dto = new TransferRequestDTO(acc1.getId(), acc2.getId(), UUID.randomUUID(), "test", 100L);
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @Test
    @DisplayName("Should use the transfer endpoint in the controller and submit a transaction successfully")
    void transferRequest(){
        RestAssured.given()
                .when()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .post("/api/v1/ledger/transfer")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("correlationId", Matchers.equalTo(dto.correlationId().toString()))
                .body("status", Matchers.equalTo(TransactionCondition.PROCESSED.toString()));
    }
}