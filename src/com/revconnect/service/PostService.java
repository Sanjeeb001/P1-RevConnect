package com.revconnect.service;

import com.revconnect.dao.PostDAO;
import com.revconnect.model.Post;

import java.util.List;

public class PostService {

    PostDAO dao = new PostDAO();

    public void createPost(Post post) throws Exception {
        dao.createPost(post);
    }

    public List<Post> getAllPosts() throws Exception {
        return dao.getAllPosts();
    }

    public boolean editPost(int postId, int userId, String content) throws Exception {
        return dao.updatePost(postId, userId, content);
    }

    public boolean deletePost(int postId, int userId) throws Exception {
        return dao.deletePost(postId, userId);
    }

    public boolean isPostExist(int id) throws Exception {
        return dao.getPostOwner(id) != -1;
    }

    public List<Post> getPostsByUser(int userId) throws Exception {
        return dao.getPostsByUser(userId);
    }


}
