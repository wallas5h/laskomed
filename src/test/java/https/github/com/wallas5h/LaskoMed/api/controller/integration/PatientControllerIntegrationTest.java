package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientCreateRequest;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static https.github.com.wallas5h.LaskoMed.api.controller.PatientController.BASE;
import static https.github.com.wallas5h.LaskoMed.util.DtoFixtures.somePatientCreateRequest;
import static https.github.com.wallas5h.LaskoMed.util.EntityFixtures.somePatientEntity;
import static https.github.com.wallas5h.LaskoMed.util.EntityFixtures.someUserEntity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientControllerIntegrationTest {

  @Autowired
  private final MockMvc mockMvc;

  private JwtTokenProvider jwtTokenProvider;
  private UserJpaRepository userJpaRepository;
  private PatientJpaRepository patientJpaRepository;

  String generateToken(UserEntity user) {
    UserEntity userEntity = user;
    String token = jwtTokenProvider.generateToken(userEntity);
    return "Bearer " + token;
  }

  @Test
  @Rollback
  void thatPatientCreateCorrect() throws Exception {
    //given
    UserEntity user = createUser();
    PatientCreateRequest patientCreateRequest = somePatientCreateRequest();

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(patientCreateRequest))
                .header(HttpHeaders.AUTHORIZATION, generateToken(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.message").value("Patient added successfully"));
  }

  @Test
  @Rollback
  void thatPatientCreateFailByExistingPatient() throws Exception {
    //given
    UserEntity user = createUser();
    PatientCreateRequest patientCreateRequest = somePatientCreateRequest();
    createPatient(user);


    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(patientCreateRequest))
                .header(HttpHeaders.AUTHORIZATION, generateToken(user)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.error").isNotEmpty());
  }
  private PatientEntity createPatient(UserEntity userEntity) {
    return patientJpaRepository.saveAndFlush(somePatientEntity().withAppUser(userEntity));
  }

  private UserEntity createUser() {
    return userJpaRepository.saveAndFlush(someUserEntity());
  }

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

}