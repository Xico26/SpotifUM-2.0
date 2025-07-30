package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Album;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AlbumDAOImpl implements AlbumDAO {
    private EntityManager em;

    public AlbumDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public Album findById(int id) {
        return em.find(Album.class, id);
    }

    @Override
    public List<Album> findAll() {
        return em.createQuery("FROM album", Album.class).getResultList();
    }

    @Override
    public void save(Album a) {

    }

    @Override
    public void delete(Album a) {

    }

    @Override
    public boolean hasMusic(int id, String musicName) {
        return false;
    }
}
