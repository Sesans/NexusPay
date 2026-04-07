package com.nexuspay;

import com.nexuspay.auth.application.dto.UserRequestDTO;
import com.nexuspay.auth.application.dto.VerifyOTP;
import com.nexuspay.auth.domain.model.User;
import com.nexuspay.auth.domain.model.UserStatus;
import com.nexuspay.auth.domain.model.VerificationCode;
import com.nexuspay.auth.domain.repository.UserRepository;
import com.nexuspay.auth.domain.repository.VerificationCodeRepository;
import com.nexuspay.auth.infra.security.TokenService;
import io.restassured.RestAssured;
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
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class NexusPayApplicationTests {
	@LocalServerPort
	private int port;
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

	@Autowired
	VerificationCodeRepository verificationRepository;
	@Autowired
	TokenService tokenService;
	@Autowired
	UserRepository userRepository;
	UserRequestDTO dto;

	@BeforeEach
	void setup(){
		RestAssured.port = port;
		dto = new UserRequestDTO(
				"Test",
				"10343354055",
				"test@gmail.com",
				18,
				"passwordTest",
				"123456");
	}

	@Test
	@DisplayName("User journey through the application: Registering and validating successfully")
	void userCreationAndValidation(){
		//Create User
		var userResponse = RestAssured.given()
				.log().all()
				.when()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(dto)
				.when()
				.post("/api/v1/auth/register")
				.then()
				.log().all()
				.statusCode(HttpStatus.CREATED.value())
				.extract();

		String token = userResponse.path("token");
		UUID userId = tokenService.validateAndGetSubject(token);

		//Retrieving the OTP code
        VerificationCode code = Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .until(() -> verificationRepository.findByUserId(userId).orElse(null),
                        Objects::nonNull);
		VerifyOTP otp = new VerifyOTP(code.getCode());

		//Validate User
		RestAssured.given()
				.auth().oauth2(token)
				.log().all()
				.when()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(otp)
				.when()
				.post("/api/v1/auth/verify")
				.then()
				.log().all()
				.statusCode(HttpStatus.OK.value());

		//Assert
        Optional<User> user = userRepository.findById(userId);
        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getStatus()).isEqualTo(UserStatus.VERIFIED);
	}
}