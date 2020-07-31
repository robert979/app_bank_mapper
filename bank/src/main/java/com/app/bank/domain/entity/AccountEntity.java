package com.app.bank.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String iban;
    private BigDecimal balance;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastUpdated;

   //@JsonIgnore
   //@ToString.Exclude
   //@ManyToOne(fetch = FetchType.LAZY) //by default e EAGER

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cnp")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "account")
    private List<CardEntity> cards;

}
