package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.DoctorDTO;
import https.github.com.wallas5h.LaskoMed.domain.Doctor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
  DoctorDTO map(Doctor doctor);
}
