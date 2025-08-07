package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.UserDAO;
import io.github.xico26.spotifum2.exceptions.InvalidLoginException;
import io.github.xico26.spotifum2.exceptions.InvalidParamsException;
import io.github.xico26.spotifum2.exceptions.UserNotFoundException;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.plan.ISubscriptionPlan;
import io.github.xico26.spotifum2.model.entity.plan.SubscriptionPlanFactory;
import jakarta.persistence.NoResultException;

import java.time.LocalDate;

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

    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public User login (String username, String password) {
        User user = findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new InvalidLoginException("Wrong Username or Password!");
        }
        return user;
    }

    public void save(User user) {
        userDAO.save(user);
    }

    public void createUser(String username, String password, String name, String address, String email, LocalDate birthDate) {
        User newUser = new User(username, password, name, address, email, birthDate, "FREE");

        if (findByUsername(username) != null) {
            throw new InvalidParamsException("Username already used!");
        }

        if (findByEmail(email) != null) {
            throw new InvalidParamsException("Email already used!");
        }

        save(newUser);
    }

    public void setPlan (User u, String newPlan) {
        u.setSubscriptionPlan(newPlan);
        if (newPlan.equals("PREMIUM")) {
            u.addPoints(100);
        }
    }

    public void removeUser (User u) {
        if (findById(u.getId()) == null) {
            throw new UserNotFoundException("User not found!");
        }

        userDAO.delete(u);
    }

    public ISubscriptionPlan getSubscriptionPlan (User u) {
        return SubscriptionPlanFactory.createPlan(u.getSubscriptionPlan());
    }
}
