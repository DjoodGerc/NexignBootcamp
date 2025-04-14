package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Entity, отражающий таблицу cdr
 */
@Data
@Entity
@Table(name = "call_data")
@AllArgsConstructor
@NoArgsConstructor
public class CallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private SubscriberEntity initiating;

    @ManyToOne(fetch = FetchType.EAGER)
    private SubscriberEntity receiving;

    @Column(name = "startCall")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startCall;

    @Column(name = "endCall")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endCall;


}
