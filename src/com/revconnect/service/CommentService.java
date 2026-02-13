package com.revconnect.service;

import com.revconnect.dao.CommentDAO;
import com.revconnect.model.Comment;
import com.revconnect.dao.PostDAO;
import com.revconnect.service.NotificationService;

import java.util.List;

public class CommentService {

    CommentDAO dao = new CommentDAO();

    public void addComment(Comment comment) throws Exception {
        dao.addComment(comment);

        PostDAO postDAO = new PostDAO();
        int ownerId = postDAO.getPostOwner(comment.getPostId());

        if (ownerId != comment.getUserId()) {
            NotificationService ns = new NotificationService();
            ns.notifyUser(ownerId, "Someone commented on your post (Post ID: " + comment.getPostId() + ")");
        }
    }


    public List<Comment> getCommentsByPost(int postId) throws Exception {
        return dao.getCommentsByPost(postId);
    }


    public boolean deleteComment(int commentId, int userId) throws Exception {
        return dao.deleteComment(commentId, userId);
    }

}
