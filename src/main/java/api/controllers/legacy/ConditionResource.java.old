package api.controllers;

import api.dto.ConditionDTO;
import core.enums.ConditionType;
import core.services.ConditionService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/conditions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConditionResource {

    @Inject
    ConditionService conditionService;

    // =======================
    // GET
    // =======================

    /** Get all conditions for a patient (optionally eager load) */
    @GET
    @Path("/patient/{patientId}")
    public List<ConditionDTO> getPatientConditions(
            @PathParam("patientId") UUID patientId,
            @QueryParam("eager") @DefaultValue("false") boolean eager
    ) {
        return conditionService.getPatientConditions(patientId, eager);
    }

    /** Get all conditions a practitioner assigned (optionally eager load) */
    @GET
    @Path("/practitioner/{practitionerId}")
    public List<ConditionDTO> getPractitionerConditions(
            @PathParam("practitionerId") UUID practitionerId
    ) {
        return conditionService.getPractitionerConditions(practitionerId);
    }

    /** Get condition by ID */
    @GET
    @Path("/{conditionId}")
    public ConditionDTO getConditionById(@PathParam("conditionId") UUID conditionId) {
        return conditionService.getConditionById(conditionId);
    }

    /** Get high severity conditions */
    @GET
    @Path("/high-severity")
    public List<ConditionDTO> getHighSeverityConditions() {
        return conditionService.getHighSeverityConditions();
    }

    /** Get conditions by type */
    @GET
    @Path("/type/{conditionType}")
    public List<ConditionDTO> getConditionsByType(@PathParam("conditionType") ConditionType conditionType) {
        return conditionService.getConditionsByType(conditionType);
    }

    /** Count conditions for a patient */
    @GET
    @Path("/patient/{patientId}/count")
    public long countPatientConditions(@PathParam("patientId") UUID patientId) {
        return conditionService.countPatientConditions(patientId);
    }

    // =======================
    // POST
    // =======================

    /** Create new condition for patient and practitioner */
    @POST
    @Path("/patient/{patientId}/practitioner/{practitionerId}")
    @Transactional
    public ConditionDTO createCondition(
            @PathParam("patientId") UUID patientId,
            @PathParam("practitionerId") UUID practitionerId,
            ConditionDTO dto
    ) {
        return conditionService.createCondition(patientId, practitionerId, dto);
    }

    // =======================
    // PUT
    // =======================

    /** Update existing condition */
    @PUT
    @Path("/{conditionId}")
    @Transactional
    public ConditionDTO updateCondition(
            @PathParam("conditionId") UUID conditionId,
            ConditionDTO dto
    ) {
        return conditionService.updateCondition(conditionId, dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete condition by ID */
    @DELETE
    @Path("/{conditionId}")
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
}
