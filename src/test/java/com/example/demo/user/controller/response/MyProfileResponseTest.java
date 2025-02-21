package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyProfileResponseTest {

    @Test
    void User로_응답을_생성_할_수_있다() {
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
        MyProfileResponse response = MyProfileResponse.from(user);
        // then
        BDDAssertions.then(response)
            .extracting("id","email","nickname","address","status")
            .containsExactlyInAnyOrder(1L,"nmh9097@gmail.com","nmh9097","경기도 이천시",UserStatus.ACTIVE);

    }

}