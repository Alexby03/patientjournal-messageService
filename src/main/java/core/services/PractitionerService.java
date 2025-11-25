package core.services;

import api.dto.PractitionerCreateDTO;
import api.dto.PractitionerDTO;
import core.enums.UserType;
import core.mappers.DTOMapper;
import data.entities.Organization;
import data.entities.Practitioner;
import data.repositories.OrganizationRepository;
import data.repositories.PractitionerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class PractitionerService {

    @Inject
    PractitionerRepository practitionerRepository;

    @Inject
    OrganizationRepository organizationRepository;

    public List<PractitionerDTO> getAllPatients(int pageIndex, int pageSize, boolean eager) {
        List<Practitioner> practitioners = eager
                ? practitionerRepository.findAllPractitionersWithRelations(pageIndex, pageSize)
                : practitionerRepository.findAllPractitioners(pageIndex, pageSize);
        return practitioners.stream()
                .map(p -> DTOMapper.toPractitionerDTO(p, eager))
                .collect(Collectors.toList());
    }

    public PractitionerDTO getPractitionerById(UUID practitionerId, boolean eager) {
        Practitioner practitioner = eager
                ? practitionerRepository.findByIdWithRelations(practitionerId)
                : practitionerRepository.findById(practitionerId);
        if (practitioner == null) {
            throw new IllegalArgumentException("Practitioner not found");
        }
        return DTOMapper.toPractitionerDTO(practitioner, eager);
    }

    public List<PractitionerDTO> getPractitionersByOrganization(UUID organizationId, boolean eager) {
        Organization org = organizationRepository.findById(organizationId);
        if (org == null) {
            throw new IllegalArgumentException("Organization not found");
        }

        List<Practitioner> practitioners = eager
                ? practitionerRepository.findByOrganizationIdWithRelations(organizationId)
                : practitionerRepository.findByOrganizationId(organizationId);
        return practitioners.stream()
                .map(p -> DTOMapper.toPractitionerDTO(p, eager))
                .collect(Collectors.toList());
    }

    public PractitionerDTO getPractitionerByEmail(String email, boolean eager) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        Practitioner practitioner = eager
                ? practitionerRepository.findByEmailWithRelations(email)
                :  practitionerRepository.findByEmail(email);
        if (practitioner == null) {
            throw new IllegalArgumentException("Practitioner not found");
        }
        return DTOMapper.toPractitionerDTO(practitioner, eager);
    }

    @Transactional
    public PractitionerDTO createPractitioner(PractitionerCreateDTO dto) {
        validateCreateDTO(dto);

        Organization org = organizationRepository.findById(dto.organizationId);
        if (org == null) {
            throw new IllegalArgumentException("Organization not found");
        }

        Practitioner existing = practitionerRepository.findByEmail(dto.email);
        if (existing != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        Practitioner practitioner = new Practitioner(
                dto.fullName,
                dto.email,
                hashPassword(dto.password),
                UserType.Doctor,
                org
        );

        practitionerRepository.persist(practitioner);
        return DTOMapper.toPractitionerDTO(practitioner, false);
    }

    @Transactional
    public PractitionerDTO updatePractitioner(UUID practitionerId, PractitionerCreateDTO dto) {
        Practitioner practitioner = practitionerRepository.findById(practitionerId);
        if (practitioner == null) {
            throw new IllegalArgumentException("Practitioner not found");
        }

        if (dto.fullName != null && !dto.fullName.isEmpty()) {
            practitioner.setFullName(dto.fullName);
        }
        if (dto.password != null && !dto.password.isEmpty()) {
            practitioner.setPassword(hashPassword(dto.password));
        }

        practitionerRepository.persist(practitioner);
        return DTOMapper.toPractitionerDTO(practitioner, false);
    }

    @Transactional
    public boolean deletePractitioner(UUID practitionerId) {
        return practitionerRepository.deleteById(practitionerId);
    }

    public long countPractitionersByOrganization(UUID organizationId) {
        return practitionerRepository.countByOrganization(organizationId);
    }

    private void validateCreateDTO(PractitionerCreateDTO dto) {
        if (dto.fullName == null || dto.fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (dto.email == null || dto.email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.password == null || dto.password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (dto.organizationId == null) {
            throw new IllegalArgumentException("Organization ID is required");
        }
    }

    private String hashPassword(String password) {
        return password; // TODO: implement proper hashing
    }
}
