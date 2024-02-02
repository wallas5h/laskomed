package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.PatientDTO;
import https.github.com.wallas5h.LaskoMed.domain.Patient;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
  PatientDTO map(Patient patient);

  @Mapping(target = "prescriptions", ignore = true )
  @Mapping(target = "referrals", ignore = true )
  @Mapping(target = "diagnosedDiseases", ignore = true )
  PatientDTO mapFromEntityToDto(PatientEntity patient);

  @Mapping(target = "prescriptions", ignore = true )
  @Mapping(target = "referrals", ignore = true )
  @Mapping(target = "diagnosedDiseases", ignore = true )
  Patient mapFromEntity(PatientEntity entity);

  PatientEntity mapToEntity(Patient object);
}
