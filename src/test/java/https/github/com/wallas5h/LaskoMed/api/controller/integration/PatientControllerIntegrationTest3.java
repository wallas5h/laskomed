package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.BookingAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.business.dao.AvailableAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.BookingAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.DoctorAvailabilityDAO;
import https.github.com.wallas5h.LaskoMed.business.services.AppointmentsService;
import https.github.com.wallas5h.LaskoMed.business.services.AuthService;
import https.github.com.wallas5h.LaskoMed.business.services.DoctorService;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DoctorJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import https.github.com.wallas5h.LaskoMed.util.DtoFixtures;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static https.github.com.wallas5h.LaskoMed.api.controller.PatientController.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientControllerIntegrationTest3 {

  private MockMvc mockMvc;

  private JwtTokenProvider jwtTokenProvider;

  private PatientMapper patientMapper;
  private PatientJpaRepository patientJpaRepository;
  private AuthService authService;
  private BookingAppointmentMapper bookingAppointmentMapper;
  private BookingAppointmentDAO bookingAppointmentDAO;
  private AvailableAppointmentDAO availableAppointmentDAO;
  private DoctorJpaRepository doctorJpaRepository;
  private EntityManager entityManager;
  private AppointmentsService appointmentsService;
  private DoctorAvailabilityDAO doctorAvailabilityDAO;
  private DoctorService doctorService;

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  @Test
  @Rollback
  void thatGetPatientDetailsIsCorrect() throws Exception {
    UserPatient userPatientData = getUserPatient();

    PatientEntity patientEntity = userPatientData.patientEntity().get();
    PatientDTO patientDTO = patientMapper.mapFromEntityToDto(patientEntity);
    String responseBody = asJsonString(patientDTO);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.get(BASE + PATIENT_DETAILS))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.patientId", Matchers.is(patientEntity.getPatientId().intValue())))
        .andExpect(jsonPath("$.pesel", Matchers.is(patientEntity.getPesel())))
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatPatientGetUpcomingAppointments() throws Exception {
    UserPatient userPatientData = getUserPatient();

    PatientEntity patientEntity = userPatientData.patientEntity().get();
    List<BookingAppointmentDTO> bookingAppointmentDAOByPatientId = bookingAppointmentDAO.findByPatientId(patientEntity.getPatientId());
    String responseBody = asJsonString(bookingAppointmentDAOByPatientId);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.get(BASE + APPOINTMENTS_UPCOMING))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatPatientCanGetAvailableMedicalAppointmentsWithoutParams() throws Exception {
    UserPatient userPatientData = getUserPatient();

    List<AvailableAppointmentDTO> availableMedicalAppointments = availableAppointmentDAO.getAvailableMedicalAppointments(LocalDate.now(), null, null);
    String responseBody = asJsonString(availableMedicalAppointments);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.get(BASE + PATIENTS_APPOINTMENTS))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatPatientCanGetAvailableMedicalAppointmentsWithParameters() throws Exception {
    //given
    UserPatient userPatientData = getUserPatient();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("specialization", "Ortopeda");
    params.add("location", "Warsaw");

    // doctor add availabilities
    createDoctorAvailabilities();

    List<AvailableAppointmentDTO> availableMedicalAppointments =
        availableAppointmentDAO.getAvailableMedicalAppointments(LocalDate.now(), "Ortopeda", "Warsaw");
    String responseBody = asJsonString(availableMedicalAppointments);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.get(BASE + PATIENTS_APPOINTMENTS),
        params)
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatPatientCanReserveMedicalAppointment() throws Exception {
    //given
    UserPatient userPatientData = getUserPatient();

    // doctor add availabilities
    createDoctorAvailabilities();

    //patient create a reservation request
    AppointmentReservationRequestDto reservationRequestDto = createPatientReservationRequest(userPatientData);

    String requestBodyContent = asJsonString(reservationRequestDto);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.post(BASE + PATIENTS_APPOINTMENTS_RESERVE),
        requestBodyContent)
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(contentAsString.contains("Booking appointment successfully"));
  }

  @Test
  @Rollback
  void thatPatientCanCancelMedicalAppointment() throws Exception {
    //given
    UserPatient userPatientData = getUserPatient();

    // doctor add availabilities
    createDoctorAvailabilities();

    // patient reserve a appointment
    Long bookingId = patientReserveAppointment(userPatientData).getBookingId();

    BookingAppointmentRequestDTO cancelRequest = BookingAppointmentRequestDTO.builder()
        .bookingId(bookingId.toString())
        .bookingStatus(EnumsContainer.BookingStatus.CANCELLED.name())
        .build();

    String requestBodyContent = asJsonString(cancelRequest);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.patch(BASE + APPOINTMENTS_UPCOMING),
        requestBodyContent)
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(contentAsString.contains("Booking canceled successfully"));
  }

  @Test
  @Rollback
  void thatPatientCanChangeStatusOfMedicalAppointment() throws Exception {
    //given
    UserPatient userPatientData = getUserPatient();

    // doctor add availabilities
    createDoctorAvailabilities();

    // patient reserve a appointment
    AppointmentReservationRequestDto reservationRequestDto = createPatientReservationRequest(userPatientData);
    BookingAppointmentEntity bookingAppointmentEntity = appointmentsService.reserveAppointment(reservationRequestDto);
    Long bookingId = bookingAppointmentEntity.getBookingId();

    BookingAppointmentRequestDTO cancelRequest = BookingAppointmentRequestDTO.builder()
        .bookingId(bookingId.toString())
        .bookingStatus(EnumsContainer.BookingStatus.CONFIRMED.name())
        .build();

    String requestBodyContent = asJsonString(cancelRequest);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.patch(BASE + APPOINTMENTS_UPCOMING),
        requestBodyContent)
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(contentAsString.contains("Booking changed successfully"));
  }


  @Test
  @Rollback
  void thatPatientCanGetResultOfExaminationById() throws Exception {
    //given
    UserPatient userPatientData = getUserPatient();
    UserDoctor userDoctor = getUserDoctor();

    // doctor add availabilities
    createDoctorAvailabilities();

    // patient reserve a appointment
    BookingAppointmentEntity bookingAppointmentEntity = patientReserveAppointment(userPatientData);
    Long bookingId = bookingAppointmentEntity.getBookingId();

    MedicalAppointmentDTO medicalAppointmentDTO =
        doctorAddResultOfExamination(bookingAppointmentEntity, userPatientData, bookingId, userDoctor);
    String stringAppointmentId = medicalAppointmentDTO.getAppointmentId().toString();
    String jsonAppointmentDetails = asJsonString(medicalAppointmentDTO);

    //then
    MvcResult result = mockMvcPerform(userPatientData,
        MockMvcRequestBuilders.get(BASE + PATIENT_APPOINTMENTS_HISTORY+ "/" +stringAppointmentId))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(contentAsString.equals(jsonAppointmentDetails));
  }

  private MedicalAppointmentDTO doctorAddResultOfExamination(
      BookingAppointmentEntity bookingAppointmentEntity,
      UserPatient userPatientData,
      Long bookingId,
      UserDoctor userDoctor) {
    MedicalAppointmentRequestDTO doctorsResultOfAppointmentRequest = MedicalAppointmentRequestDTO.builder()
        .appointmentStatus(EnumsContainer.AppointmentStatus.COMPLETED.name())
        .diagnosis("test")
        .doctorComment("test")
        .prescription("test")
        .clinicId(bookingAppointmentEntity.getClinic().getClinicId().toString())
        .patientId(userPatientData.patientEntity.get().getPatientId().toString())
        .bookingAppointmentId(bookingId.toString())
        .doctorId(userDoctor.doctorEntity.get().getDoctorId().toString())
        .addNewDisease(true)
        .build();

    MedicalAppointmentDTO medicalAppointmentDTO = doctorService.addMedicalAppointment(doctorsResultOfAppointmentRequest);
    return medicalAppointmentDTO;
  }

  private BookingAppointmentEntity patientReserveAppointment(UserPatient userPatientData) {
    AppointmentReservationRequestDto reservationRequestDto = createPatientReservationRequest(userPatientData);
    BookingAppointmentEntity bookingAppointmentEntity = appointmentsService.reserveAppointment(reservationRequestDto);
    return bookingAppointmentEntity;
  }

  @Test
  @Rollback
  void thatPatientGetAppointmentsHistory() throws Exception {
    UserPatient userPatientData = getUserPatient();

    PatientEntity patientEntity = userPatientData.patientEntity().get();

    List<MedicalAppointmentDTO> patientMedicalAppointments = appointmentsService.getPatientMedicalAppointments(patientEntity.getPatientId());
    String responseBody = asJsonString(patientMedicalAppointments);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.get(BASE + PATIENT_APPOINTMENTS_HISTORY))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatPatientGetAppointmentById() throws Exception {
    UserPatient userPatientData = getUserPatient();

    PatientEntity patientEntity = userPatientData.patientEntity().get();

    List<MedicalAppointmentDTO> patientMedicalAppointments = appointmentsService.getPatientMedicalAppointments(patientEntity.getPatientId());
    String responseBody = asJsonString(patientMedicalAppointments);

    //then
    MvcResult result = mockMvcPerform(userPatientData, MockMvcRequestBuilders.get(BASE + PATIENT_APPOINTMENTS_HISTORY))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  private AppointmentReservationRequestDto createPatientReservationRequest(UserPatient userPatientData) {
    List<AvailableAppointmentDTO> availableMedicalAppointments =
        availableAppointmentDAO.getAvailableMedicalAppointments(LocalDate.now(), null, null);

    AppointmentReservationRequestDto reservationRequestDto = AppointmentReservationRequestDto.builder()
        .availableAppointmentId(availableMedicalAppointments.get(0).getAvailableAppointmentId().toString())
        .patientId(userPatientData.patientEntity.get().getPatientId().toString())
        .appointmentType(EnumsContainer.AppointmentType.FACILITY.name())
        .build();
    return reservationRequestDto;
  }


  private void createDoctorAvailabilities() {
    DoctorEntity doctorEntity = getUserDoctor().doctorEntity.get();
    ClinicEntity clinic = entityManager.getReference(ClinicEntity.class, 1L);
    AvailabilityRequestDTO request = AvailabilityRequestDTO.builder()
        .dateAvailable(LocalDate.now().plusDays(1L).toString())
        .startTime("08:00:00")
        .endTime("16:00:00")
        .clinicId("1")
        .doctorId(doctorEntity.getDoctorId().toString())
        .build();

    DoctorAvailabilityEntity newAvailability = DoctorAvailabilityEntity.builder()
        .doctor(doctorEntity)
        .clinic(clinic)
        .dateAvailable(LocalDate.parse(request.getDateAvailable()))
        .startTime(LocalTime.parse(request.getStartTime()))
        .endTime(LocalTime.parse(request.getEndTime()))
        .build();

    DoctorAvailabilityEntity saved = doctorAvailabilityDAO.save(newAvailability);
    appointmentsService.createAppointmentsFromDoctorAvailabilities(DoctorService.convertRequestToCreatedAppointmentDTO2(request));
  }

  @NotNull
  private ResultActions mockMvcPerform(UserPatient userPatientData, MockHttpServletRequestBuilder method
  ) throws Exception {
    return mockMvc.perform(
        method
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userPatientData.token()));
  }
  @NotNull
  private ResultActions mockMvcPerform(UserPatient userPatientData, MockHttpServletRequestBuilder method,Long id
  ) throws Exception {
    return mockMvc.perform(
        method
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userPatientData.token()));
  }

  @NotNull
  private ResultActions mockMvcPerform(UserPatient userPatientData, MockHttpServletRequestBuilder method,
                                       MultiValueMap<String, String> requestParameters
  ) throws Exception {
    return mockMvc.perform(
        method
            .params(requestParameters)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userPatientData.token()));
  }

  @NotNull
  private ResultActions mockMvcPerform(UserPatient userPatientData, MockHttpServletRequestBuilder method,
                                       String content
  ) throws Exception {
    return mockMvc.perform(
        method
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userPatientData.token()));
  }

  @NotNull
  private UserPatient getUserPatient() {
    ResponseEntity<Map<String, Object>> loginResponseEntity = authService.login(DtoFixtures.patientLoginRequest());
    Map<String, Object> body = loginResponseEntity.getBody();
    String token = (String) body.get("token");
    String userId = jwtTokenProvider.getUserId(token);
    Optional<PatientEntity> patientEntity = patientJpaRepository.findByUserId(Long.valueOf(userId));
    UserPatient userPatientData = new UserPatient(token, patientEntity);
    return userPatientData;
  }

  @NotNull
  private UserDoctor getUserDoctor() {
    ResponseEntity<Map<String, Object>> loginResponseEntity = authService.login(DtoFixtures.doctorLoginRequest());
    Map<String, Object> body = loginResponseEntity.getBody();
    String token = (String) body.get("token");
    String userId = jwtTokenProvider.getUserId(token);
    Optional<DoctorEntity> doctorEntity = doctorJpaRepository.findByUserId(Long.valueOf(userId));
    return new UserDoctor(token, doctorEntity);
  }

  @NotNull
  private Result getPatientData(String userId) {
    Optional<PatientEntity> existingPatient = patientJpaRepository.findByUserId(Long.valueOf(userId));
    Long patientId = null;
    if (existingPatient.isPresent()) {
      patientId = existingPatient.get().getPatientId();
    }
    return new Result(existingPatient, patientId);
  }

  private record UserPatient(String token, Optional<PatientEntity> patientEntity) {
  }

  private record UserDoctor(String token, Optional<DoctorEntity> doctorEntity) {
  }

  private record Result(Optional<PatientEntity> existingPatient, Long patientId) {
  }

}
