package core.services;

import api.dto.LocationDTO;
import core.mappers.DTOMapper;
import data.entities.Location;
import data.repositories.LocationRepository;
import core.enums.LocationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class LocationService {

    @Inject
    LocationRepository locationRepository;

    public List<LocationDTO> getAllLocations() {
        List<Location> locations = locationRepository.findAllLocations();
        return locations.stream()
                .map(DTOMapper::toLocationDTO)
                .collect(Collectors.toList());
    }

    public LocationDTO getLocationById(UUID locationId) {
        Location location = locationRepository.findById(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found");
        }
        return DTOMapper.toLocationDTO(location);
    }

    public LocationDTO getLocationByType(LocationType locationType) {
        Location location = locationRepository.findByLocationType(locationType);
        return DTOMapper.toLocationDTO(location);
    }

    public long countLocations() {
        return locationRepository.countTotalLocations();
    }

    @Transactional
    public LocationDTO createLocation(LocationDTO dto) {
        if (dto.locationType == null) {
            throw new IllegalArgumentException("Location type is required");
        }

        Location location = new Location();
        location.setLocationType(dto.locationType);
        locationRepository.persist(location);

        return DTOMapper.toLocationDTO(location);
    }

    @Transactional
    public LocationDTO updateLocation(UUID locationId, LocationDTO dto) {
        Location location = locationRepository.findById(locationId);
        if (location == null) {
            throw new IllegalArgumentException("Location not found");
        }

        if (dto.locationType != null) {
            location.setLocationType(dto.locationType);
        }

        locationRepository.persist(location);
        return DTOMapper.toLocationDTO(location);
    }

    @Transactional
    public boolean deleteLocation(UUID locationId) {
        return locationRepository.deleteById(locationId);
    }
}
