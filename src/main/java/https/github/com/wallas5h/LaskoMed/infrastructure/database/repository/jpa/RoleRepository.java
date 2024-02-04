package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;


import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
  RoleEntity findByName(String name);

}
