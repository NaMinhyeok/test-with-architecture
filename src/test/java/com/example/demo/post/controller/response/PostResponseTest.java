package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostResponseTest {

    @Test
    void Post로_응답을_생성_할_수_있다() {
        // given
        Post post = Post.builder()
            .content("컨텐츠 내용")
            .writer(User.builder()
                .email("nmh9097@gmail.com")
                .nickname("nmh9097")
                .address("경기도 이천시")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build())
            .build();
        // when
        PostResponse postResponse = PostResponse.from(post);
        // then
        BDDAssertions.then(postResponse)
            .extracting("content","writer.email","writer.nickname","writer.status")
            .containsExactlyInAnyOrder("컨텐츠 내용","nmh9097@gmail.com","nmh9097",UserStatus.ACTIVE);

    }

}