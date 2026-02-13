package com.revconnect.dao;

import com.revconnect.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LikeDAO {

    public boolean hasUserLiked(int postId, int userId) throws Exception {
        String sql = "SELECT COUNT(*) FROM LIKES WHERE POST_ID=? AND USER_ID=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, postId);
        ps.setInt(2, userId);

        ResultSet rs = ps.executeQuery();
        rs.next();
        boolean exists = rs.getInt(1) > 0;
        con.close();
        return exists;
    }

    public boolean likePost(int postId, int userId) throws Exception {

        String sql = "INSERT INTO LIKES VALUES (LIKE_SEQ.NEXTVAL, ?, ?)";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, postId);
        ps.setInt(2, userId);

        int rows = ps.executeUpdate();
        con.close();
        return rows > 0;
    }


    public int getLikeCount(int postId) throws Exception {
        String sql = "SELECT COUNT(*) FROM LIKES WHERE POST_ID=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, postId);

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        con.close();
        return count;
    }

    public boolean unlikePost(int postId, int userId) throws Exception {

        String sql =
                "DELETE FROM LIKES WHERE POST_ID=? AND USER_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, postId);
        ps.setInt(2, userId);

        int rows = ps.executeUpdate();

        con.close();

        return rows > 0;
    }
}
