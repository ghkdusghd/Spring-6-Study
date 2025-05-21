package study.mircoblog.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.mircoblog.comment.Comment;
import study.mircoblog.comment.CommentRepository;
import study.mircoblog.post.dto.PostDetailDto;
import study.mircoblog.post.dto.PostWithCommentCount;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public List<PostWithCommentCount> getPostsWithCommentCount() {
        return postRepository.findAllWithCommentCount();
    }

    public PostDetailDto getPostDetail(Long id) {
        Post post = postRepository.findById(id);
        List<Comment> comments = commentRepository.findByPostId(id);
        return PostDetailDto.of(post, comments);
    }

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public void updatePost(Post post) {
        postRepository.update(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
