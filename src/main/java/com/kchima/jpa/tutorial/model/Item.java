package com.kchima.jpa.tutorial.model;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Column(name = "buy_now_price")
    protected BigDecimal buyNowPrice;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category;

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

    public BigDecimal getBuyNowPrice() {
        return buyNowPrice;
    }

    public void setBuyNowPrice(BigDecimal buyNowPrice) {
        this.buyNowPrice = buyNowPrice;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.getId()) &&
                Objects.equals(name, item.getName()) &&
                Objects.equals(buyNowPrice, item.getBuyNowPrice()) &&
                Objects.equals(seller, item.getSeller()) &&
                Objects.equals(auction, item.getAuction()) &&
                Objects.equals(category, item.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, buyNowPrice, seller, auction, category);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", buy_now_price=" + buyNowPrice +
                ", seller=" + seller +
                ", auction=" + auction +
                ", category=" + category +
                '}';
    }
}
