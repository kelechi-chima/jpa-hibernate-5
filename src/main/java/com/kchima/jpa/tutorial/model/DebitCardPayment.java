package com.kchima.jpa.tutorial.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "DEBIT_CARD_PAYMENT")
//@DiscriminatorValue("DEBIT_CARD")
public class DebitCardPayment extends Payment {

    @Column(name = "card_number")
    protected String cardNumber;

    @Column(name = "card_expiry_date")
    protected String cardExpiryDate;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    @Override
    public String toString() {
        return "DebitCardPayment{" +
                "id=" + id +
                ", " + super.toString() +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardExpiryDate='" + cardExpiryDate + '\'' +
                '}';
    }
}
