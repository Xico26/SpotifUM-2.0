package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Artist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class ArtistDAOImpl implements ArtistDAO {
    private final EntityManagerFactory emf;

    public ArtistDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Artist findById(int id) {
        EntityManager em = emf.createEntityManager();
        Artist a = em.find(Artist.class, id);
        em.close();
        return a;
    }

    @Override
    public List<Artist> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Artist> artists = em.createQuery("FROM Artist", Artist.class).getResultList();
        em.close();
        return artists;
    }

    @Override
    public void save(Artist a) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(a);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }  finally {
            em.close();
        }
    }

    @Override
    public void delete(Artist a) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(a);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Artist a) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(a);
            em.getTransaction().commit();
        }  catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
}
