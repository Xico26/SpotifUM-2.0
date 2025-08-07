package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Library;
import io.github.xico26.spotifum2.model.entity.User;
import io.github.xico26.spotifum2.model.entity.music.Music;
import io.github.xico26.spotifum2.model.entity.playlist.Playlist;
import jakarta.persistence.*;

import java.util.List;

public class LibraryDAOImpl implements LibraryDAO {
    private EntityManagerFactory emf;

    public LibraryDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Library findByUser(User u) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Library> query = em.createQuery("FROM Library WHERE user = :user", Library.class);
            query.setParameter("user", u);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Library library) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(library);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Library library) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(library);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Library> findAllWithMusic(Music music) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Library> query = em.createQuery(
                    "SELECT l FROM Library l JOIN l.savedMusics m WHERE m = :music", Library.class);
            query.setParameter("music", music);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Library> findAllWithPlaylist(Playlist playlist) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Library> query = em.createQuery(
                    "SELECT l FROM Library l JOIN l.savedPlaylists p WHERE p = :playlist", Library.class);
            query.setParameter("playlist", playlist);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
