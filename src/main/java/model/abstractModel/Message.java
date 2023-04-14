package model.abstractModel;

/**
 * This interface define the method to access to message data
 */
public interface Message {
    /**
     * This enumeration contains the subject to which the message has been sent
     */
    enum MessageSubject {
        /**
         * This subject means that the message has been sent to everybody is playing the same game
         */
        BROADCAST("");
        /**
         * The id of the player to which the message has been sent
         */
        private final String subjectId;

        /**
         * Construct a MessageSubject with the given subjectId
         * @param subjectId the id of the subject of the message
         */
        MessageSubject(String subjectId){
            this.subjectId = subjectId;
        }

        /**
         * This method returns the id of the subject of the message
         * @return the id of the subject of the message
         */
        public String getSubjectId() {
            return subjectId;
        }
    }

    /**
     * This method returns the id of the sender of the message
     * @return the id of the sender of the message
     */
    String getSender();

    /**
     * This method returns the MessageSubject of the message
     * @return the MessageSubject of the message
     */
    MessageSubject getSubject();

    /**
     * This method returns the text-content of the message
     * @return the text-content of the message
     */
    String getText();

    /**
     * This method returns the string representation of the timestamp when message has been created
     * @return the string representation of the timestamp when message has been created
     */
    String getTimestamp();
}
