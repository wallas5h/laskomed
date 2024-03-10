package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.controller.AuthController;
import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIntegrationRegisterTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationManager authenticationManager;

  @Test
  @Rollback
  void shouldRegisterUserSuccessfully() throws Exception {
    // given
    RegisterRequest registerRequest = DtoFixtures.someUserRegisterRequest();

    // When,then
    ResultActions perform = mockMvc.perform(
        MockMvcRequestBuilders.post(AuthController.AUTH + AuthController.REGISTER)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(registerRequest)));

    perform
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", Matchers.is("User registered successfully")));
    
  }

  @ParameterizedTest
  @MethodSource
  @Rollback
  void shouldRegisterFail(RegisterRequest registerRequest) throws Exception {

    // When,then
    ResultActions perform = mockMvc.perform(
        MockMvcRequestBuilders.post(AuthController.AUTH + AuthController.REGISTER)
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(registerRequest)));

    perform
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", Matchers.containsString("Invalid")));

  }

  public static Stream<Arguments> shouldRegisterFail(){
    RegisterRequest registerRequest = DtoFixtures.someUserRegisterRequest();
    return Stream.of(
    Arguments.of( registerRequest.withRole("")),
    Arguments.of( registerRequest.withUsername("")),
    Arguments.of( registerRequest.withUsername("aa")),
    Arguments.of( registerRequest.withEmail("")),
    Arguments.of( registerRequest.withEmail("a.mail.com")),
    Arguments.of( registerRequest.withPassword(""))
        );
  }

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}
