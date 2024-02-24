package https.github.com.wallas5h.LaskoMed.infrastructure.database.repository.jpa;


import https.github.com.wallas5h.LaskoMed.infrastructure.database.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {

}
