package com.inguarus.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.inguarus.model.User;
import com.inguarus.model.UserList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static UserDao instance;
    private static List<User> users = new ArrayList<>();
    private static final String FILE_NAME = "all_operations.txt";
    private static final String PATH = System.getProperty("java.io.tmpdir");
    private static Gson gson = new GsonBuilder().create();

    static {
        File file = new File(PATH + File.separator + FILE_NAME);
        if (file.exists()) {
            StringBuilder gsonString = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                gsonString.append(reader.readLine());
                UserList userList = gson.fromJson(gsonString.toString(), UserList.class);
                users = userList.getUsers();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
        saveUsersToFile();

    }

    public UserList getUsers() {
        return new UserList(users);
    }

    public boolean remove(String firstName) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(firstName)) {
                users.remove(user);
                saveUsersToFile();
                return true;
            }
        }
        return false;
    }

    public boolean update(User testUser) {
        for (User user : users) {
            if (user.getFirstName().equalsIgnoreCase(testUser.getFirstName())) {
                user.setAge(testUser.getAge());
                saveUsersToFile();
                return true;
            }
        }
        return false;
    }

    private void saveUsersToFile() {
        File file = new File(PATH + File.separator + FILE_NAME);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(gson.toJson(getUsers()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
