package https.github.com.wallas5h.LaskoMed.api.controller.unit;

import https.github.com.wallas5h.LaskoMed.api.controller.DoctorController;
import https.github.com.wallas5h.LaskoMed.api.dto.DoctorCreateRequest;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {


  @Mock
  private DoctorService doctorService;
  @Mock
  private PatientService patientService;
  @Mock
  private UserServiceAdvice userServiceAdvice;

  @InjectMocks
  private DoctorController doctorController;

  @Test
  public void thatCreateDoctorWorksCorrectly() throws Exception {
    //given
    Map<String, String> response = Map.of("message", "Doctor added successfully");

    when(userServiceAdvice.getUserId()).thenReturn(1L);

    doNothing().when(doctorService).createDoctor(any(DoctorCreateRequest.class), any(Long.class));

    //when
    ResponseEntity<Map<String, String>> responseEntity = doctorController.createDoctor(DtoFixtures.someDoctorCreateRequest());

    //then
    assertNotNull(responseEntity.getBody());
    assertTrue(responseEntity.getBody().containsKey("message"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(responseEntity.getBody()).isEqualTo(response);
  }

  @Test
  public void thatCreateDoctorFailure() throws Exception {
    //given
    Map<String, String> response = Map.of("error", "Some error message");

    when(userServiceAdvice.getUserId()).thenReturn(1L);

    doAnswer(invocation -> {
      throw new Exception("Some error message");
    })
        .when(doctorService).createDoctor(any(DoctorCreateRequest.class), any(Long.class));

    //when
    ResponseEntity<Map<String, String>> responseEntity = doctorController.createDoctor(DtoFixtures.someDoctorCreateRequest());

    //then
    assertNotNull(responseEntity.getBody());
    assertTrue(responseEntity.getBody().containsKey("error"));
    assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(responseEntity.getBody()).isEqualTo(response);
  }

}