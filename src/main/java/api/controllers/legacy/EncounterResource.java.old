package api.controllers;

import api.dto.EncounterDTO;
import core.services.EncounterService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/encounters")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EncounterResource {

    @Inject
    EncounterService encounterService;

    // =======================
    // GET
    // =======================

    /** Get all encounters for a patient (optionally eager) */
    @GET
    @Path("/patient/{patientId}")
    public List<EncounterDTO> getPatientEncounters(
            @PathParam("patientId") UUID patientId,
            @QueryParam("eager") @DefaultValue("false") boolean eager
    ) {
        return encounterService.getPatientEncounters(patientId, eager);
    }

    /** Get encounter by ID */
    @GET
    @Path("/{encounterId}")
    public EncounterDTO getEncounterById(@PathParam("encounterId") UUID encounterId) {
        return encounterService.getEncounterById(encounterId);
    }

    /** Get recent encounters */
    @GET
    @Path("/recent")
    public List<EncounterDTO> getRecentEncounters(@QueryParam("eager") @DefaultValue("false") boolean eager) {
        return encounterService.getRecentEncounters(eager);
    }

    /** Get all encounters for a practitioner */
    @GET
    @Path("/practitioner/{practitionerId}")
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

    // =======================
    // POST
    // =======================

    /** Create new encounter */
    @POST
    @Path("/patient/{patientId}/practitioner/{practitionerId}")
    @Transactional
    public EncounterDTO createEncounter(
            @PathParam("patientId") UUID patientId,
            @PathParam("practitionerId") UUID practitionerId,
            EncounterDTO dto
    ) {
        return encounterService.createEncounter(patientId, practitionerId, dto);
    }

    // =======================
    // PUT
    // =======================

    /** Update encounter */
    @PUT
    @Path("/{encounterId}")
    @Transactional
    public EncounterDTO updateEncounter(
            @PathParam("encounterId") UUID encounterId,
            EncounterDTO dto
    ) {
        return encounterService.updateEncounter(encounterId, dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete encounter */
    @DELETE
    @Path("/{encounterId}")
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
}
