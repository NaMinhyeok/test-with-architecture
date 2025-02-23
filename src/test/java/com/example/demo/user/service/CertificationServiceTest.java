package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class CertificationServiceTest {

    @DisplayName("이메일과 컨텐츠가 제대로 만들어져서 보내는지 확인한다")
    @Test
    void verifyEmailAndContentsIsValid() {
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        //when
        certificationService.send("nmh9097@gmail.com", 1, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
        //then
        then(fakeMailSender)
            .extracting("email", "title", "content")
            .containsExactly(
                "nmh9097@gmail.com",
                "Please certify your email address",
                "Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"
            );
    }

}