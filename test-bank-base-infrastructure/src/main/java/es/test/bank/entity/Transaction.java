package es.test.bank.entity;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "TRANSACTION")
@SQLDelete(sql = "UPDATE TRANSACTION " + "SET deleted = true " + "WHERE id = ?")
@Where(clause = "deleted = false")
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Transaction extends GenericEntity {

    @Column(name = "reference", unique = true)
    private String reference;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountIban", nullable = false)
    @EqualsAndHashCode.Exclude
    private Account accountIban;

    @Column(columnDefinition = "TIMESTAMP")
    private OffsetDateTime date;
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private Double amount;
    
    @Column(name = "fee", precision = 10, scale = 2)
    private Double fee;
    
    @Column(name = "description")
    private String description;
}