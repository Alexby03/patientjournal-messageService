package core.services;

import api.dto.ConditionDTO;
import core.mappers.DTOMapper;
import data.entities.Condition;
import data.entities.Patient;
import data.entities.Practitioner;
import data.repositories.ConditionRepository;
import data.repositories.PatientRepository;
import data.repositories.PractitionerRepository;
import core.enums.ConditionType;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ConditionService {

    @Inject
    ConditionRepository conditionRepository;

    @Inject
    PatientRepository patientRepository;

    @Inject
    PractitionerRepository practitionerRepository;

    public List<ConditionDTO> getPatientConditions(UUID patientId, boolean eager) {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        List<Condition> conditions;
        if (eager) {
            Patient patientWithRelations = patientRepository.findByIdWithRelations(patientId);
            conditions = patientWithRelations.getConditions();
        } else {
            conditions = conditionRepository.findByPatientId(patientId);
        }

        return conditions.stream()
                .map(DTOMapper::toConditionDTO)
                .collect(Collectors.toList());
    }

    public List<ConditionDTO> getPractitionerConditions(UUID practitionerId) {
        Practitioner practitioner = practitionerRepository.findById(practitionerId);
        if (practitioner == null) {
            throw new IllegalArgumentException("Practitioner not found");
        }
        List<Condition> conditions = conditionRepository.findByPractitionerId(practitionerId);

        return conditions.stream()
                .map(DTOMapper::toConditionDTO)
                .collect(Collectors.toList());
    }

    public ConditionDTO getConditionById(UUID conditionId) {
        Condition condition = conditionRepository.findById(conditionId);
        if (condition == null) {
            throw new IllegalArgumentException("Condition not found");
        }
        return DTOMapper.toConditionDTO(condition);
    }

    @Transactional
    public ConditionDTO createCondition(UUID patientId, UUID practitionerId, ConditionDTO dto) {
        validateConditionDTO(dto);

        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        Practitioner practitioner = practitionerRepository.findById(practitionerId);
        if (practitioner == null) {
            throw new IllegalArgumentException("Practitioner not found");
        }

        Condition condition = new Condition(
                dto.conditionName,
                dto.severityLevel,
                dto.conditionType,
                dto.diagnosedDate,
                patient,
                practitioner
        );

        conditionRepository.persist(condition);
        return DTOMapper.toConditionDTO(condition);
    }

    @Transactional
    public ConditionDTO updateCondition(UUID conditionId, ConditionDTO dto) {
        Condition condition = conditionRepository.findById(conditionId);
        if (condition == null) {
            throw new IllegalArgumentException("Condition not found");
        }

        if (dto.conditionName != null && !dto.conditionName.isEmpty()) {
            condition.setConditionName(dto.conditionName);
        }
        if (dto.severityLevel > 0) {
            condition.setSeverityLevel(dto.severityLevel);
        }
        if (dto.conditionType != null) {
            condition.setConditionType(dto.conditionType);
        }
        if (dto.diagnosedDate != null) {
            condition.setDiagnosedDate(dto.diagnosedDate);
        }

        conditionRepository.persist(condition);

        return DTOMapper.toConditionDTO(condition);
    }

    @Transactional
    public boolean deleteCondition(UUID conditionId) {
        return conditionRepository.deleteById(conditionId);
    }

    public List<ConditionDTO> getHighSeverityConditions() {
        List<Condition> conditions = conditionRepository.findHighSeverityConditions();
        return conditions.stream()
                .map(DTOMapper::toConditionDTO)
                .collect(Collectors.toList());
    }

    public List<ConditionDTO> getConditionsByType(ConditionType conditionType) {
        List<Condition> conditions = conditionRepository.findByConditionType(conditionType);
        return conditions.stream()
                .map(DTOMapper::toConditionDTO)
                .collect(Collectors.toList());
    }

    public long countPatientConditions(UUID patientId) {
        return conditionRepository.countByPatient(patientId);
    }

    private void validateConditionDTO(ConditionDTO dto) {
        if (dto.conditionName == null || dto.conditionName.isEmpty()) {
            throw new IllegalArgumentException("Condition name is required");
        }
        if (dto.severityLevel < 1 || dto.severityLevel > 10) {
            throw new IllegalArgumentException("Severity level must be between 1 and 10");
        }
        if (dto.conditionType == null) {
            throw new IllegalArgumentException("Condition type is required");
        }
        if (dto.diagnosedDate == null) {
            throw new IllegalArgumentException("Diagnosed date is required");
        }
    }
}