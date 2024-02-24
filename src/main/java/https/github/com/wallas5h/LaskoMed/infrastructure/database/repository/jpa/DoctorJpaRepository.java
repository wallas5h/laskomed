package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DoctorEntity;
import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorJpaRepository extends JpaRepository<DoctorEntity, Long> {

  Optional<DoctorEntity> findAllByDoctorId(Long doctorId);

  @Query("""
    SELECT dc FROM DoctorEntity dc
    WHERE dc.appUser.userId = :id
    """)
  Optional<DoctorEntity> findByUserId(@Param("id") Long userId);

}
