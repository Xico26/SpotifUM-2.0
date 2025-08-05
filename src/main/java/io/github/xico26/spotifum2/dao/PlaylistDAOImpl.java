package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Artist;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PlaylistDAOImpl implements PlaylistDAO {
    private final EntityManagerFactory emf;

    public PlaylistDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("spotifumPU");
    }

    @Override
    public Playlist findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Playlist.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Playlist> findByUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Playlist> query = em.createQuery("SELECT p FROM Playlist p WHERE p.creator = :user", Playlist.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Playlist> findPublicPlaylists() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Playlist> query = em.createQuery("SELECT p FROM Playlist p WHERE p.isPublic = true", Playlist.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Playlist> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Playlist> playlists = em.createQuery("FROM Playlist", Playlist.class).getResultList();
        em.close();
        return playlists;
    }

    @Override
    public void save(Playlist p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }  finally {
            em.close();
        }
    }

    @Override
    public void delete(Playlist p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(p);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Playlist p) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p);
            em.getTransaction().commit();
        }  catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
