package api.controllers;

import api.dto.*;
import core.services.PatientService;
import core.services.PractitionerService;
import core.services.UserService;
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
public class UserController {

    @Inject
    UserService userService;

    @Inject
    PatientService patientService;

    @Inject
    PractitionerService practitionerService;

    // =======================
    // POST
    // =======================

    /** Create a new user */
    @POST
    @Path("/users")
    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        return userService.createUser(dto);
    }

    /** User login */
    @POST
    @Path("/users/login")
    public UserDTO login(UserLoginDTO dto) {
        return userService.login(dto.email, dto.password);
    }

    /** Create a new practitioner */
    @POST
    @Path("/practitioners")
    @Transactional
    public PractitionerDTO createPractitioner(PractitionerCreateDTO dto) {
        return practitionerService.createPractitioner(dto);
    }

    /** Create a new patient */
    @POST
    @Path("/patients")
    @Transactional
    public PatientDTO createPatient(PatientCreateDTO dto) {
        return patientService.createPatient(dto);
    }

    // =======================
    // PUT
    // =======================

    /** Update user details */
    @PUT
    @Path("/users/{userId}")
    @Transactional
    public UserDTO updateUser(@PathParam("userId") UUID userId, UserCreateDTO dto) {
        return userService.updateUser(userId, dto);
    }

    /** Update existing practitioner */
    @PUT
    @Path("/practitioners/{practitionerId}")
    @Transactional
    public PractitionerDTO updatePractitioner(@PathParam("practitionerId") UUID practitionerId,
                                              PractitionerCreateDTO dto) {
        return practitionerService.updatePractitioner(practitionerId, dto);
    }

    /** Update existing patient */
    @PUT
    @Path("/patients/{patientId}")
    @Transactional
    public PatientDTO updatePatient(@PathParam("patientId") UUID patientId, PatientCreateDTO dto) {
        return patientService.updatePatient(patientId, dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete a user */
    @DELETE
    @Path("/users/{userId}")
    @Transactional
    public Response deleteUser(@PathParam("userId") UUID userId) {
        boolean deleted = userService.deleteUser(userId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /** Delete practitioner */
    @DELETE
    @Path("/practitioners/{practitionerId}")
    @Transactional
    public Response deletePractitioner(@PathParam("practitionerId") UUID practitionerId) {
        boolean deleted = practitionerService.deletePractitioner(practitionerId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    /** Delete patient */
    @DELETE
    @Path("/patients/{patientId}")
    @Transactional
    public Response deletePatient(@PathParam("patientId") UUID patientId) {
        boolean deleted = patientService.deletePatient(patientId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

}