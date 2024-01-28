package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.PrescriptionDTO;
import https.github.com.wallas5h.LaskoMed.domain.Prescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
  PrescriptionDTO map(Prescription prescription);
}
