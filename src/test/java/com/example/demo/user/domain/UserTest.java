package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @DisplayName("UserCreate 객체로 생성 할 수 있다")
    @Test
    void createUser() {
        // given
        UserCreate userCreate = UserCreate.builder()
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .build();
        // when
        User user = User.from(userCreate,new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
        // then
        BDDAssertions.then(user)
            .extracting("id","email","nickname","address","status","certificationCode")
            .containsExactlyInAnyOrder(null,"nmh9097@gmail.com","nmh9097","경기도 이천시",UserStatus.PENDING,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }

    @DisplayName("UserUpdate 객체로 데이터를 업데이트 할 수 있다")
    @Test
    void updateUser() {
        // given
        User user = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .status(UserStatus.ACTIVE)
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build();

        UserUpdate userUpdate = UserUpdate.builder()
            .nickname("민혁")
            .address("서울시")
            .build();
        // when
        user = user.update(userUpdate);
        // then
        BDDAssertions.then(user)
            .extracting("id","email","nickname","address","status","certificationCode")
            .containsExactlyInAnyOrder(1L,"nmh9097@gmail.com","민혁","서울시",UserStatus.ACTIVE,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");


    }

    @DisplayName("로그인 할 수 있고 로그인시 마지막 로그인 시간이 변경된다")
    @Test
    void loginUser() {
        // given
        User user = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .status(UserStatus.ACTIVE)
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build();
        // when
        user = user.login(new TestClockHolder(1678530673958L));
        // then
        BDDAssertions.then(user.getLastLoginAt())
            .isEqualTo(1678530673958L);
    }

    @DisplayName("인증 코드로 계정을 활성화 할 수 있다")
    @Test
    void certificateUserAccount() {
        // given
        User user = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .status(UserStatus.PENDING)
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build();
        // when
        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        // then
        BDDAssertions.then(user.getStatus())
            .isEqualTo(UserStatus.ACTIVE);

    }

    @DisplayName("잘못된 인증 코드로 계정을 활성화 하면 예외를 던진다")
    @Test
    void throwExceptionWhenCertificateInvalidCodeUserAccount() {
        // given
        User user = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .status(UserStatus.PENDING)
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build();
        // when
        // then
        BDDAssertions.thenThrownBy(() -> user.certificate("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
            .isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}