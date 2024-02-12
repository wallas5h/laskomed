package https.github.com.wallas5h.LaskoMed.business.services;

import https.github.com.wallas5h.LaskoMed.api.dto.BookingAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.BookingAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ClinicEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.CreatedAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.BookingAppointmentRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.CreatedAppointmentRepository;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AppointmentsService {
  private CreatedAppointmentRepository createdAppointmentRepository;
  private BookingAppointmentRepository bookingAppointmentRepository;
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
}
