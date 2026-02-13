package com.revconnect.dao;

import com.revconnect.model.Comment;
import com.revconnect.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public void addComment(Comment comment) throws Exception {
        String sql = "INSERT INTO COMMENTS VALUES (COMMENT_SEQ.NEXTVAL, ?, ?, ?)";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, comment.getPostId());
        ps.setInt(2, comment.getUserId());
        ps.setString(3, comment.getCommentText());

        ps.executeUpdate();
        con.close();
    }

    public List<Comment> getCommentsByPost(int postId) throws Exception {
        List<Comment> list = new ArrayList<>();

        String sql = "SELECT * FROM COMMENTS WHERE POST_ID=? ORDER BY COMMENT_ID";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, postId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Comment c = new Comment();
            c.setCommentId(rs.getInt("COMMENT_ID"));
            c.setPostId(rs.getInt("POST_ID"));
            c.setUserId(rs.getInt("USER_ID"));
            c.setCommentText(rs.getString("COMMENT_TEXT"));
            list.add(c);
        }
        con.close();
        return list;
    }

    public boolean deleteComment(int commentId, int userId) throws Exception {

        String sql = "DELETE FROM COMMENTS WHERE COMMENT_ID=? AND USER_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, commentId);
        ps.setInt(2, userId);

        int rows = ps.executeUpdate();
        con.close();

        return rows > 0;
    }


}