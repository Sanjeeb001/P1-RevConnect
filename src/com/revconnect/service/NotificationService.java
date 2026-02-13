package com.revconnect.service;

import com.revconnect.dao.NotificationDAO;
import com.revconnect.model.Notification;

import java.util.List;

public class NotificationService {

    NotificationDAO dao = new NotificationDAO();

    public void notifyUser(int userId, String message) throws Exception {
        dao.addNotification(userId, message);
    }

    public List<Notification> getNotifications(int userId) throws Exception {
        return dao.getNotifications(userId);
    }
}