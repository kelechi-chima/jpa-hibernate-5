package com.kchima.jpa.tutorial.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "ITEM")
public class Item {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    protected Long id;

    protected String name;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    protected Person seller;

    @OneToOne(mappedBy = "item")
    protected Auction auction;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getSeller() {
        return seller;
    }

    public void setSeller(Person seller) {
        this.seller = seller;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id) &&
                Objects.equals(name, item.name) &&
                Objects.equals(seller, item.seller) &&
                Objects.equals(auction, item.auction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, seller, auction);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", seller=" + seller +
                ", auction=" + auction +
                '}';
    }
}
