package hrsApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tariff_parametr")
@AllArgsConstructor
@NoArgsConstructor
public class TariffParametrEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "parameter_id")
    private Long parameterId;

    @ManyToOne
    @JoinColumn(name = "tarrif_type_id", nullable = false)
    private TariffTypeEntity tariffType;

    @Column(name = "initiating_internal_call_cost", nullable = false, precision = 10, scale = 1)
    private Double initiatingInternalCallCost;

    @Column(name = "recieving_internal_call_cost", nullable = false, precision = 10, scale = 1)
    private Double receivingInternalCallCost;

    @Column(name = "initiating_external_call_cost", nullable = false, precision = 10, scale = 1)
    private Double initiatingExternalCallCost;

    @Column(name = "recieving_external_call_cost", nullable = false, precision = 10, scale = 1)
    private Double receivingExternalCallCost;

    @Column(name = "monthly_minute_capacity", nullable = false)
    private Long monthlyMinuteCapacity;

    @Column(name = "monthly_fee", nullable = false, precision = 10, scale = 1)
    private Double monthlyFee;
}
