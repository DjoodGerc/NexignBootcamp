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
@Table(name = "call")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startCall;

    @Column(name = "end_call")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endCall;

    @Column(name = "total_cost", precision = 10)
    private Double totalCost;

    @ManyToOne
    @JoinColumn(name = "call_type_id")
    private CallTypeEntity callType;

    @Column(name = "is_romashka_call")
    private Boolean isRomashkaCall;

}
