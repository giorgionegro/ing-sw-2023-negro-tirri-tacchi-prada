package util;

import java.awt.Component;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservableTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>{@link Observable#clearChanged()}
     *   <li>{@link Observable#setChanged()}
     *   <li>{@link Observable#hasChanged()}
     * </ul>
     * <p>
     * Reason: the methods are closely related and can be tested together.
     * <p>
     * Expected result: afeter calling {@link Observable#setChanged()} the method {@link Observable#hasChanged()} should return true. After calling {@link Observable#clearChanged()} the method {@link Observable#hasChanged()} should return false.
     */
    @Test
    void testHasChanged() {
        Observable<Component.BaselineResizeBehavior> actualObservable = new Observable<>();
        actualObservable.setChanged();
        assertTrue(actualObservable.hasChanged());
        actualObservable.clearChanged();
        assertFalse(actualObservable.hasChanged());
    }

    /**
     * Method under test:
     * <ul>
     *     <li>{@link Observable#notifyObservers()} ()}</li>
     *     <li>{@link Observable#addObserver(Observer)} ()}</li>
     *     <li>{@link Observable#countObservers()}</li>
 *     </ul>
     * <p>
     * Reason: the methods are closely related and can be tested together.
     * <p>
     * Expected result:
     *<ul>
     *  <li>after calling {@link Observable#addObserver(Observer)} ()} the method {@link Observable#countObservers()} should return 1.</li>
     * <li>after calling {@link Observable#notifyObservers()} ()} the method {@link Observer#update(Observable, Enum)} should be called (called == True).</li>
     *</ul>
     */
    @Test
    void testNotifyObservers() {
        Observable<Component.BaselineResizeBehavior> observable = new Observable<>();
        AtomicBoolean called = new AtomicBoolean(false);
        observable.addObserver((o, arg) -> {
            called.set(true);
        });
        observable.notifyObservers();
        assertTrue(called.get());

        assertEquals(1, observable.countObservers());
    }


    /**
     * Method under test: {@link Observable#addObserver(Observer)} ()}
     * <p>Test for observer = null
     */
    @Test
    void testAddObserver2() {
        Observable<Component.BaselineResizeBehavior> observable = new Observable<>();
        assertThrows(NullPointerException.class, () -> observable.addObserver(null));
        assertEquals(0, observable.countObservers());
    }


}

