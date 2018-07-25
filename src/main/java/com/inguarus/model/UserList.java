package com.inguarus.model;

import java.util.ArrayList;
import java.util.List;

public class UserList {

    private List<User> users = new ArrayList<>();

    public UserList(List<User> users) {
        this.users.addAll(users);
    }
}
