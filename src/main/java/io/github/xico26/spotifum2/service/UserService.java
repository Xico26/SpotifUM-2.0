package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.UserDAO;
import io.github.xico26.spotifum2.model.entity.User;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User findById(int id) {
        return userDAO.findById(id);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
}
