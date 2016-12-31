package com.kchima.jpa.tutorial;

import com.kchima.jpa.tutorial.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MergeTest {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
    private EntityManager em;

    @Before
    public void begin() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void end() {
        if (em != null) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            if (em.isOpen()) {
                em.close();
            }
        }

        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    public void whenTransientInstanceIsMergedItBecomesPersistent() {
        Person person = new Person();
        person.setDateOfBirth(new Date());
        person.setEmail("alice@jserve.net");
        person.setFirstName("Alice");
        person.setLastName("Mortice");
        person = em.merge(person);
        assertTrue(em.contains(person));
        assertNotNull(person.getId());
    }
}
