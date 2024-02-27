package https.github.com.wallas5h.LaskoMed.api.controller.unit;

import https.github.com.wallas5h.LaskoMed.api.controller.DoctorController;
import https.github.com.wallas5h.LaskoMed.api.controller.PatientController;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorCreateRequest;
import https.github.com.wallas5h.LaskoMed.api.dto.PatientCreateRequest;
import https.github.com.wallas5h.LaskoMed.api.utils.UserServiceAdvice;
import https.github.com.wallas5h.LaskoMed.business.services.DoctorService;
import https.github.com.wallas5h.LaskoMed.business.services.PatientService;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

  @Mock
  private PatientService patientService;
  @Mock
  private UserServiceAdvice userServiceAdvice;

  @InjectMocks
  private PatientController patientController;

  @Test
  public void thatCreatePatientWorksCorrectly() throws Exception {
    //given
    Map<String, Object> response = Map.of("message", "Patient added successfully");

    when(userServiceAdvice.getUserId()).thenReturn(1L);

    doNothing().when(patientService).createPatient(any(PatientCreateRequest.class), any(Long.class));

    //when
    ResponseEntity<Map<String, Object>> responseEntity = patientController.createPatient(DtoFixtures.somePatientCreateRequest());
    //then
    assertNotNull(responseEntity.getBody());
    assertTrue(responseEntity.getBody().containsKey("message"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(response);
  }

  @Test
  public void thatCreatePatientFailure() throws Exception {
    //given
    Map<String, String> response = Map.of("error", "Some error message");

    when(userServiceAdvice.getUserId()).thenReturn(1L);

    doAnswer(invocation -> {throw new Exception("Some error message");})
        .when(patientService).createPatient(any(PatientCreateRequest.class), any(Long.class));

    //when
    ResponseEntity<Map<String, Object>> responseEntity  = patientController.createPatient(DtoFixtures.somePatientCreateRequest());

    //then
    assertNotNull(responseEntity.getBody());
    assertTrue(responseEntity.getBody().containsKey("error"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(responseEntity.getBody()).isEqualTo(response);
  }

}