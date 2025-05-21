package study.spring6jdbcweek3.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeProgrammaticService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PlatformTransactionManager transactionManager;

    /**
     * 여러 게시글에 좋아요를 누르는 메서드
     * @param partialAllowed true이면 일부 게시글 실패 시 롤백하지 않고 나머지 성공 허용
     */
    public void likeMultiplePosts(List<Long> postIds, Long userId, boolean partialAllowed) {
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);

        if (!partialAllowed) {
            txTemplate.executeWithoutResult(status -> {
                for (Long postId : postIds) {
                    // 좋아요 로직에서 예외가 발생하면 트랜잭션 전체 롤백
                    likeSinglePostOrThrow(postId, userId);
                }
            });
        }

        // partialAllowed == true인 경우 -> 부분 성공 허용
        // "각 게시글마다 별도의 트랜잭션"으로 처리 (중첩 트랜잭션 or 반복 트랜잭션)
        for (Long postId : postIds) {
            try {
                txTemplate.executeWithoutResult(status -> {
                    likeSinglePostOrThrow(postId, userId);
                });
            } catch (Exception e) {
                // 특정 게시글이 실패해도 로그만 남기고 넘어감
                log.info("게시글 {} 좋아요 실패: {}", postId, e.getMessage());
            }
        }
    }

    /**
     * 단일 게시글에 좋아요 수행하고, 중복 좋아요면 예외 발생
     * (중복 좋아요 시도 등으로 예외가 발생해도 그대로 throw해서 롤백 유도)
     */
    private void likeSinglePostOrThrow(Long postId, Long userId) {
        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시글입니다. (postId=" + postId + ")");
        }
        postRepository.incrementLikes(postId);
        postLikeRepository.save(new PostLike(postId, userId, LocalDateTime.now()));
    }
}
