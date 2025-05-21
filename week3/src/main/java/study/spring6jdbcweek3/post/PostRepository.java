package study.spring6jdbcweek3.post;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void save(Post post) {
        String sql = "insert into post(post_id, title, content, likes) values(?, ?, ?, ?)";
        jdbcTemplate.update(sql, post.getPostId(), post.getTitle(), post.getContent(), post.getLikes());
    }

    public void incrementLikes(Long postId) {
        String sql = "update post set likes = likes + 1 where post_id = ?";
        jdbcTemplate.update(sql, postId);
    }

    public void update(Post post) {
        String sql = "update post set title = ?, content = ?, likes = ? where post_id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getContent(), post.getLikes(), post.getPostId());
    }

    public Post findById(Long id) {
        String sql = "select * from post where post_id = ?";
        return jdbcTemplate.queryForObject(sql, new DataClassRowMapper<>(Post.class), id);
    }

    public void deleteAll() {
        String sql = "delete from post";
        jdbcTemplate.update(sql);
    }
}