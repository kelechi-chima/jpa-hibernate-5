package com.kchima.jpa.tutorial;

import com.kchima.jpa.tutorial.model.Item;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class App {

    public static void main( String[] args ) {
        EntityManagerFactory entityManagerFactory =  Persistence.createEntityManagerFactory("jpa-tutorial");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction userTransaction = em.getTransaction();
        userTransaction.begin();
        Item item = new Item();
        item.setName("Item 1");
        em.persist(item);
        userTransaction.commit();
        em.close();
        entityManagerFactory.close();
    }
}
