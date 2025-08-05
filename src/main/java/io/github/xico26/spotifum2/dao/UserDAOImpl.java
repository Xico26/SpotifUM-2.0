package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.User;
import jakarta.persistence.*;

import java.util.List;

public class UserDAOImpl implements UserDAO {
    private EntityManagerFactory emf;

    public UserDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User findById(int userId) {
        EntityManager em = emf.createEntityManager();
        User u = em.find(User.class, userId);
        em.close();
        return u;
    }

    @Override
    public List<User> findAll() {
        EntityManager em = emf.createEntityManager();
        List<User> users = em.createQuery("FROM User", User.class).getResultList();
        em.close();
        return users;
    }

    @Override
    public User findByUsername(String username) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.name=:name", User.class);
            query.setParameter("name", username);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(User u) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(u);
        tx.commit();
        em.close();
    }

    @Override
    public void delete(User u) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.remove(u);
        tx.commit();
        em.close();
    }

    @Override
    public void update(User u) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(u);
        tx.commit();
        em.close();
    }

}
