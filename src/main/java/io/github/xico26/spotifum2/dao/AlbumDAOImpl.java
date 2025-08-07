package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Album;
import io.github.xico26.spotifum2.model.entity.music.Music;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AlbumDAOImpl implements AlbumDAO {
    private EntityManagerFactory emf;

    public AlbumDAOImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Album findById(int id) {
        EntityManager em = emf.createEntityManager();
        Album a = em.find(Album.class, id);
        em.close();
        return a;
    }

    @Override
    public List<Album> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Album> albums = em.createQuery("FROM Album", Album.class).getResultList();
        em.close();
        return albums;
    }

    @Override
    public void save(Album a) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(a);
        tx.commit();
        em.close();
    }

    @Override
    public void delete(Album a) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.remove(a);
        tx.commit();
        em.close();
    }

    @Override
    public void update(Album a) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.merge(a);
        tx.commit();
        em.close();
    }

    @Override
    public List<Album> findByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a WHERE LOWER(a.name) LIKE :title", Album.class);
            query.setParameter("title", "%" + title.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Album> findByArtist(String artist) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a WHERE LOWER(a.artist.name) LIKE :artist", Album.class);
            query.setParameter("artist", "%" + artist.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
