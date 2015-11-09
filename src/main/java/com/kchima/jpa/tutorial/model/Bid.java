package com.kchima.jpa.tutorial.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "BID")
public class Bid {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "PERSON_ID")
    protected Person bidder;

    @ManyToOne
    @JoinColumn(name = "AUCTION_ID")
    protected Auction auction;

    protected BigDecimal amount;

    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date date;

    protected Boolean winningBid;

    public Person getBidder() {
        return bidder;
    }

    public void setBidder(Person bidder) {
        this.bidder = bidder;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean isWinningBid() {
        return winningBid;
    }

    public void setWinningBid(Boolean winningBid) {
        this.winningBid = winningBid;
    }

    public Boolean getWinningBid() {
        return winningBid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(id, bid.id) &&
                Objects.equals(bidder, bid.bidder) &&
                Objects.equals(auction, bid.auction) &&
                Objects.equals(amount, bid.amount) &&
                Objects.equals(date, bid.date) &&
                Objects.equals(winningBid, bid.winningBid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bidder, auction, amount, date, winningBid);
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidder=" + bidder +
                ", auction=" + auction +
                ", amount=" + amount +
                ", date=" + date +
                ", winningBid=" + winningBid +
                '}';
    }
}
