package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
     */
    @Test
    void testConstructor() {
        TimedLock<Object> actualTimedLock = new TimedLock<>("Default Value");
        actualTimedLock.setValue("Value");
        actualTimedLock.reset();
        assertFalse(actualTimedLock.hasBeenNotified());
    }

    /**
     * Method under test: {@link TimedLock#lock(long)}
     */
    @Test
    void testLock() throws InterruptedException {
        TimedLock<Object> timedLock = new TimedLock<>("Default Value");
        timedLock.lock(10L);
        assertFalse(timedLock.hasBeenNotified());
    }

    /**
     * Method under test: {@link TimedLock#lock(long)}} ()}
     * <p>Test for timeout = -1
     */
    @Test
    void testLock2() throws InterruptedException {
        TimedLock<Object> timedLock = new TimedLock<>("Default Value");
        new Thread(() -> {
            try {
                timedLock.lock(-1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(100L);
        assertFalse(timedLock.hasBeenNotified());
    }



    /**
     * Method under test: {@link TimedLock#notify(Object)}
     */
    @Test
    void testNotify() {
        TimedLock<Object> timedLock = new TimedLock<>("Default Value");
        timedLock.notify("Value");
        assertEquals("Value", timedLock.getValue());
        assertTrue(timedLock.hasBeenNotified());
    }


}

