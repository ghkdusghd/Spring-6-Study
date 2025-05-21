package study.spring6jdbcweek3.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public PostLikeService(PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Transactional
    public void likePost(Long postId, Long userId) {
//        postRepository.incrementLikes(postId);
        Post post = postRepository.findById(postId);
        post.increaseLikes();
        postRepository.update(post);

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다.");
        }

        postLikeRepository.save(new PostLike(postId, userId, LocalDateTime.now()));
    }
}