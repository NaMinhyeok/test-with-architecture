package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


public class PostControllerTest {

    @Test
    void 사용자는_게시물을_단건_조회_할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
            .build();
        User user = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .status(UserStatus.ACTIVE)
            .address("경기도 이천시")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .lastLoginAt(100L)
            .build();
        Post post = Post.builder()
            .id(1L)
            .content("hello world")
            .writer(user)
            .createdAt(100L)
            .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(post);
        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getById(1L);
        // then
        then(result)
            .extracting("statusCode","body.content","body.writer.nickname","body.createdAt")
            .containsExactly(HttpStatus.OK,"hello world","nmh9097",100L);
    }

    @Test
    void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
            .build();
        // when
        // then
        thenThrownBy(() -> testContainer.postController.getById(1L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 사용자는_게시물을_수정할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(() -> 200L)
            .build();
        User user = User.builder()
            .id(1L)
            .email("nmh9097@gmail.com")
            .nickname("nmh9097")
            .status(UserStatus.ACTIVE)
            .address("경기도 이천시")
            .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
            .lastLoginAt(100L)
            .build();
        Post post = Post.builder()
            .id(1L)
            .content("hello world")
            .writer(user)
            .createdAt(100L)
            .build();
        testContainer.userRepository.save(user);
        testContainer.postRepository.save(post);
        // when
        ResponseEntity<PostResponse> result = testContainer.postController.update(1L,PostUpdate.builder()
            .content("업데이트 컨텐츠")
            .build());
        // then
        then(result)
            .extracting("statusCode","body.content","body.writer.nickname","body.createdAt","body.modifiedAt")
            .containsExactly(HttpStatus.OK,"업데이트 컨텐츠","nmh9097",100L,200L);
    }
}
