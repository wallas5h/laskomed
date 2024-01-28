package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorAvailabilityDTO;
import https.github.com.wallas5h.LaskoMed.domain.DoctorAvailability;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorAvailabilityMapper {
  DoctorAvailabilityDTO map(DoctorAvailability doctorAvailability);
}
