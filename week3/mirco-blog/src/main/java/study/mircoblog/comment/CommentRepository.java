package study.mircoblog.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    // 댓글 저장
    public void save(Comment comment) {
        String sql = "insert into comment (post_id, content, created_at) values (?, ?, ?)";
        jdbcTemplate.update(sql,
                comment.getPostId(),
                comment.getContent(),
                comment.getCreatedAt());
    }

    // 특정 게시글의 모든 댓글 조회
    public List<Comment> findByPostId(Long postId) {
        String sql = "select * from comment where post_id = ? order by id desc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Comment.class), postId);
    }

    // 댓글 삭제
    public void deleteById(Long id) {
        String sql = "delete from comment where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
