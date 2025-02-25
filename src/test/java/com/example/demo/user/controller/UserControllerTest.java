package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_정보를_개인정보는_소거된채_전달_받을_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .status(UserStatus.ACTIVE)
            .address("경기도 이천시")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .lastLoginAt(100L)
            .build());
        // when
        ResponseEntity<UserResponse> result = testContainer.userController.getById(1);
        // then
        then(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(result).extracting("body.id", "body.email", "body.nickname", "body.status", "body.lastLoginAt")
            .containsExactly(1L, "nmh9097@gmail.com", "nmh9097", UserStatus.ACTIVE, 100L);
    }

    @Test
    void 사용자는_존재하지_않는_유저의_아이디로_api_호출할_경우_404_응답을_받는다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
            .build();
        // when
        // then
        thenThrownBy(() -> testContainer.userController.getById(1)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .status(UserStatus.ACTIVE)
            .address("경기도 이천시")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .lastLoginAt(100L)
            .build());
        // when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        // then
        then(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        then(testContainer.userRepository.getById(1).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() {
        // given
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .status(UserStatus.ACTIVE)
            .address("경기도 이천시")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
            .lastLoginAt(100L)
            .build());
        // when
        // then
        thenThrownBy(() -> testContainer.userController.verifyEmail(1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(() -> 1678530673958L)
            .build();
        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .status(UserStatus.ACTIVE)
            .address("경기도 이천시")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .lastLoginAt(100L)
            .build());
        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("nmh9097@gmail.com");
        // then
        then(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(result).extracting("body.id", "body.email", "body.nickname", "body.status", "body.lastLoginAt", "body.address")
            .containsExactly(1L, "nmh9097@gmail.com", "nmh9097", UserStatus.ACTIVE, 1678530673958L, "경기도 이천시");
    }

    @Test
    void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
            .build();
        testContainer.userRepository.save(User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .status(UserStatus.ACTIVE)
            .address("경기도 이천시")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .lastLoginAt(100L)
            .build());
        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("nmh9097@gmail.com", UserUpdate.builder()
                .address("서울시 강남구")
                .nickname("나민혁")
            .build());
        // then
        then(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        then(result).extracting("body.id", "body.email", "body.nickname", "body.status", "body.lastLoginAt", "body.address")
            .containsExactly(1L, "nmh9097@gmail.com", "나민혁", UserStatus.ACTIVE, 100L, "서울시 강남구");
    }

}
