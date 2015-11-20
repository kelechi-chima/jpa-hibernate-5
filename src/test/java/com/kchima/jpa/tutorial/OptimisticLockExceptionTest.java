package com.kchima.jpa.tutorial;

import com.kchima.jpa.tutorial.model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OptimisticLockExceptionTest {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auction");
    private EntityManager em;

    @Before
    public void begin() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void end() {
        if (em != null) {
            if (em.getTransaction().isActive() && !em.getTransaction().getRollbackOnly()) {
                em.getTransaction().commit();
            } else if (em.getTransaction().isActive() && em.getTransaction().getRollbackOnly()) {
                em.getTransaction().rollback();
            }

            if (em.isOpen()) {
                em.close();
            }
        }

        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test(expected = OptimisticLockException.class)
    public void whenRowThatMapsToADirtyPersistentEntityIsUpdatedByAnotherProcessFlushShouldResultInOptimisticLockException() {
        Person p = new Person();
        p.setFirstName("Paul");
        p.setLastName("Pogba");
        p.setEmail("paul.pogba@francais.fr");
        LocalDate localDate = LocalDate.of(1984, Month.JUNE, 15);
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date dob = Date.from(instant);
        p.setDateOfBirth(dob);
        em.persist(p);
        em.flush();
        em.getTransaction().commit();
        em.clear();

        em.getTransaction().begin();
        Long id = p.getId();
        Person p2 = em.find(Person.class, id);
        p2.setEmail("paul.pogba@france.fr");
        em.flush();
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
