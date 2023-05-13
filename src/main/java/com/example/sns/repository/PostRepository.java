package com.example.sns.repository;

import com.example.sns.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findMyFeedByUserId(Integer userId, Pageable pageable);

    @Lock(LockModeType.OPTIMISTIC)
    void save(Integer id);

    @Lock(LockModeType.OPTIMISTIC)
    Post save(Post post);

    @Modifying(clearAutomatically = true)
    @Query("update Post p set p.deletedDate = current_timestamp where p.id = :postId")
    void deleteById(@Param("postId") Integer postId);

}
