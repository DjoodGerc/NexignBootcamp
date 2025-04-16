package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "subscriber")
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "msisdn", length = 11)
    private String msisdn;

    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private TariffEntity tariff;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "passport_data", length = 10)
    private String passportData;

    @Column(name = "tariff_balance")
    private Integer tariffBalance;

    @Column(name = "is_active")
    private Boolean isActive;

}
