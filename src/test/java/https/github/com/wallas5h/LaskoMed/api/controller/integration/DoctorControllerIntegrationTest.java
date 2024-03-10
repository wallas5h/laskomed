package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.controller.DoctorController;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorCreateRequest;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
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

import static https.github.com.wallas5h.LaskoMed.util.EntityFixtures.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  private UserJpaRepository userJpaRepository;
  private DoctorJpaRepository doctorJpaRepository;

  @Test
  @Rollback
  void thatDoctorCreateCorrect() throws Exception {
    UserEntity user = createUser();
    DoctorCreateRequest doctorCreateRequest = DtoFixtures.someDoctorCreateRequest();

    //then
    mockMvc.perform(
        MockMvcRequestBuilders.post(DoctorController.BASE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(doctorCreateRequest))
            .header(HttpHeaders.AUTHORIZATION, generateToken(user)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.message").value("Doctor added successfully"));
  }

  @Test
  @Rollback
  void thatDoctorCreateFailByExistingDoctor() throws Exception {
    UserEntity user = createUser();
    createDoctor(user);
    DoctorCreateRequest doctorCreateRequest = DtoFixtures.someDoctorCreateRequest();

    //then
    mockMvc.perform(
            MockMvcRequestBuilders.post(DoctorController.BASE )
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(doctorCreateRequest))
                .header(HttpHeaders.AUTHORIZATION, generateToken(user)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.error").isNotEmpty());
  }

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  String generateToken(UserEntity user) {
    UserEntity userEntity = user;
    String token = jwtTokenProvider.generateToken(userEntity);
    return "Bearer " + token;
  }

  private UserEntity createUser() {
    return userJpaRepository.saveAndFlush(someUserDoctorEntity());
  }
  private DoctorEntity createDoctor(UserEntity user) {
    return doctorJpaRepository.saveAndFlush(someDoctorEntity2().withAppUser(user));
  }


}
