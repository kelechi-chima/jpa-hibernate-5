package com.kchima.jpa.tutorial;

import com.kchima.jpa.tutorial.model.Item;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class App {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("jpa-tutorial");
            entityManager = entityManagerFactory.createEntityManager();
            EntityTransaction tx = entityManager.getTransaction();
            tx.begin();
            Item item = new Item();
            item.setName("HTC One M9+ Supreme Camera");
            entityManager.persist(item);
            tx.commit();
            entityManager.clear();
            logger.debug("persisted: {}", item);

            Long itemId = item.getId();

            tx.begin();
            Item managedOne = entityManager.find(Item.class, itemId);
            Item managedTwo = entityManager.find(Item.class, itemId);
            assertThat(managedTwo, sameInstance(managedOne));
            String sony = "Sony Xperia Z5 Premium Dual";
            managedOne.setName(sony);
            entityManager.persist(managedOne);
            tx.commit();
            entityManager.clear();
            logger.debug("updated: {}", managedOne);

            tx.begin();
            Item itemQueuedForRemoval = entityManager.find(Item.class, itemId);
            entityManager.remove(itemQueuedForRemoval);
            assertFalse(entityManager.contains(itemQueuedForRemoval));
            assertNotNull(itemQueuedForRemoval.getId());
            tx.rollback();

            tx.begin();
            Item itemToBeRefreshed = entityManager.find(Item.class, itemId);
            tx.rollback();
            entityManager.clear();

            itemToBeRefreshed.setName("iPhone 6");

            tx.begin();
            itemToBeRefreshed = entityManager.merge(itemToBeRefreshed);
            entityManager.refresh(itemToBeRefreshed);
            assertThat(itemToBeRefreshed.getName(), equalTo(sony));
            tx.commit();
            logger.debug("refreshed: {}", itemToBeRefreshed);

            tx.begin();
            Item readOnlyItem = entityManager.find(Item.class, itemId);
            entityManager.unwrap(Session.class).setReadOnly(readOnlyItem, true);
            readOnlyItem.setName("Microsoft Lumia 950 XL Dual Sim");
            entityManager.persist(readOnlyItem);
            tx.commit();
            logger.debug("read-only: {}", readOnlyItem);

            tx.begin();
            TypedQuery<Item> typedQuery = entityManager.createQuery("select i from Item i", Item.class).
                    setHint(org.hibernate.annotations.QueryHints.READ_ONLY, true);

            for (Item i : typedQuery.getResultList()) {
                i.setName("Motorola X Droid");
                entityManager.persist(i);
            }

            entityManager.flush();
            tx.commit();

            logger.debug("flush mode: {}", entityManager.getFlushMode());

            entityManager.clear();

            tx.begin();
            String desire = "HTC Desire 9";
            Item autoFlushedItem = entityManager.find(Item.class, itemId);
            autoFlushedItem.setName(desire);
            //entityManager.persist(autoFlushedItem);

            String updatedName = (String) entityManager.createQuery("select i.name from Item i where i.id = :id").
                    setParameter("id", itemId).
                    getSingleResult();

            assertThat(updatedName, equalTo(desire));
            tx.commit();
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }

            if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
                entityManagerFactory.close();
            }
        }
    }
}
