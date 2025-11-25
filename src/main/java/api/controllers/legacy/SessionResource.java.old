package api.controllers;

import api.dto.SessionDTO;
import core.services.SessionService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/sessions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SessionResource {

    @Inject
    SessionService sessionService;

    // =======================
    // GET
    // =======================

    /** Get all sessions for a user */
    @GET
    @Path("/user/{userId}")
    public List<SessionDTO> getUserSessions(@PathParam("userId") UUID userId,
                                            @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.getUserSessions(userId, includeMessages);
    }

    /** Get session by ID */
    @GET
    @Path("/{sessionId}")
    public SessionDTO getSessionById(@PathParam("sessionId") UUID sessionId,
                                     @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.getSessionById(sessionId, includeMessages);
    }

    /** Get sessions between two users */
    @GET
    @Path("/between")
    public List<SessionDTO> getSessionsBetweenUsers(@QueryParam("user1") UUID user1,
                                                    @QueryParam("user2") UUID user2,
                                                    @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.getSessionsBetweenUsers(user1, user2, includeMessages);
    }

    /** Search sessions by subject */
    @GET
    @Path("/search")
    public List<SessionDTO> searchSessions(@QueryParam("q") String searchTerm,
                                           @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.searchSessionsBySubject(searchTerm, includeMessages);
    }

    // =======================
    // POST
    // =======================

    /** Create a new session */
    @POST
    @Transactional
    public SessionDTO createSession(SessionDTO dto) {
        return sessionService.createSession(dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete a session */
    @DELETE
    @Path("/{sessionId}")
    @Transactional
    public Response deleteSession(@PathParam("sessionId") UUID sessionId) {
        boolean deleted = sessionService.deleteSession(sessionId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    // =======================
    // Count
    // =======================

    /** Count all sessions for a user */
    @GET
    @Path("/count/user/{userId}")
    public long countUserSessions(@PathParam("userId") UUID userId) {
        return sessionService.countUserSessions(userId);
    }
}
