package io.github.xico26.spotifum2.service;

import io.github.xico26.spotifum2.dao.ArtistDAO;
import io.github.xico26.spotifum2.model.entity.Artist;
import io.github.xico26.spotifum2.model.entity.User;

import java.util.List;

public class ArtistService {
    private final ArtistDAO artistDAO;

    public ArtistService(ArtistDAO artistDAO) {
        this.artistDAO = artistDAO;
    }

    public Artist findById (int id) {
        return artistDAO.findById(id);
    }

    public List<Artist> findAll() {
        return artistDAO.findAll();
    }

    public void save(Artist artist) {
        artistDAO.save(artist);
    }

    public void delete(Artist artist) {
        artistDAO.delete(artist);
    }

    public void update(Artist artist) {
        artistDAO.update(artist);
    }
}
