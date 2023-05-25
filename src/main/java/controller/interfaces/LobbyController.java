package controller.interfaces;

import controller.exceptions.GameAccessDeniedException;
import distibuted.interfaces.ClientInterface;
import model.User;
import modelView.LoginInfo;

public interface LobbyController {
    void joinPlayer(ClientInterface newClient, User user, LoginInfo info) throws GameAccessDeniedException;
    void leavePlayer(ClientInterface client) throws GameAccessDeniedException;
}
