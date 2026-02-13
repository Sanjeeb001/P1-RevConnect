package com.revconnect.dao;

import com.revconnect.model.User;
import com.revconnect.util.DBConnection;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;


public class UserDAO {

    public void register(User u) throws Exception {

        Connection con = DBConnection.getConnection();

        CallableStatement cs =
                con.prepareCall("{call REV_PKG.register_user(?,?,?,?,?,?)}");

        cs.setString(1, u.getName());
        cs.setString(2, u.getEmail());
        cs.setString(3, u.getPassword());
        cs.setString(4, u.getRole());
        cs.setString(5, u.getBio());
        cs.setString(6, u.getLocation());

        cs.execute();

        con.close();
    }


    public User login(String email, String password) throws Exception {
        String sql = "SELECT * FROM USERS WHERE EMAIL=? AND PASSWORD=?";
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, email);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            User u = new User();
            u.setUserId(rs.getInt("USER_ID"));
            u.setName(rs.getString("NAME"));
            u.setEmail(rs.getString("EMAIL"));
            u.setRole(rs.getString("ROLE"));
            u.setBio(rs.getString("BIO"));
            u.setLocation(rs.getString("LOCATION"));

            return u;
        }
        return null;
    }

    public String getNameById(int id) throws Exception {

        String sql = "SELECT NAME FROM USERS WHERE USER_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        String name = "Unknown";

        if (rs.next()) {
            name = rs.getString("NAME");
        }

        con.close();
        return name;
    }


    public boolean updateProfile(int id, String bio, String loc) throws Exception {

        String sql = "UPDATE USERS SET BIO=?, LOCATION=? WHERE USER_ID=?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, bio);
        ps.setString(2, loc);
        ps.setInt(3, id);

        int rows = ps.executeUpdate();
        con.close();

        return rows > 0;
    }

    public List<User> searchByName(String name) throws Exception {

        String sql =
                "SELECT * FROM USERS WHERE LOWER(NAME) LIKE LOWER(?)";

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + name + "%");

        ResultSet rs = ps.executeQuery();

        List<User> list = new ArrayList<>();

        while (rs.next()) {
            User u = new User();
            u.setUserId(rs.getInt("USER_ID"));
            u.setName(rs.getString("NAME"));
            list.add(u);
        }

        con.close();
        return list;
    }


    public User getUserById(int id) throws Exception {

        String sql = "SELECT * FROM USERS WHERE USER_ID=?";
        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        User u = null;

        if (rs.next()) {
            u = new User();
            u.setUserId(rs.getInt("USER_ID"));
            u.setName(rs.getString("NAME"));
            u.setBio(rs.getString("BIO"));
            u.setLocation(rs.getString("LOCATION"));
        }

        con.close();
        return u;
    }

    public List<User> getAllUsers() throws Exception {

        String sql = "SELECT USER_ID, NAME FROM USERS";

        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        List<User> list = new ArrayList<>();

        while (rs.next()) {
            User u = new User();
            u.setUserId(rs.getInt("USER_ID"));
            u.setName(rs.getString("NAME"));
            list.add(u);
        }

        con.close();
        return list;
    }


}
