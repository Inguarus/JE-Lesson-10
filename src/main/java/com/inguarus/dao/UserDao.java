package com.inguarus.dao;

import com.inguarus.model.User;
import com.inguarus.model.UserList;

import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static UserDao instance;

    private List<User> users = new ArrayList<>();

    public static synchronized UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    private UserDao() {
    }

    public void addUser(User user) {
        users.add(user);
    }

    public UserList getUsers() {
        return new UserList(users);
    }

    public boolean remove(String name) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(name)) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }

    public boolean update(User testUser) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(testUser.getFirstName())) {
                user.setAge(testUser.getAge());
                return true;
            }
        }
        return false;
    }
}
