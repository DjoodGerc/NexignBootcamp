package hrsApp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tariff")
@AllArgsConstructor
@NoArgsConstructor
public class TariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    String name;
    @JsonProperty("active")
    @Column(name = "is_active")
    boolean isActive;
    @Column(name = "creation_date")
    LocalDateTime creationDate;

    String description;

    @OneToOne
    @JoinColumn(name = "tariff_parameter_id", nullable = false)
    private TariffParameterEntity tariffParametr;

}