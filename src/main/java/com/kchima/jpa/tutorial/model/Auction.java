package com.kchima.jpa.tutorial.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "AUCTION")
public class Auction {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    protected Long id;

    @OneToOne
    @JoinColumn(name = "ITEM_ID")
    protected Item item;

    @OneToMany(mappedBy = "auction")
    protected List<Bid> bids;

    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date startDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date endDate;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", item=" + item +
                ", bids=" + bids +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
