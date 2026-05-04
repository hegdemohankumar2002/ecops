package com.dsu.ecops.repository;

import com.dsu.ecops.model.ForumReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumReplyRepository extends JpaRepository<ForumReply, Long> {
    List<ForumReply> findByPostIdOrderByCreatedAtAsc(Long postId);
}
