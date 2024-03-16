package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientJpaRepository extends JpaRepository<PatientEntity, Long> {

  @Query("""
      SELECT pa FROM PatientEntity pa
      WHERE pa.appUser.userId = :id
      """)
  Optional<PatientEntity> findByUserId(@Param("id") Long userId);

  @Query("""
      SELECT pa FROM PatientEntity pa
      WHERE pa.pesel = :pesel
      """)
  Optional<PatientEntity> findByPesel(@Param("pesel") String pesel);
}
