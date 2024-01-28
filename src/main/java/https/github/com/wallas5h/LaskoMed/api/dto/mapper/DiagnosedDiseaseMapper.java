package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DiagnosedDiseaseDTO;
import https.github.com.wallas5h.LaskoMed.domain.DiagnosedDisease;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiagnosedDiseaseMapper {
  DiagnosedDiseaseDTO map(DiagnosedDisease diagnosedDisease);
}
