package core.services;

import api.dto.EncounterDTO;
import core.mappers.DTOMapper;
import data.entities.Encounter;
import data.entities.Patient;
import data.entities.Practitioner;
import data.repositories.EncounterRepository;
import data.repositories.PatientRepository;
import data.repositories.PractitionerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class EncounterService {

    @Inject
    EncounterRepository encounterRepository;

    @Inject
    PatientRepository patientRepository;

    @Inject
    PractitionerRepository practitionerRepository;

    public List<EncounterDTO> getPatientEncounters(UUID patientId, boolean eager) {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        List<Encounter> encounters = eager
                ? encounterRepository.findByPatientIdWithRelations(patientId)
                : encounterRepository.findByPatientId(patientId);

        return encounters.stream()
                .map(DTOMapper::toEncounterDTO)
                .collect(Collectors.toList());
    }

    public EncounterDTO getEncounterById(UUID encounterId) {
        Encounter encounter = encounterRepository.findById(encounterId);
        if (encounter == null) {
            throw new IllegalArgumentException("Encounter not found");
        }
        return DTOMapper.toEncounterDTO(encounter);
    }

    @Transactional
    public EncounterDTO createEncounter(UUID patientId, UUID practitionerId, EncounterDTO dto) {

        if (dto.description == null || dto.description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (dto.encounterDate == null) {
            throw new IllegalArgumentException("Encounter date is required");
        }

        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        Practitioner practitioner = practitionerRepository.findById(practitionerId);
        if (practitioner == null) {
            throw new IllegalArgumentException("Practitioner not found");
        }

        Encounter encounter = new Encounter(
                dto.description,
                dto.encounterDate,
                patient,
                practitioner
        );

        encounterRepository.persist(encounter);
        return DTOMapper.toEncounterDTO(encounter);
    }

    @Transactional
    public EncounterDTO updateEncounter(UUID encounterId, EncounterDTO dto) {
        Encounter encounter = encounterRepository.findById(encounterId);
        if (encounter == null) {
            throw new IllegalArgumentException("Encounter not found");
        }

        if (dto.description != null && !dto.description.isEmpty()) {
            encounter.setDescription(dto.description);
        }
        if (dto.encounterDate != null) {
            encounter.setEncounterDate(dto.encounterDate);
        }

        encounterRepository.persist(encounter);
        return DTOMapper.toEncounterDTO(encounter);
    }

    @Transactional
    public boolean deleteEncounter(UUID encounterId) {
        return encounterRepository.deleteById(encounterId);
    }

    public List<EncounterDTO> getRecentEncounters(boolean eager) {
        List<Encounter> encounters = eager
                ? encounterRepository.findRecentEncountersWithRelations()
                : encounterRepository.findRecentEncounters();

        return encounters.stream()
                .map(DTOMapper::toEncounterDTO)
                .collect(Collectors.toList());
    }

    public List<EncounterDTO> getPractitionerEncounters(UUID practitionerId, boolean eager) {
        List<Encounter> encounters = eager
                ? encounterRepository.findByPractitionerIdWithRelations(practitionerId)
                : encounterRepository.findByPractitionerId(practitionerId);

        return encounters.stream()
                .map(DTOMapper::toEncounterDTO)
                .collect(Collectors.toList());
    }

    public long countPatientEncounters(UUID patientId) {
        return encounterRepository.countByPatient(patientId);
    }
}
