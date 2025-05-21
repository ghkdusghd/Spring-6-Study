package study.mircoblog.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String createComment(@PathVariable Long postId,
                                @RequestParam String content) {
        Comment comment = Comment.create(postId, content);
        commentService.createComment(comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
