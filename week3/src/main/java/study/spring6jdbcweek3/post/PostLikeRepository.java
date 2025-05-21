package study.spring6jdbcweek3.post;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class PostLikeRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostLikeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(PostLike postLike) {
        String sql = "insert into post_like(post_id, user_id, liked_at) values (?, ?, ?)";
        jdbcTemplate.update(sql, postLike.getPostId(), postLike.getUserId(), Timestamp.valueOf(postLike.getLikedAt()));
    }

    public boolean existsByPostIdAndUserId(Long postId, Long userId) {
        String sql = "select count(*) from post_like where post_id = ? and user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId, userId);
        return count != null && count > 0;
    }

    public void deleteAll() {
        jdbcTemplate.update("delete from post_like");
    }
}