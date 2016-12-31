package com.kchima.jpa.tutorial;

import java.util.Date;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kchima.jpa.tutorial.model.Post;

public class MappingCollectionsTest {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("auction");
    private static EntityManager em;

    @BeforeClass
    public static void before() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterClass
    public static void after() {
        if (em != null) {
            EntityTransaction tx = em.getTransaction();
            if (tx.isActive() && !tx.getRollbackOnly()) {
                tx.commit();
            } else if (tx.isActive() && tx.getRollbackOnly()) {
                tx.rollback();
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
    public void mapSet() {
        Post p = new Post();
        p.setDescription("Post one");
        p.setPostDate(new Date());
        p.getImages().add("image1.jpg");
        p.getImages().add("image2.jpg");
        em.persist(p);
        em.flush();
        em.getTransaction().commit();
        Long id = p.getId();
        em.clear();

        Post p2 = em.find(Post.class, id);
        Iterator<String> iterator = p2.getImages().iterator();
        System.out.println(iterator.next());
        System.out.println(iterator.next());
    }
}
