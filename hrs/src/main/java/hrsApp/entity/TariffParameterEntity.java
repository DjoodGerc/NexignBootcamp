package hrsApp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tariff_parameter")
@AllArgsConstructor
@NoArgsConstructor
public class TariffParameterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tariff_type_id", nullable = false)
    private TariffTypeEntity tariffType;

    @Column(name = "initiating_internal_call_cost", nullable = false, precision = 10)
    private Double initiatingInternalCallCost;

    @Column(name = "recieving_internal_call_cost", nullable = false, precision = 10)
    private Double receivingInternalCallCost;

    @Column(name = "initiating_external_call_cost", nullable = false, precision = 10)
    private Double initiatingExternalCallCost;

    @Column(name = "recieving_external_call_cost", nullable = false, precision = 10)
    private Double receivingExternalCallCost;

    @Column(name = "monthly_minute_capacity", nullable = false)
    private Long monthlyMinuteCapacity;

    @Column(name = "monthly_fee", nullable = false, precision = 10)
    private Double monthlyFee;
}
