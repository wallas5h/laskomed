package https.github.com.wallas5h.LaskoMed.api.controller.integration;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.BookingAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.DoctorMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.business.dao.AvailableAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.BookingAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.DoctorAvailabilityDAO;
import https.github.com.wallas5h.LaskoMed.business.services.AppointmentsService;
import https.github.com.wallas5h.LaskoMed.business.services.AuthService;
import https.github.com.wallas5h.LaskoMed.business.services.DoctorService;
import https.github.com.wallas5h.LaskoMed.business.services.PatientService;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.DoctorAvailabilityRepository;
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

import static https.github.com.wallas5h.LaskoMed.api.controller.DoctorController.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorControllerIntegrationTestPart2 {

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
  private DoctorMapper doctorMapper;

  private DoctorAvailabilityRepository doctorAvailabilityRepository;
  private PatientService patientService;

  private static String asJsonString(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  @Test
  @Rollback
  void thatGetDoctorDetailsIsCorrect() throws Exception {

    UserDoctor userDoctor = getUserDoctor();

    DoctorEntity doctorEntity = userDoctor.doctorEntity().get();
    DoctorDTO doctorDTO = doctorMapper.mapFromEntityToDto(doctorEntity);
    String responseBody = asJsonString(doctorDTO);

    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.get(BASE + DOCTOR_DETAILS))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.doctorId", Matchers.is(doctorEntity.getDoctorId().intValue())))
        .andExpect(jsonPath("$.pesel", Matchers.is(doctorEntity.getPesel())))
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatDoctorGetAllAvailabilities() throws Exception {

    UserDoctor userDoctor = getUserDoctor();

    createDoctorAvailabilities();

    List<DoctorAvailabilityDTO> doctorAvailabilityDTOS = doctorAvailabilityRepository.findByDoctorId(userDoctor.doctorEntity.get().getDoctorId());

    String responseBody = asJsonString(doctorAvailabilityDTOS);

    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.get(BASE + DOCTORS_AVAILABILITIES))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatDoctorGetPresentAvailabilities() throws Exception {

    UserDoctor userDoctor = getUserDoctor();

    createDoctorAvailabilities();

    List<DoctorAvailabilityDTO> presentAvailabilities =
        doctorAvailabilityRepository.findPresentAvailabilities(userDoctor.doctorEntity.get().getDoctorId(), LocalDate.now());

    String responseBody = asJsonString(presentAvailabilities);

    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.get(BASE + DOCTORS_AVAILABILITIES_PRESENT))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    Assertions.assertThat(responseBody.equals(contentAsString));
  }


  @Test
  @Rollback
  void thatDoctorGetUpcomingAppointments() throws Exception {

    UserDoctor userDoctor = getUserDoctor();
    UserPatient userPatientData = getUserPatient();

    // doctor add availabilities
    createDoctorAvailabilities();

    //patient create a reservation request
    AppointmentReservationRequestDto reservationRequestDto = createPatientReservationRequest(userPatientData);

    List<BookingAppointmentDTO> doctorUpcomingAppointments =
        doctorService.getDoctorUpcomingAppointments(userDoctor.doctorEntity().get().getDoctorId());


    String responseBody = asJsonString(doctorUpcomingAppointments);

    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.get(BASE + DOCTORS_APPOINTMENTS_UPCOMING))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatDoctorCreateAvailabilities() throws Exception {

    UserDoctor userDoctor = getUserDoctor();

    // doctor create availabilities request
    DoctorEntity doctorEntity = getUserDoctor().doctorEntity.get();
    AvailabilityRequestDTO request = AvailabilityRequestDTO.builder()
        .dateAvailable(LocalDate.now().plusDays(1L).toString())
        .startTime("08:00:00")
        .endTime("16:00:00")
        .clinicId("1")
        .doctorId(doctorEntity.getDoctorId().toString())
        .build();

    String content = asJsonString(request);

    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.post(BASE + DOCTORS_AVAILABILITIES_CREATE),
        content)
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    Assertions.assertThat(contentAsString.contains("Availability added successfully"));
  }

  @Test
  @Rollback
  void thatDoctorCreateAvailabilitiesFail() throws Exception {

    UserDoctor userDoctor = getUserDoctor();

    // doctor add availabilities
    createDoctorAvailabilities();

    // doctor create second Time availabilities request
    DoctorEntity doctorEntity = getUserDoctor().doctorEntity.get();
    AvailabilityRequestDTO request = AvailabilityRequestDTO.builder()
        .dateAvailable(LocalDate.now().plusDays(1L).toString())
        .startTime("08:00:00")
        .endTime("16:00:00")
        .clinicId("1")
        .doctorId(doctorEntity.getDoctorId().toString())
        .build();

    String content = asJsonString(request);

    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.post(BASE + DOCTORS_AVAILABILITIES_CREATE),
        content)
        .andExpect(status().isBadRequest())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();

    Assertions.assertThat(contentAsString
        .contains("Your availabilities can not be added. There is a conflict of yours available dates or hours"));
  }

  @Test
  @Rollback
  void thatDoctorCreateMedicalAppointmentCorrect() throws Exception {

    UserDoctor userDoctor = getUserDoctor();
    UserPatient userPatient = getUserPatient();

    // doctor add availabilities
    createDoctorAvailabilities();

    // patient reserve a appointment
    BookingAppointmentEntity bookingAppointmentEntity = patientReserveAppointment(userPatient);
    Long bookingId = bookingAppointmentEntity.getBookingId();

    MedicalAppointmentRequestDTO doctorsResultOfAppointmentRequest = MedicalAppointmentRequestDTO.builder()
        .appointmentStatus(EnumsContainer.AppointmentStatus.COMPLETED.name())
        .diagnosis("test")
        .doctorComment("test")
        .prescription("test")
        .clinicId(bookingAppointmentEntity.getClinic().getClinicId().toString())
        .patientId(userPatient.patientEntity.get().getPatientId().toString())
        .bookingAppointmentId(bookingId.toString())
        .doctorId(userDoctor.doctorEntity.get().getDoctorId().toString())
        .addNewDisease(false)
        .build();

    String content = asJsonString(doctorsResultOfAppointmentRequest);

    //then
     mockMvcPerform(userDoctor, MockMvcRequestBuilders.post(BASE + DOCTORS_PROCESSING_APPOINTMENT),
        content)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", Matchers.is("Medical appointment added")))
        .andExpect(jsonPath("$.appointment", Matchers.notNullValue()));

  }

  @Test
  @Rollback
  void thatDoctorCanGetPatientAppointmentsBySpecialization() throws Exception {

    UserDoctor userDoctor = getUserDoctor();
    UserPatient userPatient = getUserPatient();

    // doctor add availabilities
    createDoctorAvailabilities();

    // patient reserve a appointment
    BookingAppointmentEntity bookingAppointmentEntity = patientReserveAppointment(userPatient);
    Long bookingId = bookingAppointmentEntity.getBookingId();

    // doctor add notes to appointment after examination
    MedicalAppointmentRequestDTO doctorsResultOfAppointmentRequest = doctorAddMedicalAppointmentAfterExamination(bookingAppointmentEntity, userPatient, bookingId, userDoctor);

    String specialization = bookingAppointmentEntity.getDoctor().getSpecialization();

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("specialization", specialization);

    Long patientId = userPatient.patientEntity.get().getPatientId();
    List<MedicalAppointmentDTO> patientAppointments = patientService.getPatientAppointments(patientId, specialization);

    String responseBody = asJsonString(patientAppointments);

    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.get(BASE + APPOINTMENTS_PATIENT,patientId),
        params)
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(responseBody.equals(contentAsString));
  }

  @Test
  @Rollback
  void thatDoctorCanGetPatientDisease() throws Exception {

    UserDoctor userDoctor = getUserDoctor();
    UserPatient userPatient = getUserPatient();
    Long patientId = userPatient.patientEntity.get().getPatientId();

    // doctor add availabilities
    createDoctorAvailabilities();

    // patient reserve a appointment
    BookingAppointmentEntity bookingAppointmentEntity = patientReserveAppointment(userPatient);
    Long bookingId = bookingAppointmentEntity.getBookingId();

    // doctor add notes to appointment after examination
    MedicalAppointmentRequestDTO medicalAppointmentRequestDTO = doctorAddMedicalAppointmentAfterExamination(bookingAppointmentEntity, userPatient, bookingId, userDoctor);

    //get diagnosis/disease name
    String diagnosisName = medicalAppointmentRequestDTO.getDiagnosis();


    //then
    MvcResult result = mockMvcPerform(userDoctor, MockMvcRequestBuilders.get(BASE + APPOINTMENTS_PATIENT,patientId))
        .andExpect(status().isOk())
        .andReturn();

    String contentAsString = result.getResponse().getContentAsString();
    Assertions.assertThat(contentAsString.contains(diagnosisName));
  }

  private MedicalAppointmentRequestDTO doctorAddMedicalAppointmentAfterExamination(BookingAppointmentEntity bookingAppointmentEntity, UserPatient userPatient, Long bookingId, UserDoctor userDoctor) {
    MedicalAppointmentRequestDTO doctorsResultOfAppointmentRequest = MedicalAppointmentRequestDTO.builder()
        .appointmentStatus(EnumsContainer.AppointmentStatus.COMPLETED.name())
        .diagnosis("test")
        .doctorComment("test")
        .prescription("test")
        .clinicId(bookingAppointmentEntity.getClinic().getClinicId().toString())
        .patientId(userPatient.patientEntity.get().getPatientId().toString())
        .bookingAppointmentId(bookingId.toString())
        .doctorId(userDoctor.doctorEntity.get().getDoctorId().toString())
        .addNewDisease(true)
        .build();

    doctorService.addMedicalAppointment(doctorsResultOfAppointmentRequest);
    return doctorsResultOfAppointmentRequest;
  }


  private BookingAppointmentEntity patientReserveAppointment(UserPatient userPatientData) {
    AppointmentReservationRequestDto reservationRequestDto = createPatientReservationRequest(userPatientData);
    BookingAppointmentEntity bookingAppointmentEntity = appointmentsService.reserveAppointment(reservationRequestDto);
    return bookingAppointmentEntity;
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
  private ResultActions mockMvcPerform(UserDoctor userDoctor, MockHttpServletRequestBuilder method
  ) throws Exception {
    return mockMvc.perform(
        method
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userDoctor.token()));
  }

  @NotNull
  private ResultActions mockMvcPerform(UserDoctor userDoctor, MockHttpServletRequestBuilder method,
                                       MultiValueMap<String, String> requestParameters
  ) throws Exception {
    return mockMvc.perform(
        method
            .params(requestParameters)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userDoctor.token()));
  }

  @NotNull
  private ResultActions mockMvcPerform(UserDoctor userDoctor, MockHttpServletRequestBuilder method,
                                       String content
  ) throws Exception {
    return mockMvc.perform(
        method
            .content(content)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userDoctor.token()));
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
