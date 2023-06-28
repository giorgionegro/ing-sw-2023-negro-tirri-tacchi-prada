package view.graphicInterfaces;

import model.abstractModel.Message;

import java.util.List;

/**
 * This interface represents the graphics for the user's chat.
 * It provides a method to update the information of the chat when users communicate with each other by sending messages.
 */
@FunctionalInterface
public interface PlayerChatGraphics {
    /**
     * This method updates the information of the chat with the specified parameter:
     *
     * @param chat list of messages sent to the player
     */
    void updatePlayerChatGraphics(List<? extends Message> chat);
}
