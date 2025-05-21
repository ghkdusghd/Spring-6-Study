package study.spring6jdbcweek3.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostLikeServiceTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    PostLikeService postLikeService;

    @BeforeEach
    void init() throws SQLException {
        postRepository.save(Post.create(1L, "트랜잭션 테스트", "좋아요 기능 테스트"));
    }

    @AfterEach
    void clear() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
    }

    @DisplayName("게시글 좋아요 테스트")
    @Test
    void postLikeSave() throws SQLException {
        // given
        Long postId = 1L;
        Long userId = 100L;

        // when
        postLikeService.likePost(postId, userId);

        // then
        Post post = postRepository.findById(postId);
        assertEquals(1, post.getLikes());
        assertTrue(postLikeRepository.existsByPostIdAndUserId(postId, userId));
    }

    @DisplayName("게시글에 중복해서 좋아요를 할 수 없다")
    @Test
    void duplicatePostLikeSave() throws SQLException {
        // given
        Long postId = 1L;
        Long userId = 100L;

        postLikeService.likePost(postId, userId); // 첫 번째 좋아요 성공

        // when, then
        assertThrows(IllegalStateException.class, () -> {
            postLikeService.likePost(postId, userId); // 중복 좋아요 시도
        });

        // 롤백 확인: 좋아요 수는 증가하지 않음
        Post post = postRepository.findById(postId);
        assertEquals(1, post.getLikes());
    }
}
