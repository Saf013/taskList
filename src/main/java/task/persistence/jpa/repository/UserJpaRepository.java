package task.persistence.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import task.persistence.entity.UserEntity;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String name);

    @Query(value = "select username, password from users where username = :user and password = :pwd", nativeQuery = true)
    Optional<UserEntity> findByLoginAndPassword(@Param("user") String name, @Param("pwd") String password);
}
