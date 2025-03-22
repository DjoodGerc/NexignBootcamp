package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "cdr")
@AllArgsConstructor
@NoArgsConstructor
public class CdrEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private SubscriberEntity initiating;

    @ManyToOne(fetch = FetchType.EAGER)
    private SubscriberEntity receiving;

    @Column(name = "startCall")
    private Timestamp startCall;

    @Column(name = "endCall")
    private Timestamp endCall;


}
