package modelView;

import model.User;

import java.io.Serializable;

/**
 * This record contains information about the state of a {@link model.User} on the server
 * @param status the status of the user
 * @param eventMessage message about an event happened during interaction with the server
 * @param joinTime the timestamp of the last accepted join request
 */
public record UserInfo(User.Status status, String eventMessage, long joinTime) implements Serializable {}
