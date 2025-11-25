package data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.enums.OrganizationType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "organizations")
public class Organization extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "organization_type", nullable = false)
    private OrganizationType organizationType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @JsonIgnore
    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Practitioner> practitioners = new ArrayList<>();

    public Organization() {
    }

    public Organization(OrganizationType organizationType, Location location) {
        this.organizationType = organizationType;
        this.location = location;
    }

    public void setOrganizationType(OrganizationType organizationType) { this.organizationType = organizationType; }

    public void setPractitioners(List<Practitioner> practitioners) { this.practitioners = practitioners; }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public Location getLocation() {
        return location;
    }

    public List<Practitioner> getPractitioners() {
        return practitioners;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addPractitioner(Practitioner practitioner) {
        practitioners.add(practitioner);
        practitioner.setOrganization(this);
    }

    public void removePractitioner(Practitioner practitioner) {
        practitioners.remove(practitioner);
        practitioner.setOrganization(null);
    }

    @Override
    public String toString() {
        return "OrganizationDb{" +
                "organizationId=" + organizationId +
                ", organizationType=" + organizationType +
                '}';
    }
}
