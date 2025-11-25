package api.controllers;

import api.dto.MessageDTO;
import api.dto.SessionDTO;
import core.services.MessageService;
import core.services.PatientService;
import core.services.SessionService;
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
public class MessageController {

    @Inject
    MessageService messageService;

    @Inject
    SessionService sessionService;

    // =======================
    // GET
    // =======================

    /** Get all messages in a session */
    @GET
    @Path("/messages/session/{sessionId}")
    public List<MessageDTO> getSessionMessages(@PathParam("sessionId") UUID sessionId) {
        return messageService.getSessionMessages(sessionId);
    }

    /** Get message by ID */
    @GET
    @Path("/messages/{messageId}")
    public MessageDTO getMessageById(@PathParam("messageId") UUID messageId) {
        return messageService.getMessageById(messageId);
    }

    /** Get latest message in a session */
    @GET
    @Path("/messages/latest/session/{sessionId}")
    public MessageDTO getLatestMessage(@PathParam("sessionId") UUID sessionId) {
        return messageService.getLatestMessage(sessionId);
    }

    /** Search messages by content */
    @GET
    @Path("/messages/search")
    public List<MessageDTO> searchMessages(@QueryParam("q") String searchTerm) {
        return messageService.searchMessages(searchTerm);
    }

    /** Get all sessions for a user */
    @GET
    @Path("/sessions/user/{userId}")
    public List<SessionDTO> getUserSessions(@PathParam("userId") UUID userId,
                                            @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.getUserSessions(userId, includeMessages);
    }

    /** Get session by ID */
    @GET
    @Path("/sessions/{sessionId}")
    public SessionDTO getSessionById(@PathParam("sessionId") UUID sessionId,
                                     @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.getSessionById(sessionId, includeMessages);
    }

    /** Get sessions between two users */
    @GET
    @Path("/sessions/between")
    public List<SessionDTO> getSessionsBetweenUsers(@QueryParam("user1") UUID user1,
                                                    @QueryParam("user2") UUID user2,
                                                    @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.getSessionsBetweenUsers(user1, user2, includeMessages);
    }

    /** Search sessions by subject */
    @GET
    @Path("/sessions/search")
    public List<SessionDTO> searchSessions(@QueryParam("q") String searchTerm,
                                           @QueryParam("includeMessages") @DefaultValue("false") boolean includeMessages) {
        return sessionService.searchSessionsBySubject(searchTerm, includeMessages);
    }

    // =======================
    // Count
    // =======================

    /** Count all sessions for a user */
    @GET
    @Path("/sessions/count/user/{userId}")
    public long countUserSessions(@PathParam("userId") UUID userId) {
        return sessionService.countUserSessions(userId);
    }

    /** Count messages in a session */
    @GET
    @Path("/messages/count/session/{sessionId}")
    public long countSessionMessages(@PathParam("sessionId") UUID sessionId) {
        return messageService.countSessionMessages(sessionId);
    }

    // =======================
    // POST
    // =======================

    /** Create a new message */
    @POST
    @Path("/messages")
    @Transactional
    public MessageDTO createMessage(MessageDTO dto) {
        return messageService.createMessage(dto);
    }

    /** Create a new session */
    @POST
    @Path("/sessions")
    @Transactional
    public SessionDTO createSession(SessionDTO dto) {
        return sessionService.createSession(dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete a message */
    @DELETE
    @Path("/messages/{messageId}")
    @Transactional
    public Response deleteMessage(@PathParam("messageId") UUID messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (deleted) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Message not found")
                    .build();
        }
    }

    /** Delete a session */
    @DELETE
    @Path("/sessions/{sessionId}")
    @Transactional
    public Response deleteSession(@PathParam("sessionId") UUID sessionId) {
        boolean deleted = sessionService.deleteSession(sessionId);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

}