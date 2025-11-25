package api.controllers;

import api.dto.PatientCreateDTO;
import api.dto.PatientDTO;
import core.services.PatientService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientResource {

    @Inject
    PatientService patientService;

    // =======================
    // GET
    // =======================

    /** Get all patients with pagination */
    @GET
    public List<PatientDTO> getAllPatients(
            @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize,
            @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.getAllPatients(pageIndex, pageSize, fetchRelations);
    }

    /** Get patient by ID */
    @GET
    @Path("/{patientId}")
    public PatientDTO getPatientById(@PathParam("patientId") UUID patientId,
                                     @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.getPatientById(patientId, fetchRelations);
    }

    /** Get patient by email */
    @GET
    @Path("/email/{email}")
    public PatientDTO getPatientByEmail(@PathParam("email") String email,
                                        @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.getPatientByEmail(email, fetchRelations);
    }

    /** Search patients by name */
    @GET
    @Path("/search")
    public List<PatientDTO> searchPatients(@QueryParam("q") String searchTerm,
                                           @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
                                           @QueryParam("pageSize") @DefaultValue("10") int pageSize,
                                           @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return patientService.searchPatientsByName(searchTerm, pageIndex, pageSize, fetchRelations);
    }

    /** Count total patients */
    @GET
    @Path("/count")
    public long countPatients() {
        return patientService.countPatients();
    }

    // =======================
    // POST
    // =======================

    /** Create a new patient */
    @POST
    @Transactional
    public PatientDTO createPatient(PatientCreateDTO dto) {
        return patientService.createPatient(dto);
    }

    // =======================
    // PUT
    // =======================

    /** Update existing patient */
    @PUT
    @Path("/{patientId}")
    @Transactional
    public PatientDTO updatePatient(@PathParam("patientId") UUID patientId, PatientCreateDTO dto) {
        return patientService.updatePatient(patientId, dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete patient */
    @DELETE
    @Path("/{patientId}")
    @Transactional
    public Response deletePatient(@PathParam("patientId") UUID patientId) {
        boolean deleted = patientService.deletePatient(patientId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
