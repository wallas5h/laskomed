package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.*;
import https.github.com.wallas5h.LaskoMed.api.mapper.DiagnosedDiseaseMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.PatientMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.business.dao.PatientDAO;
import https.github.com.wallas5h.LaskoMed.business.dao.UserDao;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.AddressEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.BookingAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AvailableAppointmentJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DiagnosesDiseaseJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.PatientJpaRepository;
import https.github.com.wallas5h.LaskoMed.security.JwtTokenProvider;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PatientService {
  private final PatientDAO patientDAO;
  private final UserDao userDao;

  private PatientJpaRepository patientJpaRepository;
  private AvailableAppointmentJpaRepository availableAppointmentJpaRepository;
  private DiagnosesDiseaseJpaRepository diseaseRepository;

  private AppointmentsService appointmentsService;
  private PatientMapper patientMapper;
  private DiagnosedDiseaseMapper diagnosedDiseaseMapper;
  private EntityManager entityManager;
  private JwtTokenProvider jwtTokenProvider;

  public PatientDTO getPatientDetails(Long patientId) {
   return patientDAO.findById(patientId);

  }

  public List<BookingAppointmentDTO> getPatientUpcomingAppointments(Long patientId) {
    return appointmentsService.getPatientUpcomingBookings(patientId);
  }
  public List<MedicalAppointmentDTO> getPatientAppointments(Long patientId) {
    return appointmentsService.getPatientMedicalAppointments(patientId);
  }

  public List<MedicalAppointmentDTO> getPatientAppointments(Long patientId, String specialization) {
    specialization = specialization == null ? "" : specialization;

    if (specialization.isEmpty()){
      return appointmentsService.getPatientMedicalAppointments(patientId);
    }
    return appointmentsService.getPatientMedicalAppointments(patientId, specialization);
  }

  public ResponseEntity<?> changeBookingStatus(Long patientId, BookingAppointmentRequestDTO request) {
    int changedBookingStatus = appointmentsService.changeBookingStatus(patientId, request);
    if (changedBookingStatus < 1) {
      return ResponseEntity.status(400).body("Incorrect input data");
    } else {
      if (request.getBookingStatus().toUpperCase().equals(EnumsContainer.BookingStatus.CANCELLED.name())){
        return ResponseEntity.ok("Booking canceled successfully");
      }
      return ResponseEntity.ok("Booking changed successfully");
    }
  }

  public boolean isBookingStatusValid(BookingAppointmentRequestDTO request) {
    try {
      EnumsContainer.BookingStatus status = EnumsContainer.BookingStatus.valueOf(request.getBookingStatus().toUpperCase());
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public MedicalAppointmentDTO getPatientMedicalAppointmentDetails(Long patientId, Long appointmentId) {
    return appointmentsService.getPatientMedicalAppointmentDeatails(appointmentId);
  }

  public List<AvailableAppointmentDTO> getAvailableMedicalAppointments(String specialization, String location) {
    LocalDate date= LocalDate.now();
    specialization = specialization == null ? "" : specialization;
    location = location == null ? "" : location;

    return appointmentsService.getAvailableMedicalAppointments(date, specialization, location);
  }

  public ResponseEntity<?> reserveAppointment(AppointmentReservationRequestDto request) {
    if (validatePatientDateTimeCollisionAppointments(request)){
      return ResponseEntity.status(400).body("Patient already has an appointment at this time.");
    };

    BookingAppointmentEntity newBookingAppointment = appointmentsService.reserveAppointment(request);
    if (newBookingAppointment==null){
      return ResponseEntity.status(404).body(
          "The appointment is no longer available. The appointment has been reserved by another patient.");
    }
    if (newBookingAppointment.getBookingId() != null) {
      return ResponseEntity.ok("Booking appointment successfully");
    } else {
      return ResponseEntity.status(500).body("Sorry try later");
    }
  }

  private boolean validatePatientDateTimeCollisionAppointments(AppointmentReservationRequestDto request) {
    boolean isCollision=false;
    Optional<BookingAppointmentEntity> appointmentByDateAndPatient = appointmentsService.findAppointmentByDateAndPatient(request);
    if (appointmentByDateAndPatient.isPresent()){
      isCollision=true ;
    }
    return isCollision;
  }

  public List<DiagnosedDiseaseDTO> getPatientDiseases(Long patientId) {
    return diseaseRepository.findByPatientId(patientId).stream()
        .map(diagnosedDiseaseMapper::mapFromEntityToDto)
        .toList();
  }

  public void createPatient(PatientCreateRequest request, Long userId) throws Exception {

   if(patientDAO.findByUserIdOptional(userId).isPresent()){
     throw new Exception("Patient exist with this credentials.");
    }

    AddressEntity addressEntity = AddressEntity.builder()
        .houseNumber(request.getAddress().getHouseNumber())
        .city(request.getAddress().getCity())
        .street(request.getAddress().getStreet())
        .apartmentNumber(request.getAddress().getApartmentNumber())
        .country("Poland")  // tutaj zewnętrzna api @TODO zewnętrzne api do zip code
        .voivodeship("Mazowieckie")
        .postalCode("01-891")
        .build();

    UserEntity userReference = entityManager.getReference(UserEntity.class, userId);

    PatientEntity newPatientEntiy = PatientEntity.builder()
        .name(request.getName())
        .surname(request.getSurname())
        .pesel(request.getPesel())
        .birthdate(LocalDate.parse(request.getBirthdate()))
        .email(request.getEmail())
        .phone(request.getPhone())
        .medicalPackage(request.getMedicalPackage())
        .gender(request.getGender())
        .address(addressEntity)
        .appUser(userReference)
        .build();

      patientDAO.save(newPatientEntiy);

  }
}
