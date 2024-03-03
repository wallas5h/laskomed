package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository;

import https.github.com.wallas5h.LaskoMed.api.dto.MedicalAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.api.mapper.AvailableAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.BookingAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.api.mapper.MedicalAppointmentMapper;
import https.github.com.wallas5h.LaskoMed.business.dao.MedicalAppointmentDAO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.MedicalAppointmentEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.AvailableAppointmentJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.BookingAppointmentJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.DiagnosesDiseaseJpaRepository;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa.MedicalAppointmentJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class MedicalAppointmentRepository implements MedicalAppointmentDAO {
  private AvailableAppointmentJpaRepository availableAppointmentJpaRepository;
  private BookingAppointmentJpaRepository bookingAppointmentJpaRepository;
  private MedicalAppointmentJpaRepository medicalAppointmentJpaRepository;
  private DiagnosesDiseaseJpaRepository diagnosesDiseaseJpaRepository;
  private EntityManager entityManager;

  private BookingAppointmentMapper bookingAppointmentMapper;
  private MedicalAppointmentMapper medicalAppointmentMapper;
  private AvailableAppointmentMapper availableAppointmentMapper;

  @Override
  public List<MedicalAppointmentDTO> findByPatientId(Long patientId) {
    return medicalAppointmentJpaRepository.findByPatientId(patientId).stream()
        .map(medicalAppointmentMapper::mapFromEntityToDto)
        .toList();
  }

  @Override
  public List<MedicalAppointmentDTO> findByPatientIdAndSpecialization(Long patientId, String specialization) {
    return medicalAppointmentJpaRepository.findByPatientIdAndSpecialization(patientId, specialization).stream()
        .map(a -> medicalAppointmentMapper.mapFromEntityToDto(a))
        .toList();
  }

  @Override
  public MedicalAppointmentEntity save(MedicalAppointmentEntity medicalAppointmentEntity) {
    return medicalAppointmentJpaRepository.save(medicalAppointmentEntity);
  }

  @Override
  public int updateByBookingId(Long bookingId, MedicalAppointmentEntity medicalAppointmentEntity) {
    return medicalAppointmentJpaRepository.updateByBookingId(bookingId, medicalAppointmentEntity);
  }

  @Override
  public MedicalAppointmentDTO findByBookingId(Long bookingId) {
   return medicalAppointmentJpaRepository.findByBookingId(bookingId)
       .map( medicalAppointmentMapper::mapFromEntityToDto)
       .orElseThrow(() -> new EntityNotFoundException(
           "MedicalAppointmentEntity not found, bookingId: [%s]".formatted(bookingId)
       ));
  }

  @Override
  public MedicalAppointmentDTO findById(Long appointmentId) {
    return medicalAppointmentJpaRepository.findById(appointmentId)
        .map( medicalAppointmentMapper::mapFromEntityToDto)
        .orElseThrow(() -> new EntityNotFoundException(
            "MedicalAppointmentEntity not found, appointmentId: [%s]".formatted(appointmentId)
        ));
  }
}
