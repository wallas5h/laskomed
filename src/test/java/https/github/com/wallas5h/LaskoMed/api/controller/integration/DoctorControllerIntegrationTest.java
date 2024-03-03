package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.controller.DoctorController;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorCreateRequest;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorMapper;
import https.github.com.wallas5h.LaskoMed.business.dao.DoctorDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.UserJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static https.github.com.wallas5h.LaskoMed.api.controller.PatientController.BASE;
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
public class DoctorControllerIntegrationTest {


  private MockMvc mockMvc;

  private JwtTokenProvider jwtTokenProvider;

  private DoctorMapper doctorMapper;
  private DoctorJpaRepository doctorJpaRepository;

  @MockBean
  private DoctorDAO doctorDAO;

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

  // TODO MockHttpServletResponse:
  //           Status = 401
  //    Error message = Full authentication is required to access this resource

  @Test
  @Rollback
  void thatDoctorCreateCorrect() throws Exception {
    DoctorCreateRequest doctorCreateRequest = DtoFixtures.someDoctorCreateRequest();
     long userId=123L;

    UserEntity user = someUserDoctorEntity().withUserId(userId);
    when(entityManager.getReference(UserEntity.class, userId))
        .thenReturn(user);
    when(userJpaRepository.findByUsernameOrEmail(any(String.class),any(String.class)))
        .thenReturn(Optional.of(user));

    //then
    mockMvc.perform(
        MockMvcRequestBuilders.post(DoctorController.BASE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(doctorCreateRequest))
            .header(HttpHeaders.AUTHORIZATION, generateToken()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.message").value("Doctor added successfully"));
  }

  @Test
  @Rollback
  void thatDoctorCreateFailByExistingDoctor() throws Exception {
    DoctorCreateRequest doctorCreateRequest = DtoFixtures.someDoctorCreateRequest();
     long userId=123L;

    when(doctorDAO.findByUserIdOptional(userId))
        .thenReturn(Optional.of(DoctorEntity.builder().build()));

    mockingJwtSecurity(userId);

    //then
    mockMvc.perform(
        MockMvcRequestBuilders.post(BASE )
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(doctorCreateRequest))
            .header(HttpHeaders.AUTHORIZATION, generateToken()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").exists())
        .andExpect(jsonPath("$.error").isNotEmpty());
  }

  private void mockingJwtSecurity(long userId) {
    UserEntity user = someUserDoctorEntity().withUserId(userId);
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
