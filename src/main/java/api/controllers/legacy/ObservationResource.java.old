package api.controllers;

import api.dto.ObservationDTO;
import core.services.ObservationService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/observations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ObservationResource {

    @Inject
    ObservationService observationService;

    // =======================
    // GET
    // =======================

    /** Get all observations for a patient */
    @GET
    @Path("/patient/{patientId}")
    public List<ObservationDTO> getPatientObservations(@PathParam("patientId") UUID patientId) {
        return observationService.getPatientObservations(patientId);
    }

    /** Get observation by ID */
    @GET
    @Path("/{observationId}")
    public ObservationDTO getObservationById(@PathParam("observationId") UUID observationId) {
        return observationService.getObservationById(observationId);
    }

    /** Get most recent observation for a patient */
    @GET
    @Path("/recent/patient/{patientId}")
    public ObservationDTO getMostRecentObservation(@PathParam("patientId") UUID patientId) {
        return observationService.getMostRecentObservation(patientId);
    }

    /** Get observations by practitioner */
    @GET
    @Path("/practitioner/{practitionerId}")
    public List<ObservationDTO> getPractitionerObservations(@PathParam("practitionerId") UUID practitionerId) {
        return observationService.getPractitionerObservations(practitionerId);
    }

    /** Count observations for a patient */
    @GET
    @Path("/count/patient/{patientId}")
    public long countPatientObservations(@PathParam("patientId") UUID patientId) {
        return observationService.countPatientObservations(patientId);
    }

    // =======================
    // POST
    // =======================

    /** Create a new observation */
    @POST
    @Path("/patient/{patientId}/practitioner/{practitionerId}")
    @Transactional
    public ObservationDTO createObservation(@PathParam("patientId") UUID patientId,
                                            @PathParam("practitionerId") UUID practitionerId,
                                            ObservationDTO dto) {
        return observationService.createObservation(patientId, practitionerId, dto);
    }

    // =======================
    // PUT
    // =======================

    /** Update an existing observation */
    @PUT
    @Path("/{observationId}")
    @Transactional
    public ObservationDTO updateObservation(@PathParam("observationId") UUID observationId,
                                            ObservationDTO dto) {
        return observationService.updateObservation(observationId, dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete an observation */
    @DELETE
    @Path("/{observationId}")
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
