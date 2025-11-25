package api.controllers;

import api.dto.UserCreateDTO;
import api.dto.UserDTO;
import api.dto.UserLoginDTO;
import core.services.UserService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    // =======================
    // GET
    // =======================

    /** Get all users with pagination */
    @GET
    public List<UserDTO> getAllUsers(@QueryParam("pageIndex") @DefaultValue("0") int pageIndex,
                                     @QueryParam("pageSize") @DefaultValue("10") int pageSize) {
        return userService.getAllUsers(pageIndex, pageSize);
    }

    /** Get user by ID */
    @GET
    @Path("/{userId}")
    public UserDTO getUserById(@PathParam("userId") UUID userId) {
        UserDTO user = userService.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    /** Get user by email */
    @GET
    @Path("/email/{email}")
    public UserDTO getUserByEmail(@PathParam("email") String email) {
        UserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    /** Count total users */
    @GET
    @Path("/count")
    public long countUsers() {
        return userService.countUsers();
    }

    // =======================
    // POST
    // =======================

    /** Create a new user */
    @POST
    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        return userService.createUser(dto);
    }

    /** User login */
    @POST
    @Path("/login")
    public UserDTO login(UserLoginDTO dto) {
        return userService.login(dto.email, dto.password);
    }

    // =======================
    // PUT
    // =======================

    /** Update user details */
    @PUT
    @Path("/{userId}")
    @Transactional
    public UserDTO updateUser(@PathParam("userId") UUID userId, UserCreateDTO dto) {
        return userService.updateUser(userId, dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete a user */
    @DELETE
    @Path("/{userId}")
    @Transactional
    public Response deleteUser(@PathParam("userId") UUID userId) {
        boolean deleted = userService.deleteUser(userId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
