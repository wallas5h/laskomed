package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.ReferralEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReferralRepository extends JpaRepository<ReferralEntity, Long> {

}
