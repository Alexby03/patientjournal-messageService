package data.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "encounters")
public class Encounter extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "encounter_id", nullable = false)
    private UUID encounterId;

    private String description;

    @Column(name = "encounter_date", nullable = false)
    private LocalDateTime encounterDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "practitioner_id", nullable = false)
    private Practitioner practitioner;

    public Encounter() {}

    public Encounter(String description, LocalDateTime encounterDate,
                     Patient patient, Practitioner practitioner) {
        this.description = description;
        this.encounterDate = encounterDate;
        this.patient = patient;
        this.practitioner = practitioner;
    }

    public void setDescription(String description) { this.description = description; }

    public void setEncounterDate(LocalDateTime encounterDate) { this.encounterDate = encounterDate; }

    public UUID getEncounterId() {
        return encounterId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getEncounterDate() {
        return encounterDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public Practitioner getPractitioner() {
        return practitioner;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setPractitioner(Practitioner practitioner) {
        this.practitioner = practitioner;
    }

    @Override
    public String toString() {
        return "Encounter{" +
                "encounterId=" + encounterId +
                ", description='" + description + '\'' +
                ", encounterDate=" + encounterDate +
                ", patient=" + patient +
                ", practitioner=" + practitioner +
                '}';
    }
}
