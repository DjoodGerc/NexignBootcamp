package crmApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@ToString(exclude = {"subscriber"})
@EqualsAndHashCode(exclude = {"subscriber"})
@NoArgsConstructor
@AllArgsConstructor
@Transactional
public class RolesEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    public RolesEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<SubscriberEntity> subscriber;

    @Override
    public String getAuthority() {
        return getName();
    }
}