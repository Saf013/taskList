package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import task.persistence.entity.UserEntity;

import java.util.Optional;

@Component
public class DbClient {

    @Autowired
    private TransactionalUserEntity transactionalUser;


    public void create(UserEntity user) {
        transactionalUser.create(user);
    }

    public Optional<UserEntity> findByLogin(String login) {
        return transactionalUser.findByLogin(login);
    }

    public Optional<UserEntity> findByLoginAndPassword(String login, String pwd) {
        return transactionalUser.findByLoginAndPassword(login, pwd);
    }

}
