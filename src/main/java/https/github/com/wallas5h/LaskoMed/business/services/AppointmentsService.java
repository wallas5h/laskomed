package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.BookingAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.api.dto.MedicalAppointmentRequestDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.BookingAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.utils.EnumsContainer;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.*;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.BookingAppointmentRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.CreatedAppointmentRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.MedicalAppointmentRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentsService {
  private CreatedAppointmentRepository createdAppointmentRepository;
  private BookingAppointmentRepository bookingAppointmentRepository;
  private MedicalAppointmentRepository medicalAppointmentRepository;
  private EntityManager entityManager;

  private BookingAppointmentMapper bookingAppointmentMapper;

  public List<BookingAppointmentDTO> getDoctorUpcomingAppointments(Long doctorId) {
    return bookingAppointmentRepository.findByDoctorId(doctorId).stream()
        .map(a-> bookingAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }

  @Transactional
  public void createAppointmentsFromDoctorAvailabilities(DoctorService.CreatedAppointmentDTO2 request){
    LocalDate dateAvailable= request.dateAvailable();
    LocalTime startTime= request.startTime();
    LocalTime endTime= request.endTime();

    DoctorEntity doctor = entityManager.getReference(
        DoctorEntity.class, request.doctorId());
    ClinicEntity clinic = entityManager.getReference(
        ClinicEntity.class, request.clincId());

    Duration appointmentDuration = Duration.ofMinutes(20);
    LocalTime currentTime = startTime;

    while (currentTime.plus(appointmentDuration).isBefore(endTime) || currentTime.plus(appointmentDuration).equals(endTime)) {

      CreatedAppointmentEntity newCreatedAppointment = CreatedAppointmentEntity.builder()
          .doctor(doctor)
          .clinic(clinic)
          .dateAvailable(dateAvailable)
          .startTime(currentTime)
          .endTime(currentTime.plus(appointmentDuration))
          .build();

      createdAppointmentRepository.save(newCreatedAppointment);

      currentTime = currentTime.plus(appointmentDuration);
    }
  }

  @Transactional
  public MedicalAppointmentEntity addMedicalAppointment(MedicalAppointmentRequestDTO request) {

    MapResult result = mapRequestDtoToEntity(request);
    return medicalAppointmentRepository.save(result.newMedicalAppointmentEntity());
  }
  @Transactional
  public int updateMedicalAppointment(MedicalAppointmentRequestDTO request) {

    MapResult result = mapRequestDtoToEntity(request);

    int rowUpdated = medicalAppointmentRepository
        .updateByBookingId(result.bookingAppointment().getBookingId(), result.newMedicalAppointmentEntity());
    entityManager.flush();
    return rowUpdated;
  }

  private MapResult mapRequestDtoToEntity(MedicalAppointmentRequestDTO request) {
    PatientEntity patient = entityManager.find(
        PatientEntity.class, request.getPatientId());
    DoctorEntity doctor = entityManager.find(
        DoctorEntity.class, request.getDoctorId());
    ClinicEntity clinic = entityManager.find(
        ClinicEntity.class, request.getClinicId());
    BookingAppointmentEntity bookingAppointment = entityManager.find(
        BookingAppointmentEntity.class, request.getBookingAppointmentId());


    MedicalAppointmentEntity newMedicalAppointmentEntity = MedicalAppointmentEntity.builder()
        .patient(patient)
        .doctor(doctor)
        .clinic(clinic)
        .diagnosis(request.getDiagnosis())
        .appointmentStatus(request.getAppointmentStatus())
        .prescription(request.getPrescription())
        .bookingAppointment(bookingAppointment)
        .doctorComment(request.getDoctorComment())
        .cost(getMedicalAppointmentCost(patient.getMedicalPackage(), bookingAppointment.getType(), request.getAppointmentStatus()))
        .build();
    MapResult result = new MapResult(bookingAppointment, newMedicalAppointmentEntity);
    return result;
  }

  private record MapResult(BookingAppointmentEntity bookingAppointment, MedicalAppointmentEntity newMedicalAppointmentEntity) {
  }

  @Transactional
  public Boolean isExistMedicalAppointment(MedicalAppointmentRequestDTO request) {
    BookingAppointmentEntity bookingAppointment = entityManager.find(
        BookingAppointmentEntity.class, request.getBookingAppointmentId());

    Optional<MedicalAppointmentEntity> medicalAppointmentByBookingId = medicalAppointmentRepository
        .findByBookingId(bookingAppointment.getBookingId());
    return medicalAppointmentByBookingId.isPresent();
  }
  public MedicalAppointmentEntity getMedicalAppointmentById(MedicalAppointmentRequestDTO request) {
    BookingAppointmentEntity bookingAppointment = entityManager.find(
        BookingAppointmentEntity.class, request.getBookingAppointmentId());

    Optional<MedicalAppointmentEntity> medicalAppointmentByBookingId = medicalAppointmentRepository
        .findByBookingId(bookingAppointment.getBookingId());
    return medicalAppointmentByBookingId.get();
  }


  private BigDecimal getMedicalAppointmentCost(String medicalPackage, String type, String status){

    if(medicalPackage.equalsIgnoreCase(EnumsContainer.MedicalPackage.PREMIUM.name())){
      return BigDecimal.ZERO;
    }

    if(status.equalsIgnoreCase(EnumsContainer.AppointmentStatus.MISSED.name())){
      return BigDecimal.valueOf(50);
    }
    try {
      EnumsContainer.AppointmentType appointmentType = EnumsContainer.AppointmentType.valueOf(type.toUpperCase());

      switch (appointmentType) {
        case ONLINE:
          return BigDecimal.valueOf(100);
        case TELECONSULTATION:
          return BigDecimal.valueOf(80);
        case FACILITY:
          return BigDecimal.valueOf(150);
        default:
          return BigDecimal.valueOf(100);
      }
    } catch (IllegalArgumentException e) {
      System.err.println("Nieprawidłowy typ wizyty: " + type);
      return BigDecimal.valueOf(100);
    }
  }
}
