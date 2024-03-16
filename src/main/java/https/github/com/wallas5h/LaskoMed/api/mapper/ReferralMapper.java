package https.github.com.wallas5h.LaskoMed.api.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.ReferralDTO;
import https.github.com.wallas5h.LaskoMed.domain.Referral;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ReferralEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReferralMapper {
  ReferralDTO map(Referral address);

  @Mapping(target = "address", ignore = true)
  @Mapping(target = "doctor", ignore = true)
  @Mapping(target = "clinic", ignore = true)
  Referral mapFromEntity(ReferralEntity entity);

  @Mapping(target = "address", ignore = true)
  @Mapping(target = "doctor", ignore = true)
  @Mapping(target = "clinic", ignore = true)
  ReferralDTO mapFromEntityToDto(ReferralEntity entity);

  ReferralEntity mapToEntity(Referral object);
}
