package study.mircoblog.post.dto;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
public class PostWithCommentCount {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private int commentCount;

    private PostWithCommentCount(Long id, String title, String content, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
    }
}
