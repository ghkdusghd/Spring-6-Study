package study.spring6jdbcweek3.jdbcTemplateTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import study.spring6jdbcweek3.post.Post;
import study.spring6jdbcweek3.post.PostRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class NamedParameterJdbcTemplateTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @AfterEach
    void clear() {
        postRepository.deleteAll();
    }

    @DisplayName("한 레코드의 한 컬럼 가져오기")
    @Test
    void queryForObject() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // 파라미터 설정
        MapSqlParameterSource params = new MapSqlParameterSource("postId", 1L);

        // SQL 실행
        String title = namedParameterJdbcTemplate.queryForObject(
                "SELECT title FROM post WHERE post_id = :postId",
                params,
                String.class
        );

        // then
        Assertions.assertThat(title).isEqualTo("제목1");
    }

    @DisplayName("한 레코드를 Map 객체로 변환해서 가져오기")
    @Test
    void queryForMap() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // 파라미터 설정
        Map<String, Object> params = Collections.singletonMap("postId", 1L);

        // SQL 실행
        Map<String, Object> map = namedParameterJdbcTemplate.queryForMap(
                "SELECT * FROM post WHERE post_id = :postId",
                params
        );

        // then
        System.out.println(map);
    }

    @DisplayName("레코드를 Entity 객체로 변환해서 가져오기")
    @Test
    void queryForEntity() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // 파라미터 설정
        Map<String, Object> params = Collections.singletonMap("postId", 1L);

        // SQL 실행
        Post savedPost = namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM post WHERE post_id = :postId",
                params,
                new DataClassRowMapper<>(Post.class)
        );

        // then
        assertEquals(savedPost, post);
    }

    @DisplayName("여러 레코드를 Entity 객체로 변환해서 가져오기")
    @Test
    void queryForEntityList() throws Exception {
        // given
        Post post1 = Post.create(1L, "제목1", "내용1");
        Post post2 = Post.create(2L, "제목2", "내용2");
        Post post3 = Post.create(3L, "제목3", "내용3");
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        // SQL 실행
        List<Post> posts = namedParameterJdbcTemplate.query(
                "SELECT * FROM post",
                new DataClassRowMapper<>(Post.class)
        );

        // then
        assertEquals(posts.size(), 3);
        assertEquals(posts, List.of(post1, post2, post3));
    }

    @DisplayName("insert 쿼리 실행")
    @Test
    void insert() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");

        // 파라미터 설정
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);

        // SQL 실행
        namedParameterJdbcTemplate.update(
                "INSERT INTO post(post_id, title, content, likes) VALUES(:postId, :title, :content, :likes)",
                params
        );

        // when
        Post savedPost = namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM post WHERE post_id = :postId",
                Collections.singletonMap("postId", post.getPostId()),
                new DataClassRowMapper<>(Post.class)
        );

        // then
        assertEquals(savedPost, post);
    }

    @DisplayName("update 쿼리 실행")
    @Test
    void update() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // 파라미터 설정
        Map<String, Object> params = new HashMap<>();
        params.put("title", "제목2");
        params.put("content", "내용2");
        params.put("postId", 1L);

        // SQL 실행
        namedParameterJdbcTemplate.update(
                "UPDATE post SET title = :title, content = :content WHERE post_id = :postId",
                params
        );

        // then
        Post updatedPost = namedParameterJdbcTemplate.queryForObject(
                "SELECT * FROM post WHERE post_id = :postId",
                Collections.singletonMap("postId", 1L),
                new DataClassRowMapper<>(Post.class)
        );
        assertEquals(updatedPost.getTitle(), "제목2");
        assertEquals(updatedPost.getContent(), "내용2");
    }

    @DisplayName("delete 쿼리 실행")
    @Test
    void delete() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // 파라미터 설정
        Map<String, Object> params = Collections.singletonMap("postId", 1L);

        // SQL 실행
        namedParameterJdbcTemplate.update(
                "DELETE FROM post WHERE post_id = :postId",
                params
        );

        // then
        List<Post> posts = namedParameterJdbcTemplate.query(
                "SELECT * FROM post",
                new DataClassRowMapper<>(Post.class)
        );
        assertEquals(posts.size(), 0);
    }
}

