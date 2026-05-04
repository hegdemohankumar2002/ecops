package com.dsu.ecops.service;

import com.dsu.ecops.model.*;
import com.dsu.ecops.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ForumService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private ForumReplyRepository forumReplyRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ForumPost> getAllPosts() {
        return forumPostRepository.findAllByOrderByCreatedAtDesc();
    }

    public ForumPost getPostById(Long id) {
        return forumPostRepository.findById(id).orElse(null);
    }

    @Transactional
    public ForumPost createPost(Long authorId, ForumPost post) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));
                
        if (author.getRole() != Role.POLICE_OFFICER && author.getRole() != Role.DETECTIVE) {
            throw new IllegalStateException("Only Officers and Detectives can post in the forum.");
        }

        post.setAuthor(author);
        return forumPostRepository.save(post);
    }

    @Transactional
    public ForumReply addReply(Long postId, Long authorId, String content) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
                
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author ID"));

        ForumReply reply = new ForumReply();
        reply.setPost(post);
        reply.setAuthor(author);
        reply.setContent(content);

        return forumReplyRepository.save(reply);
    }
}
