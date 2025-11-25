package api.controllers;

import api.dto.PractitionerCreateDTO;
import api.dto.PractitionerDTO;
import core.services.PractitionerService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/practitioners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PractitionerResource {

    @Inject
    PractitionerService practitionerService;

    // =======================
    // GET
    // =======================

    /** Get all practitioners with pagination */
    @GET
    public List<PractitionerDTO> getAllPractitioners(
            @QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize,
            @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getAllPatients(pageIndex, pageSize, fetchRelations);
    }

    /** Get practitioner by ID */
    @GET
    @Path("/{practitionerId}")
    public PractitionerDTO getPractitionerById(@PathParam("practitionerId") UUID practitionerId,
                                               @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getPractitionerById(practitionerId, fetchRelations);
    }

    /** Get practitioners by organization */
    @GET
    @Path("/organization/{organizationId}")
    public List<PractitionerDTO> getPractitionersByOrganization(@PathParam("organizationId") UUID organizationId,
                                                                @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getPractitionersByOrganization(organizationId, fetchRelations);
    }

    /** Get practitioner by email */
    @GET
    @Path("/email/{email}")
    public PractitionerDTO getPractitionerByEmail(@PathParam("email") String email,
                                                  @QueryParam("fetchRelations") @DefaultValue("false") boolean fetchRelations) {
        return practitionerService.getPractitionerByEmail(email, fetchRelations);
    }

    // =======================
    // POST
    // =======================

    /** Create a new practitioner */
    @POST
    @Transactional
    public PractitionerDTO createPractitioner(PractitionerCreateDTO dto) {
        return practitionerService.createPractitioner(dto);
    }

    // =======================
    // PUT
    // =======================

    /** Update existing practitioner */
    @PUT
    @Path("/{practitionerId}")
    @Transactional
    public PractitionerDTO updatePractitioner(@PathParam("practitionerId") UUID practitionerId,
                                              PractitionerCreateDTO dto) {
        return practitionerService.updatePractitioner(practitionerId, dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete practitioner */
    @DELETE
    @Path("/{practitionerId}")
    @Transactional
    public Response deletePractitioner(@PathParam("practitionerId") UUID practitionerId) {
        boolean deleted = practitionerService.deletePractitioner(practitionerId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
