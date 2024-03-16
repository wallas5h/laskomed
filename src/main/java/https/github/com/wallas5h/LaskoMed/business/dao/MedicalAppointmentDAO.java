package https.github.com.wallas5h.LaskoMed.business.dao;

import https.github.com.wallas5h.LaskoMed.api.dto.MedicalAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.MedicalAppointmentEntity;

import java.util.List;

public interface MedicalAppointmentDAO {
  List<MedicalAppointmentDTO> findByPatientId(Long patientId);

  List<MedicalAppointmentDTO> findByPatientIdAndSpecialization(Long patientId, String specialization);

  MedicalAppointmentEntity save(MedicalAppointmentEntity medicalAppointmentEntity);

  int updateByBookingId(Long bookingId, MedicalAppointmentEntity medicalAppointmentEntity);

  MedicalAppointmentDTO findByBookingId(Long bookingId);

  MedicalAppointmentDTO findById(Long appointmentId);
}
