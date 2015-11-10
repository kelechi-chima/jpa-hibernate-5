package com.kchima.jpa.tutorial;

import com.kchima.jpa.tutorial.model.CreditCardPayment;
import com.kchima.jpa.tutorial.model.DebitCardPayment;
import com.kchima.jpa.tutorial.model.Item;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("auction");
            em = emf.createEntityManager();

            em.getTransaction().begin();
            CreditCardPayment creditCardPayment = new CreditCardPayment();
            creditCardPayment.setCardNumber("40003000");
            creditCardPayment.setCardExpiryDate("09/16");
            creditCardPayment.setDate(new Date());
            creditCardPayment.setAmount(BigDecimal.TEN);
            em.persist(creditCardPayment);
            em.getTransaction().commit();
            logger.info("persisted: {}", creditCardPayment);

            em.getTransaction().begin();
            DebitCardPayment debitCardPayment = new DebitCardPayment();
            debitCardPayment.setCardNumber("40002853");
            debitCardPayment.setCardExpiryDate("11/17");
            debitCardPayment.setDate(new Date());
            debitCardPayment.setAmount(new BigDecimal("11.00"));
            em.persist(debitCardPayment);
            em.getTransaction().commit();
            logger.info("persisted: {}", debitCardPayment);

            em.getTransaction().begin();
            List<CreditCardPayment> creditCardPayments = em.createQuery("select ccp from CreditCardPayment ccp", CreditCardPayment.class).getResultList();
            assertFalse(creditCardPayments.isEmpty());
            for (CreditCardPayment ccp : creditCardPayments) {
                logger.info("retrieved: {}", ccp);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            closePersistenceContext(em, emf);
        }
    }

    private static void exercisePersistenceOperations() {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("auction");
            em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            Item item = new Item();
            item.setName("HTC One M9+ Supreme Camera");
            em.persist(item);
            tx.commit();
            em.clear();
            logger.debug("persisted: {}", item);

            Long itemId = item.getId();

            tx.begin();
            Item managedOne = em.find(Item.class, itemId);
            Item managedTwo = em.find(Item.class, itemId);
            assertThat(managedTwo, sameInstance(managedOne));
            String sony = "Sony Xperia Z5 Premium Dual";
            managedOne.setName(sony);
            em.persist(managedOne);
            tx.commit();
            em.clear();
            logger.debug("updated: {}", managedOne);

            tx.begin();
            Item itemQueuedForRemoval = em.find(Item.class, itemId);
            em.remove(itemQueuedForRemoval);
            assertFalse(em.contains(itemQueuedForRemoval));
            assertNotNull(itemQueuedForRemoval.getId());
            tx.rollback();

            tx.begin();
            Item itemToBeRefreshed = em.find(Item.class, itemId);
            tx.rollback();
            em.clear();

            itemToBeRefreshed.setName("iPhone 6");

            tx.begin();
            itemToBeRefreshed = em.merge(itemToBeRefreshed);
            em.refresh(itemToBeRefreshed);
            assertThat(itemToBeRefreshed.getName(), equalTo(sony));
            tx.commit();
            logger.debug("refreshed: {}", itemToBeRefreshed);

            tx.begin();
            Item readOnlyItem = em.find(Item.class, itemId);
            em.unwrap(Session.class).setReadOnly(readOnlyItem, true);
            readOnlyItem.setName("Microsoft Lumia 950 XL Dual Sim");
            em.persist(readOnlyItem);
            tx.commit();
            logger.debug("read-only: {}", readOnlyItem);

            tx.begin();
            TypedQuery<Item> typedQuery = em.createQuery("select i from Item i", Item.class).
                    setHint(org.hibernate.annotations.QueryHints.READ_ONLY, true);

            for (Item i : typedQuery.getResultList()) {
                i.setName("Motorola X Droid");
                em.persist(i);
            }

            em.flush();
            tx.commit();

            logger.debug("flush mode: {}", em.getFlushMode());

            em.clear();

            tx.begin();
            String desire = "HTC Desire 9";
            Item autoFlushedItem = em.find(Item.class, itemId);
            autoFlushedItem.setName(desire);
            //entityManager.persist(autoFlushedItem);

            String updatedName = (String) em.createQuery("select i.name from Item i where i.id = :id").
                    setParameter("id", itemId).
                    getSingleResult();

            assertThat(updatedName, equalTo(desire));
            tx.commit();
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            closePersistenceContext(em, emf);
        }
    }

    private static void closePersistenceContext(EntityManager em, EntityManagerFactory emf) {
        if (em != null && em.isOpen()) {
            em.close();
        }

        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
