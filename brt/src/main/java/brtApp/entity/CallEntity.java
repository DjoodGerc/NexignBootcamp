package brtApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "call")
@AllArgsConstructor
@NoArgsConstructor
public class CallEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private SubscriberEntity subscriber;

    @Column(name = "opponent_msisdn")
    private String opponentMsisdn;

    @Column(name = "start_call")
    private LocalDateTime startCall;

    @Column(name = "end_call")
    private LocalDateTime endCall;

    @Column(name = "total_cost")
    private Double totalCost;

    @ManyToOne
    @JoinColumn(name = "call_type_id")
    private CallTypeEntity callType;

    @Column(name = "is_romashka_call")
    private Boolean isRomashkaCall;

}
