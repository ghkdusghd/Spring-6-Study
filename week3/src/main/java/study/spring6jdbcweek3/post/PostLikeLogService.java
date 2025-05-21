package study.spring6jdbcweek3.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.spring6jdbcweek3.log.Log;
import study.spring6jdbcweek3.log.LogRepository;

@Service
public class PostLikeLogService {
    private final PostLikeService postLikeService;
    private final LogRepository logRepository;

    public PostLikeLogService(PostLikeService postLikeService, LogRepository logRepository) {
        this.postLikeService = postLikeService;
        this.logRepository = logRepository;
    }

    @Transactional
    public void likePost(Long postId, Long userId) {
        postLikeService.likePost(postId, userId);
        logRepository.save(Log.create("Post liked. postId=" + postId + ", userId=" + userId));

        throw new RuntimeException("강제 예외 발생!");
    }
}
