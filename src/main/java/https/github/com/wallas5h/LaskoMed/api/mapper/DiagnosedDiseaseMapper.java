package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DiagnosedDiseaseDTO;
import https.github.com.wallas5h.LaskoMed.domain.DiagnosedDisease;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DiagnosedDiseaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiagnosedDiseaseMapper {
  DiagnosedDiseaseDTO map(DiagnosedDisease diagnosedDisease);

  @Mapping(target = "patient", ignore = true )
  DiagnosedDisease mapFromEntity(DiagnosedDiseaseEntity entity);

//  @Mapping(target = "patient", ignore = true )
//  DiagnosedDiseaseDTO mapFromEntityToDto(DiagnosedDiseaseEntity entity);

  DiagnosedDiseaseEntity mapToEntity(DiagnosedDisease object);
}
