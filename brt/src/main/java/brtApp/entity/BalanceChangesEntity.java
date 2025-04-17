package brtApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "balance_changes")
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChangesEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "subscriber_id")
        private SubscriberEntity subscriber;

        @Column(name = "value")
        private Double value;

        @Column(name = "date")
        private LocalDateTime date;

        @ManyToOne
        @JoinColumn(name = "change_type_id")
        private ChangeTypeEntity changeType;
    }