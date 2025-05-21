package study.mircoblog.post;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import study.mircoblog.post.dto.PostWithCommentCount;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;

    // 게시글 저장
    public void save(Post post) {
        String sql = "insert into post (title, content, created_at) values (?, ?, ?)";
        jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getCreatedAt());
    }

    // 게시글 수정
    public void update(Post post) {
        String sql = "update post set title = ?, content = ? where id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getId());
    }

    // 게시글 단건 조회
    public Post findById(Long id) {
        String sql = "select * from post where id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Post.class), id);
    }

    // 모든 게시글 조회 (댓글 개수 Join)
    public List<PostWithCommentCount> findAllWithCommentCount() {
        String sql = """
            select p.*, count(c.id) as comment_count
              from post p
              left join comment c on p.id = c.post_id
              group by p.id
              order by p.id desc
            """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PostWithCommentCount.class));
    }

    // 게시글 삭제
    public void deleteById(Long id) {
        String sql = "delete from post where id = ?";
        jdbcTemplate.update(sql, id);
    }
}

