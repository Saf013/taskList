package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import task.persistence.entity.UserEntity;
import task.persistence.jpa.repository.UserJpaRepository;

import java.util.Optional;

@Service
public class TransactionalUserEntity {

    @Autowired
    private UserJpaRepository jpaRepository;

    @Transactional
    public void create(UserEntity userEntity) {
        userEntity.setRole("ROLE_NEW_USER");
        userEntity.setPassword(new BCryptPasswordEncoder().encode(userEntity.getPassword()));
        jpaRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByLogin(String login) {
        return jpaRepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> findByLoginAndPassword(String login, String password) {
        return jpaRepository.findByLoginAndPassword(login, password);
    }
}
