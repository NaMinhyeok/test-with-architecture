package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void PostCreate로_게시물을_만들_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
            .writerId(1L)
            .content("컨텐츠 내용")
            .build();
        User writer = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .status(UserStatus.ACTIVE)
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build();
        // when
        Post post = Post.from(writer, postCreate);
        // then
        then(post)
            .extracting("content","writer.email","writer.nickname","writer.address","writer.status","writer.certificationCode")
            .containsExactlyInAnyOrder("컨텐츠 내용","nmh9097@gmail.com","nmh9097","경기도 이천시",UserStatus.ACTIVE,"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }

}