package study.spring6jdbcweek3.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostLikeSyncTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @BeforeEach
    void init() {
        postRepository.deleteAll();
        postLikeRepository.deleteAll();
        // 초기 게시글 생성
        postRepository.save(Post.create(10L, "title", "content"));
    }

    @AfterEach
    void clear() {
        postRepository.deleteAll();
        postLikeRepository.deleteAll();
    }

    @DisplayName("게시글 좋아요 동시성 테스트")
    @Test
    void unLikeSync() throws Exception {
        // given
        int threadCount = 400;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    postLikeService.likePost(10L, userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Post post = postRepository.findById(10L);
        assertThat(post.getLikes()).isEqualTo(400);
    }
}
