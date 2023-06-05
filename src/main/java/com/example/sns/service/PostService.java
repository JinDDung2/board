package com.example.sns.service;

import com.example.sns.entity.Post;
import com.example.sns.entity.PostLike;
import com.example.sns.entity.User;
import com.example.sns.entity.dto.post.*;
import com.example.sns.event.AlarmEvent;
import com.example.sns.exception.SpringBootAppException;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.PostLikeRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.sns.entity.Alarm.AlarmType.NEW_LIKE_ON_POST;
import static com.example.sns.entity.Role.ADMIN;
import static com.example.sns.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher publisher;

    /**
     * 비지니스로직
     */
    public PostCreateResponseDto createPost(PostCreateRequestDto requestDto, String userName) {
        User user = findUser(userName);
        Post post = requestDto.toEntity();
        post.addUser(user);

        postRepository.save(post);
        return PostCreateResponseDto.from(post);
    }

    @Transactional(readOnly = true)
    public Page<PostReadResponseDto> findAllByPage(Pageable pageable) {
        Page<Post> pages = postRepository.findAll(pageable);
        return pages.map(PostReadResponseDto::from);
    }

    @Transactional(readOnly = true)
    public Page<PostMyFeedResponseDto> findMyFeed(String userName, Pageable pageable) {
        User user = findUser(userName);

        Page<Post> pages = postRepository.findMyFeedByUserId(user.getId(), pageable);

        return pages.map(PostMyFeedResponseDto::from);
    }

    @Transactional(readOnly = true)
    public PostReadResponseDto findById(Integer postId) {
        Post post = findPost(postId);
        return PostReadResponseDto.from(post);
    }

    public PostUpdateResponseDto update(PostUpdateRequestDto requestDto, Integer postId, String userName) {
        Post post = findPost(postId);
        User user = findUser(userName);

        if (!post.getUser().getUserName().equals(userName) && user.getRole() != ADMIN) {
            throw new SpringBootAppException(INVALID_PERMISSION, "사용자가 권한이 없습니다.");
        }

        post.update(requestDto.getTitle(), requestDto.getBody());
        postRepository.flush();
        return PostUpdateResponseDto.from(post);
    }

    public PostDeleteResponseDto deleteById(Integer postId, String userName) {
        Post post = findPost(postId);
        User user = findUser(userName);

        if (!post.getUser().getUserName().equals(userName) && user.getRole() != ADMIN) {
            throw new SpringBootAppException(INVALID_PERMISSION, "사용자가 권한이 없습니다.");
        }

        commentRepository.deleteAllByPost(postId);
        postLikeRepository.deleteAllByPost(postId);
        postRepository.deleteById(postId);
        return PostDeleteResponseDto.from(post);
    }

    public boolean isLike(Integer postId, String userName) {
        User user = findUser(userName);
        Post post = findPost(postId);

        PostLike postLike = postLikeRepository.findByPostAndUser(post, user).orElse(PostLike.from(post, user));
        boolean isLike = postLike.likes();
        try {
            postLikeRepository.save(postLike);

            if (isLike) {
                publisher.publishEvent(AlarmEvent.from(NEW_LIKE_ON_POST, post.getUser(), user));
            }
        } catch (ObjectOptimisticLockingFailureException e) {
            log.info("게시글 좋아요 동시성 이슈 발생");
            throw e;
        }

        return isLike;
    }

    /**
     * 중복메서드 정리
     */
    private Post findPost(Integer postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
            throw new SpringBootAppException(POST_NOT_FOUND, postId + " 해당 포스트가 없습니다.");
        });
    }
    private User findUser(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(() -> {
            throw new SpringBootAppException(USERNAME_NOT_FOUND, "UserName을 찾을 수 없습니다.");
        });
    }

}
