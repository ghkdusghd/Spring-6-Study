package study.mircoblog.post;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Post {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private Post(Long id, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Post create(String title, String content) {
        return new Post(null, title, content, LocalDateTime.now());
    }

    public static Post of(Long id, String title, String content, LocalDateTime createdAt) {
        return new Post(id, title, content, createdAt);
    }
}
