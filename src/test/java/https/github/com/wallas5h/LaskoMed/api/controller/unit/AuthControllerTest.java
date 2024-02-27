package https.github.com.wallas5h.LaskoMed.api.controller.unit;

import https.github.com.wallas5h.LaskoMed.api.controller.AuthController;
import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.business.services.AuthService;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

  @Mock
  private AuthService authService;

  @InjectMocks
  private AuthController authController;


  @Test
  public void thatRegisterUserWorksCorrectly() {
    //given
    Map<String, String> successResponse = Map.of("message", "User registered successfully");

    when(authService.register(any(RegisterRequest.class)))
        .thenReturn(ResponseEntity.ok(Map.of("message", "User registered successfully")));


    //when
    ResponseEntity<Map<String, Object>> responseEntity = authController.register(DtoFixtures.someUserRegisterRequest());

    //then
    assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().containsKey("message"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(successResponse);
  }

  @Test
  public void thatRegisterUserFailure() {
    //given
    Map<String, String> errorResponse = Map.of("error", "");

    when(authService.register(any(RegisterRequest.class)))
        .thenReturn(ResponseEntity.badRequest().body(Map.of("error", "")));

    //when
    ResponseEntity<Map<String, Object>> responseEntity = authController.register(DtoFixtures.someUserRegisterRequest());

    //then
    assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().containsKey("error"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(responseEntity.getBody()).isEqualTo(errorResponse);
  }

  @Test
  public void thatRegisterUserFailure2() {
    //given
    Map<String, String> errorResponse = Map.of("error", "");

    when(authService.register(any(RegisterRequest.class)))
        .thenReturn(ResponseEntity.internalServerError().body(Map.of("error", "")));

    //when
    ResponseEntity<Map<String, Object>> responseEntity = authController.register(DtoFixtures.someUserRegisterRequest());

    //then
    assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().containsKey("error"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(responseEntity.getBody()).isEqualTo(errorResponse);
  }

  @Test
  public void thatLoginUserWorksCorrectly() {
    //given
    Map<String, String> successResponse = Map.of(
        "message", "Login successful",
        "token", "someToken"
    );

    when(authService.login(any(LoginRequest.class)))
        .thenReturn(ResponseEntity.ok(
            Map.of(
                "message", "Login successful",
                "token", "someToken"
            )));

    //when
    ResponseEntity<Map<String, Object>> responseEntity = authController.login(DtoFixtures.someUserLoginRequest());

    //then
    assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().containsKey("message"));
    Assert.assertTrue(responseEntity.getBody().containsKey("token"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(successResponse);
  }

  @Test
  public void thatLoginUserFailure() {
    //given
    Map<String, String> response = Map.of("error", "Invalid credentials");

    when(authService.login(any(LoginRequest.class)))
        .thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            Map.of("error", "Invalid credentials")));

    //when
    ResponseEntity<Map<String, Object>> responseEntity = authController.login(DtoFixtures.someUserLoginRequest());

    //then
    assertNotNull(responseEntity.getBody());
    Assert.assertTrue(responseEntity.getBody().containsKey("error"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(responseEntity.getBody()).isEqualTo(response);
  }
}
