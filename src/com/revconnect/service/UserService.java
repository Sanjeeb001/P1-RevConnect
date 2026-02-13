package com.revconnect.service;
import java.util.List;

import com.revconnect.dao.UserDAO;
import com.revconnect.model.User;

public class UserService {

    UserDAO dao = new UserDAO();

    public void register(User user) throws Exception {
        dao.register(user);
    }

    public User login(String email, String password) throws Exception {
        return dao.login(email, password);
    }

    public String getNameById(int id) throws Exception {
        return dao.getNameById(id);
    }


    public boolean updateProfile(int id, String bio, String loc) throws Exception {
        return dao.updateProfile(id, bio, loc);
    }

    public User getUserById(int id) throws Exception {
        return dao.getUserById(id);
    }

    public List<User> searchByName(String name) throws Exception {
        return dao.searchByName(name);
    }

    public List<User> getAllUsers() throws Exception {
        return dao.getAllUsers();
    }


}
