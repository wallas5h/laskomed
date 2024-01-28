package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.MedicalAppointmentDTO;
import https.github.com.wallas5h.LaskoMed.domain.MedicalAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper {
  MedicalAppointmentDTO map(MedicalAppointment medicalAppointment);
}
