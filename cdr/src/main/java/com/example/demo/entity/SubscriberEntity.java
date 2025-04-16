package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity, отражающий таблицу subscriber
 */
@Data
@Entity
@Table(name = "subscriber")
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "msisdn", nullable = false)
    private String msisdn;

    @ManyToOne(fetch = FetchType.EAGER)
    private OperatorEntity operator;

}
