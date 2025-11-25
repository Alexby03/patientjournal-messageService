package data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import core.enums.ConditionType;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "conditions")
public class Condition extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "condition_id", nullable = false)
    private UUID conditionId;

    @Column(name = "condition_name", nullable = false)
    private String conditionName;

    @Column(name = "severity_level", nullable = false)
    private int severityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false)
    private ConditionType conditionType;

    @Column(name = "diagnosed_date", nullable = false)
    private LocalDate diagnosedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "practitioner_id", nullable = false)
    @JsonIgnore
    private Practitioner practitioner;


    public Condition() {}

    public Condition(String conditionName, int severityLevel, ConditionType conditionType,
                     LocalDate diagnosedDate, Patient patient, Practitioner practitioner) {
        this.conditionName = conditionName;
        this.severityLevel = severityLevel;
        this.conditionType = conditionType;
        this.diagnosedDate = diagnosedDate;
        this.patient = patient;
        this.practitioner = practitioner;
    }

    public void setConditionName(String conditionName) { this.conditionName = conditionName; }

    public void setConditionType(ConditionType conditionType) { this.conditionType = conditionType; }

    public void setDiagnosedDate(LocalDate diagnosedDate) { this.diagnosedDate = diagnosedDate; }

    public UUID getConditionId() {
        return conditionId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public int getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(int severityLevel) {this.severityLevel = severityLevel;}

    public ConditionType getConditionType() {
        return conditionType;
    }

    public LocalDate getDiagnosedDate() {
        return diagnosedDate;
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
        return "Condition{" +
                "conditionId=" + conditionId +
                ", conditionName='" + conditionName + '\'' +
                ", severityLevel=" + severityLevel +
                ", conditionType=" + conditionType +
                ", diagnosedDate=" + diagnosedDate +
                ", patient=" + patient +
                ", practitioner=" + practitioner +
                '}';
    }
}
