package study.spring6jdbcweek3.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.spring6jdbcweek3.log.LogRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostLikeLogServiceTest {
    @Autowired
    private PostLikeLogService postLikeLogService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private LogRepository logRepository;

    @BeforeEach
    void before() {
        postRepository.deleteAll();
        postLikeRepository.deleteAll();
        logRepository.deleteAll();
        postRepository.save(Post.create(10L, "title", "content"));
    }

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        postLikeRepository.deleteAll();
        logRepository.deleteAll();

    }

    @DisplayName("Post 좋아요 로그 테스트")
    @Test
    void postLikeLog() throws Exception {
        assertThrows(RuntimeException.class, () -> postLikeLogService.likePost(10L, 1L));
        assertEquals(1, logRepository.findAll().size());
    }
}