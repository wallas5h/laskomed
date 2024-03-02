package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.controller.AuthController;
import https.github.com.wallas5h.LaskoMed.api.controller.PatientController;
import https.github.com.wallas5h.LaskoMed.api.dto.LoginRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.PatientCreateRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.PatientDTO;
import https.github.com.wallas5h.LaskoMed.api.dto.RegisterRequest;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.UserServiceAdvice;
import https.github.com.wallas5h.LaskoMed.business.dao.PatientDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.UserDao;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.PatientRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
import https.github.com.wallas5h.LaskoMed.util.EntityFixtures;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static https.github.com.wallas5h.LaskoMed.api.controller.PatientController.*;
import static https.github.com.wallas5h.LaskoMed.util.EntityFixtures.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientControllerIntegrationTest {


  private MockMvc mockMvc;

  private JwtTokenProvider jwtTokenProvider;

  private PatientMapper patientMapper;
  private PatientJpaRepository patientJpaRepository;

  @MockBean
  private PatientDAO patientDAO;

  @MockBean
  private AuthenticationManager authenticationManager;
  @MockBean
  private EntityManager entityManager;
  @MockBean
  private UserJpaRepository userJpaRepository;



  String generateToken(){
    UserEntity userEntity = someUserEntity().withUserId(123L);
    String token = jwtTokenProvider.generateToken(userEntity);
    return "Bearer " + token;
  }

  @Test
  @Rollback
  void thatPatientCreateCorrect() throws Exception {
    PatientCreateRequest patientCreateRequest = DtoFixtures.somePatientCreateRequest();
     long userId=123L;

    mockingJwtSecurity(userId);

    //then
    mockMvc.perform(
        MockMvcRequestBuilders.post(BASE )
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(patientCreateRequest))
            .header(HttpHeaders.AUTHORIZATION, generateToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.message").value("Patient added successfully"));
  }

  @Test
  @Rollback
  void thatPatientCreateFailByExistingPatient() throws Exception {
    PatientCreateRequest patientCreateRequest = DtoFixtures.somePatientCreateRequest();
     long userId=123L;

    when(patientDAO.findByUserIdOptional(userId))
        .thenReturn(Optional.of(PatientEntity.builder().build()));
    mockingJwtSecurity(userId);

    //then
    mockMvc.perform(
        MockMvcRequestBuilders.post(BASE )
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(patientCreateRequest))
            .header(HttpHeaders.AUTHORIZATION, generateToken()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.error").isNotEmpty());
  }

  private void mockingJwtSecurity(long userId) {
    UserEntity user = someUserEntity().withUserId(userId);
    when(entityManager.getReference(UserEntity.class, userId))
        .thenReturn(user);
    when(userJpaRepository.findByUsernameOrEmail(any(String.class),any(String.class)))
            .thenReturn(Optional.of(user));
  }

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}
