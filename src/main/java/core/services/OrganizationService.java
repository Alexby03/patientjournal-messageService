package core.services;

import api.dto.OrganizationDTO;
import core.mappers.DTOMapper;
import data.entities.Location;
import data.entities.Organization;
import data.repositories.LocationRepository;
import data.repositories.OrganizationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrganizationService {

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    LocationRepository locationRepository;

    public List<OrganizationDTO> getAllOrganizations(int pageIndex, int pageSize) {
        List<Organization> organizations = organizationRepository.findAllOrganizations(pageIndex, pageSize);
        return organizations.stream()
                .map(DTOMapper::toOrganizationDTO)
                .collect(Collectors.toList());
    }

    public OrganizationDTO getOrganizationById(UUID organizationId) {
        Organization organization = organizationRepository.findById(organizationId);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }
        return DTOMapper.toOrganizationDTO(organization);
    }

    public List<OrganizationDTO> getOrganizationsByType(core.enums.OrganizationType organizationType) {
        List<Organization> organizations = organizationRepository.findByOrganizationType(organizationType);
        return organizations.stream()
                .map(DTOMapper::toOrganizationDTO)
                .collect(Collectors.toList());
    }

    public long countOrganizations() {
        return organizationRepository.countTotalOrganizations();
    }


    @Transactional
    public OrganizationDTO createOrganization(OrganizationDTO dto) {
        if (dto.organizationType == null) {
            throw new IllegalArgumentException("Organization type is required");
        }
        if (dto.organizationId == null) {
            throw new IllegalArgumentException("Location ID is required");
        }

        Location location = locationRepository.findByLocationType(dto.locationType);
        if (location == null) {
            throw new IllegalArgumentException("Location not found");
        }

        Organization organization = new Organization(dto.organizationType, location);
        organizationRepository.persist(organization);

        return DTOMapper.toOrganizationDTO(organization);
    }


    @Transactional
    public OrganizationDTO updateOrganization(UUID organizationId, OrganizationDTO dto) {
        Organization organization = organizationRepository.findById(organizationId);
        if (organization == null) {
            throw new IllegalArgumentException("Organization not found");
        }

        if (dto.organizationType != null) {
            organization.setOrganizationType(dto.organizationType);
        }

        if (dto.locationType != null) {
            Location location = locationRepository.findByLocationType(dto.locationType);
            if (location == null) {
                throw new IllegalArgumentException("Location not found");
            }
            organization.setLocation(location);
        }

        organizationRepository.persist(organization);
        return DTOMapper.toOrganizationDTO(organization);
    }
}