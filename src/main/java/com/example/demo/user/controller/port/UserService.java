package com.example.demo.user.controller.port;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {


    User getByEmail(String email);

    User getById(long id);

    @Transactional
    User create(UserCreate userCreate);

    @Transactional
    User update(long id, UserUpdate userUpdate);

    @Transactional
    void login(long id);

    @Transactional
    void verifyEmail(long id, String certificationCode);
}
