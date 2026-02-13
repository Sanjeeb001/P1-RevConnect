package com.revconnect.dao;

import com.revconnect.model.Notification;
import com.revconnect.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public void addNotification(int userId, String message) throws Exception {
        String sql = "INSERT INTO NOTIFICATIONS VALUES (NOTIFICATION_SEQ.NEXTVAL, ?, ?, 'N')";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, userId);
        ps.setString(2, message);

        ps.executeUpdate();
        con.close();
    }


    public List<Notification> getNotifications(int userId) throws Exception {
        List<Notification> list = new ArrayList<>();

        String sql = "SELECT * FROM NOTIFICATIONS WHERE USER_ID=? ORDER BY NOTIFICATION_ID DESC";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Notification n = new Notification();
            n.setNotificationId(rs.getInt("NOTIFICATION_ID"));
            n.setUserId(rs.getInt("USER_ID"));
            n.setMessage(rs.getString("MESSAGE"));
            n.setIsRead(rs.getString("IS_READ"));
            list.add(n);
        }

        con.close();
        return list;
    }
}
