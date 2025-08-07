package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.music.Music;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MusicDAOImpl implements MusicDAO {
    private final EntityManagerFactory emf;

    public MusicDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Music findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Music.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Music> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Music> query = em.createQuery("SELECT m FROM Music m", Music.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Music music) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (music.getId() == 0) {
                em.persist(music);
            } else {
                em.merge(music);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Music music) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Music managedMusic = em.find(Music.class, music.getId());
            if (managedMusic != null) {
                em.remove(managedMusic);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public int getMusicCount() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(m) FROM Music m", Long.class);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Music> findByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Music> query = em.createQuery("SELECT m FROM Music m WHERE LOWER(m.title) LIKE :title", Music.class);
            query.setParameter("title", "%" + title.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Music> getMusicsByGenre(String genre) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Music> query = em.createQuery("SELECT m FROM Music m WHERE m.genre == :genre", Music.class);
            query.setParameter("genre", genre);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

