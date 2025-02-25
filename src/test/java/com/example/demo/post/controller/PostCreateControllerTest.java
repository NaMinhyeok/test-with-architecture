package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostCreateControllerTest {
    @Test
    void 사용자는_게시물을_작성할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
            .clockHolder(() -> 1679530673958L)
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

        PostCreate postCreate = PostCreate.builder()
            .writerId(1)
            .content("helloworld")
            .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);
        // then
        then(result)
            .extracting("statusCode","body.content","body.writer.nickname","body.createdAt")
            .containsExactly(HttpStatus.CREATED,"helloworld","nmh9097",1679530673958L);
    }
}
