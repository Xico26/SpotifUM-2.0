package io.github.xico26.spotifum2.model.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JpaEntitiesTest {

    private static EntityManagerFactory emf;
    private EntityManager em;

    @BeforeAll
    public static void setupClass() {
        // Replace "your-persistence-unit-name" with your actual persistence unit name
        emf = Persistence.createEntityManagerFactory("your-persistence-unit-name");
    }

    @AfterAll
    public static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @BeforeEach
    public void setup() {
        em = emf.createEntityManager();
    }

    @AfterEach
    public void tearDown() {
        if (em != null) {
            em.close();
        }
    }

    @Test
    public void testArtistPersistence() {
        em.getTransaction().begin();

        // Create new Artist entity
        Artist artist = new Artist();
        artist.setName("Test Artist");
        artist.setCountry("Testland");
        artist.setBirthDate(LocalDate.of(2005,10,26));
        artist.setDeathDate(null);

        em.persist(artist);
        em.getTransaction().commit();

        // Clear persistence context to force reload from DB
        em.clear();

        // Retrieve the artist back by id
        Artist found = em.find(Artist.class, artist.getId());

        assertNotNull(found);
        assertEquals("Test Artist", found.getName());
        assertEquals("Testland", found.getCountry());
    }

}