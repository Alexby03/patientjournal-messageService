package api.controllers;

import api.dto.MessageDTO;
import core.services.MessageService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResource {

    @Inject
    MessageService messageService;

    // =======================
    // GET
    // =======================

    /** Get all messages in a session */
    @GET
    @Path("/session/{sessionId}")
    public List<MessageDTO> getSessionMessages(@PathParam("sessionId") UUID sessionId) {
        return messageService.getSessionMessages(sessionId);
    }

    /** Get message by ID */
    @GET
    @Path("/{messageId}")
    public MessageDTO getMessageById(@PathParam("messageId") UUID messageId) {
        return messageService.getMessageById(messageId);
    }

    /** Get latest message in a session */
    @GET
    @Path("/latest/session/{sessionId}")
    public MessageDTO getLatestMessage(@PathParam("sessionId") UUID sessionId) {
        return messageService.getLatestMessage(sessionId);
    }

    /** Count messages in a session */
    @GET
    @Path("/count/session/{sessionId}")
    public long countSessionMessages(@PathParam("sessionId") UUID sessionId) {
        return messageService.countSessionMessages(sessionId);
    }

    /** Search messages by content */
    @GET
    @Path("/search")
    public List<MessageDTO> searchMessages(@QueryParam("q") String searchTerm) {
        return messageService.searchMessages(searchTerm);
    }

    // =======================
    // POST
    // =======================

    /** Create a new message */
    @POST
    @Transactional
    public MessageDTO createMessage(MessageDTO dto) {
        return messageService.createMessage(dto);
    }

    // =======================
    // DELETE
    // =======================

    /** Delete a message */
    @DELETE
    @Path("/{messageId}")
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
}
