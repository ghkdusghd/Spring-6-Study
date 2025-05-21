package study.mircoblog.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.mircoblog.post.dto.PostDetailDto;
import study.mircoblog.post.dto.PostWithCommentCount;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public String list(Model model) {
        List<PostWithCommentCount> posts = postService.getPostsWithCommentCount();
        model.addAttribute("posts", posts);
        return "post_list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        PostDetailDto postDetailDto = postService.getPostDetail(id);
        model.addAttribute("postDetail", postDetailDto);
        return "post_detail";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("post", new Post()); // 폼 바인딩용
        return "post_form";
    }

    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam String content) {
        Post post = Post.create(title, content);
        postService.createPost(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = postService.getPostDetail(id).toPost();
        model.addAttribute("post", post);
        return "post_form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String content) {
        Post post = Post.of(id, title, content, null);
        postService.updatePost(post);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }
}
