package study.spring6jdbcweek3.post;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class PostLike {
    private Long postId;
    private Long userId;
    private LocalDateTime likedAt;
}
