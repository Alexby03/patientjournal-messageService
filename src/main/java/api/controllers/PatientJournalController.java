package api.controllers;

import api.dto.*;
import core.enums.ConditionType;
import core.enums.LocationType;
import core.services.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientJournalController {

    @Inject
    UserService userService;

    @Inject
    PatientService patientService;

    @Inject
    PractitionerService practitionerService;

    @Inject
    OrganizationService organizationService;

    @Inject
    ObservationService observationService;

    @Inject
    LocationService locationService;

    @Inject
    EncounterService encounterService;

    @Inject
    ConditionService conditionService;

    // =======================
    // GET
    // =======================

    // Users =======================

    /** Get all users with pagination */
    @GET
    @Path("/users")
    public List<UserDTO> getAllUsers(@QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
                                     @QueryParam("pageSize") @DefaultValue("10") int pageSize) {
        return userService.getAllUsers(pageIndex, pageSize);
    }

    /** Get user by ID */
    @GET
    @Path("/users/{userId}")
    public UserDTO getUserById(@PathParam("userId") UUID userId) {
        UserDTO user = userService.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    /** Get user by email */
    @GET
    @Path("/users/email/{email}")
    public UserDTO getUserByEmail(@PathParam("email") String email) {
        UserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    /** Count total users */
    @GET
    @Path("/users/count")
    public long countUsers() {
        return userService.countUsers();
    }

    // Practitioners =======================

    /** Get all practitioners with pagination */
    @GET
    @Path("/practitioners")
    public List<PractitionerDTO> getAllPractitioners(
            @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize,
            @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getAllPatients(pageIndex, pageSize, fetchRelations);
    }

    /** Get practitioner by ID */
    @GET
    @Path("/practitioners/{practitionerId}")
    public PractitionerDTO getPractitionerById(@PathParam("practitionerId") UUID practitionerId,
                                               @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getPractitionerById(practitionerId, fetchRelations);
    }

    /** Get practitioners by organization */
    @GET
    @Path("/practitioners/organization/{organizationId}")
    public List<PractitionerDTO> getPractitionersByOrganization(@PathParam("organizationId") UUID organizationId,
                                                                @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getPractitionersByOrganization(organizationId, fetchRelations);
    }

    /** Get practitioner by email */
    @GET
    @Path("/practitioners/email/{email}")
    public PractitionerDTO getPractitionerByEmail(@PathParam("email") String email,
                                                  @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getPractitionerByEmail(email, fetchRelations);
    }

    // Patients =======================

    /** Get all patients with pagination */
    @GET
    @Path("/patients")
    public List<PatientDTO> getAllPatients(
            @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize,
            @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.getAllPatients(pageIndex, pageSize, fetchRelations);
    }

    /** Get patient by ID */
    @GET
    @Path("/patients/{patientId}")
    public PatientDTO getPatientById(@PathParam("patientId") UUID patientId,
                                     @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.getPatientById(patientId, fetchRelations);
    }

    /** Get patient by email */
    @GET
    @Path("/patients/email/{email}")
    public PatientDTO getPatientByEmail(@PathParam("email") String email,
                                        @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.getPatientByEmail(email, fetchRelations);
    }

    /** Search patients by name */
    @GET
    @Path("/patients/search")
    public List<PatientDTO> searchPatients(@QueryParam("q") String searchTerm,
                                           @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
                                           @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                                           @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.searchPatientsByName(searchTerm, pageIndex, pageSize, fetchRelations);
    }

    /** Count total patients */
    @GET
    @Path("/patients/count")
    public long countPatients() {
        return patientService.countPatients();
    }

    // Organizations =======================

    /** Get all organizations with pagination */
    @GET
    public List<OrganizationDTO> getAllOrganizations(@QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
                                                     @QueryParam("pageSize") @DefaultValue("10") int pageSize) {
        return organizationService.getAllOrganizations(pageIndex, pageSize);
    }

    /** Get organization by ID */
    @GET
    @Path("/organizations/{organizationId}")
    public OrganizationDTO getOrganizationById(@PathParam("organizationId") UUID organizationId) {
        return organizationService.getOrganizationById(organizationId);
    }

    /** Get organizations by type */
    @GET
    @Path("/organizations/type/{type}")
    public List<OrganizationDTO> getOrganizationsByType(@PathParam("type") String type) {
        return organizationService.getOrganizationsByType(core.enums.OrganizationType.valueOf(type));
    }

    /** Count total organizations */
    @GET
    @Path("/organizations/count")
    public long countOrganizations() {
        return organizationService.countOrganizations();
    }

    // Locations =======================

    /** Get all locations */
    @GET
    @Path("/locations")
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }

    /** Get location by ID */
    @GET
    @Path("/locations/{locationId}")
    public LocationDTO getLocationById(@PathParam("locationId") UUID locationId) {
        return locationService.getLocationById(locationId);
    }

    /** Get locations by type */
    @GET
    @Path("/locations/type/{type}")
    public LocationDTO getLocationByType(@PathParam("type") LocationType type) {
        return locationService.getLocationByType(type);
    }

    /** Count total locations */
    @GET
    @Path("/locations/count")
    public long countLocations() {
        return locationService.countLocations();
    }

    // Conditions =======================

    /** Get all conditions for a patient (optionally eager load) */
    @GET
    @Path("/conditions/patient/{patientId}")
    public List<ConditionDTO> getPatientConditions(
            @PathParam("patientId") UUID patientId,
            @QueryParam("eager") @DefaultValue("false") boolean eager
    ) {
        return conditionService.getPatientConditions(patientId, eager);
    }

    /** Get all conditions a practitioner assigned (optionally eager load) */
    @GET
    @Path("/conditions/practitioner/{practitionerId}")
    public List<ConditionDTO> getPractitionerConditions(
            @PathParam("practitionerId") UUID practitionerId
    ) {
        return conditionService.getPractitionerConditions(practitionerId);
    }

    /** Get condition by ID */
    @GET
    @Path("/conditions/{conditionId}")
    public ConditionDTO getConditionById(@PathParam("conditionId") UUID conditionId) {
        return conditionService.getConditionById(conditionId);
    }

    /** Get high severity conditions */
    @GET
    @Path("/conditions/high-severity")
    public List<ConditionDTO> getHighSeverityConditions() {
        return conditionService.getHighSeverityConditions();
    }

    /** Get conditions by type */
    @GET
    @Path("/conditions/type/{conditionType}")
    public List<ConditionDTO> getConditionsByType(@PathParam("conditionType") ConditionType conditionType) {
        return conditionService.getConditionsByType(conditionType);
    }

    /** Count conditions for a patient */
    @GET
    @Path("/conditions/patient/{patientId}/count")
    public long countPatientConditions(@PathParam("patientId") UUID patientId) {
        return conditionService.countPatientConditions(patientId);
    }

    // Encounters =======================

    /** Get all encounters for a patient (optionally eager) */
    @GET
    @Path("/encounters/patient/{patientId}")
    public List<EncounterDTO> getPatientEncounters(
            @PathParam("patientId") UUID patientId,
            @QueryParam("eager") @DefaultValue("false") boolean eager
    ) {
        return encounterService.getPatientEncounters(patientId, eager);
    }

    /** Get encounter by ID */
    @GET
    @Path("/encounters/{encounterId}")
    public EncounterDTO getEncounterById(@PathParam("encounterId") UUID encounterId) {
        return encounterService.getEncounterById(encounterId);
    }

    /** Get recent encounters */
    @GET
    @Path("/encounters/recent")
    public List<EncounterDTO> getRecentEncounters(@QueryParam("eager") @DefaultValue("false") boolean eager) {
        return encounterService.getRecentEncounters(eager);
    }

    /** Get all encounters for a practitioner */
    @GET
    @Path("/encounters/practitioner/{practitionerId}")
    public List<EncounterDTO> getPractitionerEncounters(
            @PathParam("practitionerId") UUID practitionerId,
            @QueryParam("eager") @DefaultValue("false") boolean eager
    ) {
        return encounterService.getPractitionerEncounters(practitionerId, eager);
    }

    /** Count encounters for a patient */
    @GET
    @Path("/count/patient/{patientId}")
    public long countPatientEncounters(@PathParam("patientId") UUID patientId) {
        return encounterService.countPatientEncounters(patientId);
    }

    // Observations =======================

    /** Get all observations for a patient */
    @GET
    @Path("/observations/patient/{patientId}")
    public List<ObservationDTO> getPatientObservations(@PathParam("patientId") UUID patientId) {
        return observationService.getPatientObservations(patientId);
    }

    /** Get observation by ID */
    @GET
    @Path("/observations/{observationId}")
    public ObservationDTO getObservationById(@PathParam("observationId") UUID observationId) {
        return observationService.getObservationById(observationId);
    }

    /** Get most recent observation for a patient */
    @GET
    @Path("/observations/recent/patient/{patientId}")
    public ObservationDTO getMostRecentObservation(@PathParam("patientId") UUID patientId) {
        return observationService.getMostRecentObservation(patientId);
    }

    /** Get observations by practitioner */
    @GET
    @Path("/observations/practitioner/{practitionerId}")
    public List<ObservationDTO> getPractitionerObservations(@PathParam("practitionerId") UUID practitionerId) {
        return observationService.getPractitionerObservations(practitionerId);
    }

    /** Count observations for a patient */
    @GET
    @Path("/observations/count/patient/{patientId}")
    public long countPatientObservations(@PathParam("patientId") UUID patientId) {
        return observationService.countPatientObservations(patientId);
    }

    // =======================
    // POST
    // =======================

    // Organizations =======================

    /** Create a new organization */
    @POST
    @Path("/organizations")
    @Transactional
    public OrganizationDTO createOrganization(OrganizationDTO dto) {
        return organizationService.createOrganization(dto);
    }

    // Locations =======================

    /** Create a new location */
    @POST
    @Path("/locations")
    @Transactional
    public LocationDTO createLocation(LocationDTO dto) {
        return locationService.createLocation(dto);
    }

    // Conditions =======================

    /** Create new condition for patient and practitioner */
    @POST
    @Path("conditions/patient/{patientId}/practitioner/{practitionerId}")
    @Transactional
    public ConditionDTO createCondition(
            @PathParam("patientId") UUID patientId,
            @PathParam("practitionerId") UUID practitionerId,
            ConditionDTO dto
    ) {
        return conditionService.createCondition(patientId, practitionerId, dto);
    }

    // Encounters =======================

    /** Create new encounter */
    @POST
    @Path("encounters/patient/{patientId}/practitioner/{practitionerId}")
    @Transactional
    public EncounterDTO createEncounter(
            @PathParam("patientId") UUID patientId,
            @PathParam("practitionerId") UUID practitionerId,
            EncounterDTO dto
    ) {
        return encounterService.createEncounter(patientId, practitionerId, dto);
    }

    // Observations =======================

    /** Create a new observation */
    @POST
    @Path("/observations/patient/{patientId}/practitioner/{practitionerId}")
    @Transactional
    public ObservationDTO createObservation(@PathParam("patientId") UUID patientId,
                                            @PathParam("practitionerId") UUID practitionerId,
                                            ObservationDTO dto) {
        return observationService.createObservation(patientId, practitionerId, dto);
    }

    // =======================
    // PUT
    // =======================

    // Organizations =======================

    /** Update an existing organization */
    @PUT
    @Path("organizations/{organizationId}")
    @Transactional
    public OrganizationDTO updateOrganization(@PathParam("organizationId") UUID organizationId,
                                              OrganizationDTO dto) {
        return organizationService.updateOrganization(organizationId, dto);
    }

    // Locations =======================

    /** Update existing location */
    @PUT
    @Path("locations/{locationId}")
    @Transactional
    public LocationDTO updateLocation(@PathParam("locationId") UUID locationId,
                                      LocationDTO dto) {
        return locationService.updateLocation(locationId, dto);
    }

    // Conditions =======================

    /** Update existing condition */
    @PUT
    @Path("conditions/{conditionId}")
    @Transactional
    public ConditionDTO updateCondition(
            @PathParam("conditionId") UUID conditionId,
            ConditionDTO dto
    ) {
        return conditionService.updateCondition(conditionId, dto);
    }

    // Encounters =======================

    /** Update encounter */
    @PUT
    @Path("encounters/{encounterId}")
    @Transactional
    public EncounterDTO updateEncounter(
            @PathParam("encounterId") UUID encounterId,
            EncounterDTO dto
    ) {
        return encounterService.updateEncounter(encounterId, dto);
    }

    // Observations =======================

    /** Update an existing observation */
    @PUT
    @Path("/observations/{observationId}")
    @Transactional
    public ObservationDTO updateObservation(@PathParam("observationId") UUID observationId,
                                            ObservationDTO dto) {
        return observationService.updateObservation(observationId, dto);
    }

    // =======================
    // DELETE
    // =======================

    // Organizations =======================



    // Locations =======================

    /** Delete location */
    @DELETE
    @Path("/{locationId}")
    @Transactional
    public Response deleteLocation(@PathParam("locationId") UUID locationId) {
        boolean deleted = locationService.deleteLocation(locationId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Location not found")
                    .build();
        }
    }

    // Conditions =======================

    /** Delete condition by ID */
    @DELETE
    @Path("conditions/{conditionId}")
    @Transactional
    public Response deleteCondition(@PathParam("conditionId") UUID conditionId) {
        boolean deleted = conditionService.deleteCondition(conditionId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Condition not found")
                    .build();
        }
    }

    // Encounters =======================

    /** Delete encounter */
    @DELETE
    @Path("encounters/{encounterId}")
    @Transactional
    public Response deleteEncounter(@PathParam("encounterId") UUID encounterId) {
        boolean deleted = encounterService.deleteEncounter(encounterId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Encounter not found")
                    .build();
        }
    }

    // Observations =======================

    /** Delete an observation */
    @DELETE
    @Path("/observations/{observationId}")
    @Transactional
    public Response deleteObservation(@PathParam("observationId") UUID observationId) {
        boolean deleted = observationService.deleteObservation(observationId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Observation not found")
                    .build();
        }
    }

}