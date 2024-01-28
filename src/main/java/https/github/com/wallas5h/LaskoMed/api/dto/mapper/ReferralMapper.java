package https.github.com.wallas5h.LaskoMed.api.dto.mapper;

import https.github.com.wallas5h.LaskoMed.api.dto.ReferralDTO;
import https.github.com.wallas5h.LaskoMed.domain.Referral;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReferralMapper {
  ReferralDTO map(Referral address);
}
