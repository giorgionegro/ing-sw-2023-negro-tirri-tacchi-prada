package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import util.TimedLock;

class TimedLockTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link TimedLock#TimedLock(Object)}
     *   <li>{@link TimedLock#setValue(Object)}
     *   <li>{@link TimedLock#reset()}
     *   <li>{@link TimedLock#hasBeenNotified()}
     * </ul>
     *
     * <p>Reason: the methods are closely related and can be tested together.
     *
     */
    @Test
    void testGeneral() {
        TimedLock<Object> actualTimedLock = new TimedLock<>("Default Value");
        actualTimedLock.setValue("Value");
        //check that the value is set correctly
        assertEquals("Value", actualTimedLock.getValue());
        actualTimedLock.reset();
        //check that the value is reset to the default value
        assertFalse(actualTimedLock.hasBeenNotified());
    }

    /**
     * Method under test: {@link TimedLock#lock(long)}
     * <p>Test for timeout > 0
     *
     * <p>Reason: the method is tested with different values for timeout.
     * <p>Expected result: the method should wait for the specified time and then be notified.
     *
     */
    @Test
    void testLock() throws InterruptedException {
        TimedLock<Object> timedLock = new TimedLock<>("Default Value");
        timedLock.lock(10L);
        assertFalse(timedLock.hasBeenNotified());
        new Thread(() -> {
            try {
                timedLock.lock(100L);
                timedLock.notify("Value");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        assertFalse(timedLock.hasBeenNotified());
        Thread.sleep(200L);
        assertTrue(timedLock.hasBeenNotified());

    }

    /**
     * Method under test: {@link TimedLock#lock(long)}} ()}
     * <p>Test for timeout = -1
     * <p>Reason: the method is tested with negative values for timeout.
     * <p>Expected result: the method should wait forever and never be notified.
     */
    @Test
    void testLock2() throws InterruptedException {
        TimedLock<Object> timedLock = new TimedLock<>("Default Value");
        new Thread(() -> {
            try {
                timedLock.lock(-1L);
                timedLock.notify("Value");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(100L);
        assertFalse(timedLock.hasBeenNotified());
    }




}

