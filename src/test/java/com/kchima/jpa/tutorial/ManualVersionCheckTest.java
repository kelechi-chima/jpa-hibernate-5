package com.kchima.jpa.tutorial;

import com.kchima.jpa.tutorial.model.Category;
import com.kchima.jpa.tutorial.model.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ManualVersionCheckTest {

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

    @Test
    public void queryWithSetLockModeShouldDetectDifferentDatabaseVersionDuringFlush() {
        Category electricals = new Category();
        electricals.setName("electricals");
        electricals.setItems(new HashSet<>());

        Item shaver = new Item();
        shaver.setName("shaver");
        shaver.setBuyNowPrice(new BigDecimal(75L));
        shaver.setCategory(electricals);
        electricals.getItems().add(shaver);

        Item toothbrush = new Item();
        toothbrush.setName("toothbrush");
        toothbrush.setBuyNowPrice(new BigDecimal(42L));
        toothbrush.setCategory(electricals);
        electricals.getItems().add(toothbrush);

        em.persist(electricals);
        em.persist(toothbrush);
        em.persist(shaver);

        Category books = new Category();
        books.setName("books");
        books.setItems(new HashSet<>());

        Item toKillAMockingBird = new Item();
        toKillAMockingBird.setName("toKillAMockingBird");
        toKillAMockingBird.setBuyNowPrice(BigDecimal.TEN);
        toKillAMockingBird.setCategory(books);
        books.getItems().add(toKillAMockingBird);

        Item jpaWithHibernate = new Item();
        jpaWithHibernate.setName("jpaWithHibernate");
        jpaWithHibernate.setBuyNowPrice(new BigDecimal(25L));
        jpaWithHibernate.setCategory(books);
        books.getItems().add(jpaWithHibernate);

        em.persist(books);
        em.persist(toKillAMockingBird);
        em.persist(jpaWithHibernate);
        em.getTransaction().commit();

        List<Long> categories = Arrays.asList(electricals.getId(), books.getId());

        em.getTransaction().begin();

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (Long categoryId : categories) {
            List<Item> items = em.createQuery("select i from Item i where i.category.id = :catId").
                    setLockMode(LockModeType.OPTIMISTIC).
                    setParameter("catId", categoryId).
                    getResultList();

            for (Item item : items) {
                totalPrice = totalPrice.add(item.getBuyNowPrice());
            }
        }

        em.getTransaction().commit();
        assertEquals("152", totalPrice.toString());
    }
}
