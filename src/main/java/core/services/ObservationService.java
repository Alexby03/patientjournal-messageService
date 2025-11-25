package core.services;

import api.dto.ObservationDTO;
import core.mappers.DTOMapper;
import data.entities.Observation;
import data.entities.Patient;
import data.entities.Practitioner;
import data.repositories.ObservationRepository;
import data.repositories.PatientRepository;
import data.repositories.PractitionerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class ObservationService {

    @Inject
    ObservationRepository observationRepository;

    @Inject
    PatientRepository patientRepository;

    @Inject
    PractitionerRepository practitionerRepository;

    public List<ObservationDTO> getPatientObservations(UUID patientId) {
        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }
        List<Observation> observations = observationRepository.findByPatientId(patientId);
        return observations.stream()
                .map(DTOMapper::toObservationDTO)
                .collect(Collectors.toList());
    }

    public ObservationDTO getObservationById(UUID observationId) {
        Observation observation = observationRepository.findById(observationId);
        if (observation == null) {
            throw new IllegalArgumentException("Observation not found");
        }
        return DTOMapper.toObservationDTO(observation);
    }

    public ObservationDTO getMostRecentObservation(UUID patientId) {
        List<Observation> observations = observationRepository.findByPatientId(patientId);
        if (observations == null || observations.isEmpty()) {
            return null;
        }
        Observation latest = observations.stream()
                .max(Comparator.comparing(Observation::getObservationDate))
                .orElse(null);
        return DTOMapper.toObservationDTO(latest);
    }

    public List<ObservationDTO> getPractitionerObservations(UUID practitionerId) {
        List<Observation> observations = observationRepository.findByPractitionerId(practitionerId);
        return observations.stream()
                .map(DTOMapper::toObservationDTO)
                .collect(Collectors.toList());
    }

    public long countPatientObservations(UUID patientId) {
        return observationRepository.countByPatient(patientId);
    }

    @Transactional
    public ObservationDTO createObservation(UUID patientId, UUID practitionerId, ObservationDTO dto) {
        if (dto.description == null || dto.description.isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (dto.observationDate == null) {
            throw new IllegalArgumentException("Observation date is required");
        }

        Patient patient = patientRepository.findById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found");
        }

        Practitioner practitioner = practitionerRepository.findById(practitionerId);
        if (practitioner == null) {
            throw new IllegalArgumentException("Practitioner not found");
        }

        Observation observation = new Observation(
                dto.description,
                dto.observationDate,
                patient,
                practitioner
        );

        observationRepository.persist(observation);
        return DTOMapper.toObservationDTO(observation);
    }


    @Transactional
    public ObservationDTO updateObservation(UUID observationId, ObservationDTO dto) {
        Observation observation = observationRepository.findById(observationId);
        if (observation == null) {
            throw new IllegalArgumentException("Observation not found");
        }

        if (dto.description != null && !dto.description.isEmpty()) {
            observation.setDescription(dto.description);
        }
        if (dto.observationDate != null) {
            observation.setObservationDate(dto.observationDate);
        }

        observationRepository.persist(observation);
        return DTOMapper.toObservationDTO(observation);
    }


    @Transactional
    public boolean deleteObservation(UUID observationId) {
        return observationRepository.deleteById(observationId);
    }
}
