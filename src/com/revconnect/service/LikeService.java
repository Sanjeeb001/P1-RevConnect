package com.revconnect.service;

import com.revconnect.dao.LikeDAO;
import com.revconnect.dao.PostDAO;

public class LikeService {

    // CORRECT OBJECTS
    LikeDAO likeDao = new LikeDAO();
    PostDAO postDao = new PostDAO();

    public boolean likePost(int postId, int userId) throws Exception {

        // 1. Check post exists
        int owner = postDao.getPostOwner(postId);

        if (owner == -1) {
            return false;   // post not found
        }

        // 2. Like post
        return likeDao.likePost(postId, userId);
    }

    public int getLikeCount(int postId) throws Exception {
        return likeDao.getLikeCount(postId);
    }

    public boolean unlikePost(int postId, int userId) throws Exception {
        return likeDao.unlikePost(postId, userId);
    }
}
