package io.github.xico26.spotifum2.dao;

import io.github.xico26.spotifum2.model.entity.ListeningRecord;
import io.github.xico26.spotifum2.model.entity.User;
import jakarta.persistence.*;

import java.util.List;

public class ListeningRecordDAOImpl implements ListeningRecordDAO {
    private EntityManagerFactory emf;

    public ListeningRecordDAOImpl() {
        this.emf = Persistence.createEntityManagerFactory("spotifumPU");
    }

    @Override
    public List<ListeningRecord> findByUser(User u) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<ListeningRecord> query = em.createQuery("SELECT l FROM ListeningRecord l WHERE l.user == :user", ListeningRecord.class);
            query.setParameter("user", u);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteByUser(User u) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM ListeningRecord l WHERE l.user == :user")
                    .setParameter("user", u)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void save (ListeningRecord listeningRecord) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(listeningRecord);
        tx.commit();
        em.close();
    }

    @Override
    public boolean hasListened (User u, int musicId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(l) FROM ListeningRecord l WHERE l.user == :user AND l.music.id = :music_id", Long.class);
            query.setParameter("user", u);
            query.setParameter("music_id", musicId);

            Long count = query.getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public int getNumListened (User u) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(l) FROM ListeningRecord l WHERE l.user == :user", Long.class);
            query.setParameter("user", u);

            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}
