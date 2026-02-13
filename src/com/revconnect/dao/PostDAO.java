package com.revconnect.dao;

import com.revconnect.model.Post;
import com.revconnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    public void createPost(Post p) throws Exception {

        Connection con = DBConnection.getConnection();

        CallableStatement cs =
                con.prepareCall("{call REV_PKG.create_post(?,?)}");

        cs.setInt(1, p.getUserId());
        cs.setString(2, p.getContent());

        cs.execute();
        con.close();
    }


    public List<Post> getAllPosts() throws Exception {
        List<Post> posts = new ArrayList<>();

        String sql = "SELECT * FROM POSTS ORDER BY POST_ID DESC";
        Connection con = DBConnection.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            Post p = new Post();
            p.setPostId(rs.getInt("POST_ID"));
            p.setUserId(rs.getInt("USER_ID"));
            p.setContent(rs.getString("CONTENT"));
            posts.add(p);
        }
        con.close();
        return posts;
    }

    public int getPostOwner(int postId) throws Exception {

        String sql =
                "SELECT USER_ID FROM POSTS WHERE POST_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, postId);

        ResultSet rs = ps.executeQuery();
        int owner = -1;

        if (rs.next()) {
            owner = rs.getInt("USER_ID");
        }

        con.close();
        return owner;   // -1 means not found
    }

    public boolean updatePost(int postId, int userId, String content) throws Exception {

        String sql = "UPDATE POSTS SET CONTENT=? WHERE POST_ID=? AND USER_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, content);
        ps.setInt(2, postId);
        ps.setInt(3, userId);

        int rows = ps.executeUpdate();
        con.close();

        return rows > 0;
    }

    public boolean editPost(int postId, int userId, String newContent) throws Exception {

        String sql =
                "UPDATE POSTS SET CONTENT=? " +
                        "WHERE POST_ID=? AND USER_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, newContent);
        ps.setInt(2, postId);
        ps.setInt(3, userId);

        int rows = ps.executeUpdate();

        con.close();

        return rows > 0;   // true if updated
    }

    public boolean deletePost(int postId, int userId) throws Exception {

        String sql =
                "DELETE FROM POSTS WHERE POST_ID=? AND USER_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, postId);
        ps.setInt(2, userId);

        int rows = ps.executeUpdate();

        con.close();

        return rows > 0;
    }

    public List<Post> getPostsByUser(int userId) throws Exception {

        String sql = "SELECT * FROM POSTS WHERE USER_ID = ? ORDER BY POST_ID DESC";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        List<Post> list = new ArrayList<>();

        while (rs.next()) {

            Post p = new Post();

            p.setPostId(rs.getInt("POST_ID"));
            p.setUserId(rs.getInt("USER_ID"));
            p.setContent(rs.getString("CONTENT"));

            list.add(p);
        }

        con.close();

        return list;
    }



}
