package com.kchima.jpa.tutorial;

import com.kchima.jpa.tutorial.model.Item;
import com.kchima.jpa.tutorial.model.Person;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.hibernate.proxy.HibernateProxyHelper;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class FetchPlanStrategyTest {

    @Test
    public void getReferenceShouldReturnPlaceholderProxy() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("auction");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Item item = new Item();
            item.setName("Shaver");
            item.setBuyNowPrice(new BigDecimal(75L));
            em.persist(item);
            em.getTransaction().commit();

            Long itemId = item.getId();
            em.clear();

            em.getTransaction().begin();
            Item proxy = em.getReference(Item.class, itemId);
            //assertEquals(itemId, proxy.getId());

            assertNotEquals(Item.class, proxy.getClass());
            assertEquals(Item.class, HibernateProxyHelper.getClassWithoutInitializingProxy(proxy));
            assertFalse(Hibernate.isInitialized(proxy));

            PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
            assertFalse(persistenceUtil.isLoaded(proxy));
            assertFalse(persistenceUtil.isLoaded(proxy, "seller"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }

    @Test(expected = LazyInitializationException.class)
    public void callingUninitializedFieldInDetachedStateShouldCauseException() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("auction");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            Person seller = new Person();
            seller.setFirstName("Kelechi");
            seller.setLastName("Chima");
            seller.setEmail("coder_yoda@gmail.com");
            Item item = new Item();
            item.setName("Shaver");
            item.setBuyNowPrice(new BigDecimal(75L));
            seller.getItems().add(item);
            item.setSeller(seller);
            em.persist(seller);
            //em.persist(item);
            em.getTransaction().commit();

            Long itemId = item.getId();
            em.clear();

            em.getTransaction().begin();
            item = em.find(Item.class, itemId);
            assertEquals(itemId, item.getId());

            em.detach(item);
            em.detach(item.getSeller());
            PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
            assertTrue(persistenceUtil.isLoaded(item));
            assertFalse(persistenceUtil.isLoaded(item, "seller"));
            assertEquals("Kelechi", item.getSeller().getFirstName());
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
