package util;

import java.awt.Component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObservableTest {
    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default constructor of {@link Observable}
     *   <li>{@link Observable#clearChanged()}
     *   <li>{@link Observable#setChanged()}
     *   <li>{@link Observable#hasChanged()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Observable<Component.BaselineResizeBehavior> actualObservable = new Observable<>();
        actualObservable.clearChanged();
        actualObservable.setChanged();
        assertTrue(actualObservable.hasChanged());
    }

    /**
     * Method under test: {@link Observable#notifyObservers()} ()}
     */
    @Test
    void testNotifyObservers() {
        Observable<Component.BaselineResizeBehavior> observable = new Observable<>();
        observable.notifyObservers();
        assertEquals(0, observable.countObservers());
    }

    /**
     * Method under test: {@link Observable#addObserver(Observer)} ()}
     */
    @Test
    void testAddObserver() {
        Observable<Component.BaselineResizeBehavior> observable = new Observable<>();
        observable.addObserver((o, arg) -> {
        });
        assertEquals(1, observable.countObservers());
    }

    /**
     * Method under test: {@link Observable#addObserver(Observer)} ()}
     * <p>Test for observer = null
     */
    @Test
    void testAddObserver2() {
        Observable<Component.BaselineResizeBehavior> observable = new Observable<>();
        assertThrows(NullPointerException.class,()->observable.addObserver(null));
        assertEquals(0, observable.countObservers());
    }

    /**
     * Method under test: {@link Observable#countObservers()}
     */
    @Test
    void testCountObservers() {
        assertEquals(0, (new Observable<>()).countObservers());
    }
}

