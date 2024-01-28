package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;

import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.DiagnosedDiseaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosesDiseaseJpaRepository extends JpaRepository<DiagnosedDiseaseEntity, Long> {

}
