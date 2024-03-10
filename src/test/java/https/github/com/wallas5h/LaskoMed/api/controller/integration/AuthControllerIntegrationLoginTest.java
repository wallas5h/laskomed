package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.controller.AuthController;
import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.UserRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserJpaRepository;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
import https.github.com.wallas5h.LaskoMed.util.EntityFixtures;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static https.github.com.wallas5h.LaskoMed.util.EntityFixtures.someUserDoctorEntity;
import static https.github.com.wallas5h.LaskoMed.util.EntityFixtures.someUserEntity;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthControllerIntegrationLoginTest {

  @Autowired
  private MockMvc mockMvc;

  private UserJpaRepository userJpaRepository;
  private PasswordEncoder passwordEncoder;


  @Test
  @Rollback
  void testLoginSuccessful() throws Exception {
    //given
    createUser();
    LoginRequest loginRequest = DtoFixtures.someUserLoginRequest();

    //then
    mockMvc.perform(MockMvcRequestBuilders.post(AuthController.AUTH + AuthController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.message").value("Login successful"));
  }

  @Test
  @Rollback
  void testLoginFailByIncorrectCredentials() throws Exception {
    //given
    createUser();
    LoginRequest loginRequest = DtoFixtures.someUserLoginRequest().withUsernameOrEmail("abc");

    //then
    mockMvc.perform(MockMvcRequestBuilders.post(AuthController.AUTH + AuthController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(loginRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Invalid credentials"));
  }

  @Test
  @Rollback
  void testLoginFailByAuthentication() throws Exception {
    //given
    createUser();
    LoginRequest loginRequest = DtoFixtures.someUserLoginRequest().withPassword("");

    //then
    mockMvc.perform(MockMvcRequestBuilders.post(AuthController.AUTH + AuthController.LOGIN)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error").value("Invalid authentication"));
  }

  private UserEntity createUser() {
    UserEntity userEntity = someUserEntity();
    return userJpaRepository.saveAndFlush(
        userEntity.withPassword(passwordEncoder.encode(userEntity.getPassword())));

  }

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}
