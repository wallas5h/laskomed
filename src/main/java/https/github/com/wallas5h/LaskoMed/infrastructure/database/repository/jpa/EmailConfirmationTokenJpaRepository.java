package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;


import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.EmailConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailConfirmationTokenJpaRepository extends JpaRepository<EmailConfirmationTokenEntity, Long> {

  @Query("""
      SELECT ec FROM EmailConfirmationTokenEntity ec
      WHERE ec.token = :token
      """)
  Optional<EmailConfirmationTokenEntity> findByToken(@Param("token") String token);
}
