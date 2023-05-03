package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void constructorWorksCorrectly(){
        User test = new User();
        assertEquals(test.getStatus(), User.Status.NOT_JOINED);
        assertEquals(test.getReportedError(), "");
    }
    @Test
    void setStatusTest(){
        User test = new User();
        assertEquals(test.getStatus(), User.Status.NOT_JOINED);
        test.setStatus(User.Status.JOINED);
        assertEquals(test.getStatus(), User.Status.JOINED);
    }
    @Test
    void ReportErrorTest(){
        User test = new User();
        assertEquals(test.getReportedError(), "");
        test.reportError("New error");
        assertEquals(test.getReportedError(), "New error");
    }
}