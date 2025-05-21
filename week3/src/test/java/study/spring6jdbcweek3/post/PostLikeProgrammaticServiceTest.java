package study.spring6jdbcweek3.post;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostLikeProgrammaticServiceTest {

    @Autowired
    PostLikeProgrammaticService postLikeProgrammaticService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostLikeRepository postLikeRepository;

    @BeforeEach
    void init() throws SQLException {
        // 3개의 게시글을 미리 생성
        postRepository.save(Post.create(1L, "Title1", "Content1"));
        postRepository.save(Post.create(2L, "Title2", "Content2"));
        postRepository.save(Post.create(3L, "Title3", "Content3"));
    }

    @AfterEach
    void clear() {
        postLikeRepository.deleteAll();
        postRepository.deleteAll();
    }

    @DisplayName("All-or-Nothing 모드에서 하나라도 실패 시 전체 롤백")
    @Test
    void likeMultiplePostsAllOrNothingFail() throws SQLException {
        // given
        List<Long> postIds = Arrays.asList(1L, 2L, 3L);
        Long userId = 100L;

        // 2번 게시글에 좋아요 넣어 중복 상황을 만들기
        postLikeRepository.save(new PostLike(2L, userId, LocalDateTime.now()));

        // when - then
        // 2번 게시글에서 예외 -> 전체 롤백
        assertThrows(IllegalStateException.class, () ->
                postLikeProgrammaticService.likeMultiplePosts(postIds, userId, false) // partialAllowed = false
        );

        // then: 1, 3번 게시글 좋아요도 실패해야 함 -> likes=0, post_like 테이블도 변화 없음
        assertEquals(0, postRepository.findById(1L).getLikes());
        assertEquals(0, postRepository.findById(2L).getLikes()); // 기존에 있는 likes만 반영 (지금은 0)
        assertEquals(0, postRepository.findById(3L).getLikes());

        // 2번 게시글 좋아요는 이미 있음
        assertTrue(postLikeRepository.existsByPostIdAndUserId(2L, userId));

        // 나머지 게시글 좋아요는 없음
        assertFalse(postLikeRepository.existsByPostIdAndUserId(1L, userId));
        assertFalse(postLikeRepository.existsByPostIdAndUserId(3L, userId));
    }

    @Test
    @DisplayName("PartialAllowed=true -> 실패한 게시글만 롤백, 나머지는 성공")
    void likeMultiplePostsPartialSuccess() throws SQLException {
        // given
        List<Long> postIds = Arrays.asList(1L, 2L, 3L);
        Long userId = 100L;

        // 이미 1번 게시글에 좋아요 -> 1번 게시글에서 예외 예상
        postLikeRepository.save(new PostLike(1L, userId, LocalDateTime.now()));

        // when
        postLikeProgrammaticService.likeMultiplePosts(postIds, userId, true); // partialAllowed = true

        // then
        // 1) 1번 게시글 -> 실패(중복), 롤백됨 (likes 변화 없음)
        assertEquals(0, postRepository.findById(1L).getLikes());

        // 2) 2번, 3번 게시글 -> 성공 (likes=1)
        assertEquals(1, postRepository.findById(2L).getLikes());
        assertEquals(1, postRepository.findById(3L).getLikes());

        // post_like -> 1번은 기존 1개 레코드만, 2번과 3번 추가됨
        assertTrue(postLikeRepository.existsByPostIdAndUserId(1L, userId));
        assertTrue(postLikeRepository.existsByPostIdAndUserId(2L, userId));
        assertTrue(postLikeRepository.existsByPostIdAndUserId(3L, userId));
    }
}
