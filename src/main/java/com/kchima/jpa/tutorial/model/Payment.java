package com.kchima.jpa.tutorial.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PAYMENT")
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Payment {

    @Id
    @GeneratedValue
    protected Long id;

    protected BigDecimal amount;

    @Temporal(value = TemporalType.DATE)
    @Column(name = "payment_date")
    protected Date date;

    /*@Enumerated(value = EnumType.STRING)
    @Column(name = "payment_type")
    protected PaymentType paymentType;*/

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

    /*public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }*/

    @Override
    public String toString() {
        return "amount=" + amount +
                ", date=" + date;/* +
                ", payment_type=" + paymentType;*/
    }
}
