package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

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
        Post post = Post.from(writer, postCreate, new TestClockHolder(1679530673958L));
        // then
        then(post)
            .extracting("content", "writer.email", "writer.nickname", "writer.address", "writer.status", "writer.certificationCode", "createdAt")
            .containsExactlyInAnyOrder("컨텐츠 내용", "nmh9097@gmail.com", "nmh9097", "경기도 이천시", UserStatus.ACTIVE, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", 1679530673958L);

    }

    @Test
    void PostUpdate로_게시물을_수정_할_수_있다() {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
            .content("수정된 컨텐츠 내용")
            .build();
        User writer = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .address("경기도 이천시")
            .status(UserStatus.ACTIVE)
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .build();
        Post post = Post.builder()
            .id(1L)
            .content("컨텐츠 내용")
            .createdAt(1678530673958L)
            .modifiedAt(0L)
            .writer(writer)
            .build();
        // when
        post = post.update(postUpdate, new TestClockHolder(1679530673958L));
        // then
        then(post)
            .extracting("content", "modifiedAt")
            .containsExactlyInAnyOrder("수정된 컨텐츠 내용", 1679530673958L);
    }

}