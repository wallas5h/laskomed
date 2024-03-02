package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.controller.AuthController;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.business.dao.UserDao;
import https.github.com.wallas5h.LaskoMed.business.services.AuthService;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserRoleEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserRoleJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtAuthenticationFilter;
import https.github.com.wallas5h.LaskoMed.security.config.ApplicationConfig;
import https.github.com.wallas5h.LaskoMed.security.config.SecurityConfig;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
import https.github.com.wallas5h.LaskoMed.util.EntityFixtures;
import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
//@WebMvcTest(controllers = AuthController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class AuthControllerMvcTest {

  private final MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @MockBean
  private UserDao userDao;

  @MockBean
  private UserRoleJpaRepository userRoleJpaRepository;

  @MockBean
  private RestTemplate restTemplate;

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
  }

  @Test
  public void thatRegisterUserFail() throws Exception {
    //given
    Map<String, Object> response = new HashMap<>();
    response.put("error", "Invalid role specified");

    RegisterRequest registerRequest = DtoFixtures.someUserRegisterRequest().withRole("");
    when(authService.register(registerRequest))
        .thenReturn(new ResponseEntity<>(response, HttpStatus.BAD_REQUEST));


    // When,then
    mockMvc.perform(
            post(AuthController.AUTH + AuthController.REGISTER)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(registerRequest))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", Matchers.is("Invalid role specified")));
  }


}

