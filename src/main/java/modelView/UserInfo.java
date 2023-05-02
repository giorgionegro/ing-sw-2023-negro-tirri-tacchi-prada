package modelView;

import model.User;

import java.io.Serializable;

public record UserInfo(User.Status status, String errorMessage) implements Serializable {}
