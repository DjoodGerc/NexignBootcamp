package brtApp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "balance_changes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BalanceChangesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private SubscriberEntity subscriber;

    @Column(name = "value", precision = 10)
    private Double value;

    @Column(name = "date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "change_type_id")
    private ChangeTypeEntity changeType;
}