package study.spring6jdbcweek3.jdbcTemplateTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import study.spring6jdbcweek3.post.Post;
import study.spring6jdbcweek3.post.PostRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JdbcTemplateTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

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

        /**
         * 두 번째 인수에는 반환 값으로 반활할 객체의 타입을 지정한다.
         * 세 번째 인수는 ?기호에 바인딩할 값이다.
         */
        String title = jdbcTemplate.queryForObject("select title from post where post_id = ?", String.class, 1l);

        // then
        Assertions.assertThat(title).isEqualTo("제목1");
    }

    @DisplayName("한 레코드를 Map 객체로 변환해서 가져오기")
    @Test
    void queryForMap() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // when
        Map<String, Object> map = jdbcTemplate.queryForMap("select * from post where post_id = ?", 1l);

        // then
        System.out.println(map);
    }

    @DisplayName("레코드를 Entity 객체로 변환해서 가져오기")
    @Test
    void queryFonEntity() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // when
        Post savedPost = jdbcTemplate.queryForObject("select * from post where post_id = ?"
                , new DataClassRowMapper<>(Post.class), 1l);

        // then
        assertEquals(savedPost, post);
    }

    /**
     * INSERT 문을 실행할 때는 update 메서드를 사용한다. SQL 문은 INSERT 지만 메서드 이름은 update이므로 주의.
     * 이어서 UPDATE, DELETE 문을 실행할 때도 update 메서드를 사용한다.
     */
    @DisplayName("insert 쿼리 실행")
    @Test
    void insert() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        jdbcTemplate.update("insert into post(post_id, title, content, likes) values(?, ?, ?, ?)",
                post.getPostId(), post.getTitle(), post.getContent(), post.getLikes());

        // when
        Post savedPost = jdbcTemplate.queryForObject("select * from post", new DataClassRowMapper<>(Post.class));

        // then
        assertEquals(savedPost, post);
    }

    @DisplayName("update 쿼리 실행")
    @Test
    void update() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // when
        jdbcTemplate.update("update post set title = ?, content = ? where post_id = ?", "제목2", "내용2", 1L);

        // then
        Post updatedPost = jdbcTemplate.queryForObject("select * from post where post_id = ?", new DataClassRowMapper<>(Post.class), 1L);
        assertEquals(updatedPost.getTitle(), "제목2");
        assertEquals(updatedPost.getContent(), "내용2");
    }

    @DisplayName("delete 쿼리 실행")
    @Test
    void delete() throws Exception {
        // given
        Post post = Post.create(1L, "제목1", "내용1");
        postRepository.save(post);

        // when
        jdbcTemplate.update("delete from post where post_id = ?", 1L);

        // then
        List<Post> posts = jdbcTemplate.query("select * from post", new DataClassRowMapper<>(Post.class));
        assertEquals(posts.size(), 0);
    }
}
