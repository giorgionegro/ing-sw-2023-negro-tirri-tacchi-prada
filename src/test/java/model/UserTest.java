package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void constructorWorksCorrectly(){
        User test = new User();
        assertEquals(test.getStatus(), User.Status.NOT_JOINED);
        assertEquals(test.getEventMessage(), "");
    }
    @Test
    void setStatusTest(){
        User test = new User();
        assertEquals(test.getStatus(), User.Status.NOT_JOINED);
        test.reportEvent(User.Status.JOINED,"message",2000, User.Event.GAME_JOINED);
        assertEquals(test.getStatus(), User.Status.JOINED);
    }
    @Test
    void ReportErrorTest(){
        User test = new User();
        assertEquals(test.getEventMessage(), "");
        test.reportEvent(User.Status.NOT_JOINED,"New error",0, User.Event.ERROR_REPORTED);
        assertEquals(test.getEventMessage(), "New error");
    }

    @Test
    void infoTest(){
        User test = new User();
        assertTrue(test.getStatus().equals(test.getInfo().status()) && test.getStatus().equals(test.getInfo().status()) && test.getEventMessage().equals(test.getInfo().eventMessage()));
    }
}