package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.Library;
import io.github.xico26.spotifum2.model.entity.User;
import jakarta.persistence.*;

public class LibraryDAOImpl implements LibraryDAO {
    private EntityManagerFactory emf;

    public LibraryDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("spotifumPU");
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
}
