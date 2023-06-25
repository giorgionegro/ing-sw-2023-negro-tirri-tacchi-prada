package model;

import modelView.UserInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
    /**
     * Method under test: {@link User#User()}
     */
    @Test
    void constructorWorksCorrectly() {
        User test = new User();
        assertEquals(test.getStatus(), User.Status.NOT_JOINED);
        assertEquals(test.getInfo().eventMessage(), "");
    }

    /**
     * test: {@link User#getInfo()}
     */
    @Test
    void testGetInfo() {
        UserInfo actualInfo = (new User()).getInfo();
        assertEquals("", actualInfo.eventMessage());
        assertEquals(User.Status.NOT_JOINED, actualInfo.status());
        assertEquals(-1L, actualInfo.sessionID());
    }

    /**
     * Method under test: {@link User#reportEvent(User.Status, String, User.Event, long)}
     */
    @Test
    void setStatusTest() {
        User test = new User();
        assertEquals(test.getStatus(), User.Status.NOT_JOINED);
        test.reportEvent(User.Status.JOINED, "message", User.Event.GAME_JOINED,2000);
        assertEquals(test.getStatus(), User.Status.JOINED);
    }

    /**
     * Method under test: {@link User#reportEvent(User.Status, String, User.Event, long)}
     */
    @Test
    void ReportErrorTest() {
        User test = new User();
        assertEquals(test.getInfo().eventMessage(), "");
        test.reportEvent(User.Status.NOT_JOINED, "New error", User.Event.ERROR_REPORTED,0);
        assertEquals(test.getInfo().eventMessage(), "New error");
    }

    /**
     * Method under test: {@link User#getInfo()}
     */
    @Test
    void infoTest(){
        User test = new User();
        assertTrue(test.getStatus().equals(test.getInfo().status()) && test.getStatus().equals(test.getInfo().status()) && test.getInfo().eventMessage().equals(test.getInfo().eventMessage()));
    }
}