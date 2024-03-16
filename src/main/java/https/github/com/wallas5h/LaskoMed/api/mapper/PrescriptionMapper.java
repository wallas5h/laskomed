package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.PrescriptionDTO;
import https.github.com.wallas5h.LaskoMed.domain.Prescription;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PrescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
  PrescriptionDTO map(Prescription prescription);

  @Mapping(target = "patient", ignore = true)
  @Mapping(target = "clinic", ignore = true)
  @Mapping(target = "doctor", ignore = true)
  Prescription mapFromEntity(PrescriptionEntity entity);

  @Mapping(target = "patient", ignore = true)
  @Mapping(target = "clinic", ignore = true)
  @Mapping(target = "doctor", ignore = true)
  PrescriptionDTO mapFromEntityToDto(PrescriptionEntity entity);

  PrescriptionEntity mapToEntity(Prescription object);
}
