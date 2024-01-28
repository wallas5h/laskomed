package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.ClinicDTO;
import https.github.com.wallas5h.LaskoMed.domain.Clinic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
  ClinicDTO map(Clinic clinic);
}
