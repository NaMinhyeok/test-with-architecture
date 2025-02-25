package com.example.demo.user.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserCreateControllerTest {

    @Test
    void 사용자는_회원_가입을_할_수있고_회원가입된_사용자는_PENDING_상태이다() {
        // given
        TestContainer testContainer = TestContainer.builder()
            .uuidHolder(()->"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build();
        UserCreate userCreate = UserCreate.builder()
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.create(userCreate);
        // then
        then(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        then(result).extracting("body.id", "body.email", "body.nickname", "body.status", "body.lastLoginAt")
            .containsExactly(1L, "nmh9097@gmail.com", "nmh9097", UserStatus.PENDING, null);
        then(testContainer.userRepository.getById(1).getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }

}
