package com.example.sns.fixture;

import com.example.sns.entity.Post;

public class PostInfoFixture {

    public static Post get(String userName, String password) {
        return Post.builder()
                .id(1)
                .title("title")
                .content("content")
                .user(UserInfoFixture.get(userName, password))
                .build();
    }
}
