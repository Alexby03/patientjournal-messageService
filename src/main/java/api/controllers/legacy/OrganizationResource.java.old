package api.controllers;

import api.dto.OrganizationDTO;
import core.services.OrganizationService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.UUID;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    @Inject
    OrganizationService organizationService;

    // =======================
    // GET
    // =======================

    /** Get all organizations with pagination */
    @GET
    public List<OrganizationDTO> getAllOrganizations(@QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
                                                     @QueryParam("pageSize") @DefaultValue("10") int pageSize) {
        return organizationService.getAllOrganizations(pageIndex, pageSize);
    }

    /** Get organization by ID */
    @GET
    @Path("/{organizationId}")
    public OrganizationDTO getOrganizationById(@PathParam("organizationId") UUID organizationId) {
        return organizationService.getOrganizationById(organizationId);
    }

    /** Get organizations by type */
    @GET
    @Path("/type/{type}")
    public List<OrganizationDTO> getOrganizationsByType(@PathParam("type") String type) {
        return organizationService.getOrganizationsByType(core.enums.OrganizationType.valueOf(type));
    }

    /** Count total organizations */
    @GET
    @Path("/count")
    public long countOrganizations() {
        return organizationService.countOrganizations();
    }

    // =======================
    // POST
    // =======================

    /** Create a new organization */
    @POST
    @Transactional
    public OrganizationDTO createOrganization(OrganizationDTO dto) {
        return organizationService.createOrganization(dto);
    }

    // =======================
    // PUT
    // =======================

    /** Update an existing organization */
    @PUT
    @Path("/{organizationId}")
    @Transactional
    public OrganizationDTO updateOrganization(@PathParam("organizationId") UUID organizationId,
                                              OrganizationDTO dto) {
        return organizationService.updateOrganization(organizationId, dto);
    }
}
