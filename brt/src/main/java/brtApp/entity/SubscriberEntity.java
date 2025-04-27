package brtApp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.postgresql.util.PGmoney;

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

    @Column(name ="tariff_id")
    private Long tariffId;


    @Column(name = "last_month_tariffication_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastMonthTarifficationDate;

    @Column(name = "balance", precision = 10, scale = 2)
    private Double balance;

    @Column(name = "registration_date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registrationDate;

    @Column(name = "passport_data", length = 10)
    private String passportData;

    @Column(name = "tariff_balance")
    private Long tariffBalance;

    @Column(name = "is_active")
    private Boolean isActive;

    public Double changeBalance(double value) {
        this.balance += value;
        return this.balance;
    }

    public Long changeTariffBalance(long value) {
        this.tariffBalance += value;
        return this.tariffBalance;
    }

}
