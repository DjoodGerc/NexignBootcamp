package entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "description", length = 255)
    private String description;


}