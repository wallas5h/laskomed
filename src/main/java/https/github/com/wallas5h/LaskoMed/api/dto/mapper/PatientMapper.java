package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientDTO;
import https.github.com.wallas5h.LaskoMed.domain.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {
  PatientDTO map(Patient patient);
}
