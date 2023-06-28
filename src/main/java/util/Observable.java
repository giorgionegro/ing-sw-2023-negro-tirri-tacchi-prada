package util;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class represents an observable object, or "data"
 * in the model-view paradigm. It can be subclassed to represent an
 * object that the application wants to have observed.
 * <p>
 * An observable object can have one or more observers. An observer
 * may be any object that implements interface {@code Observer}. After an
 * observable instance changes, an application calling the
 * {@code Observable}'s {@code notifyObservers} method
 * causes all of its observers to be notified of the change by a call
 * to their {@code update} method.
 * <p>
 * The order in which notifications will be delivered is unspecified.
 * The default implementation provided in the Observable class will
 * notify Observers in the order in which they registered interest, but
 * subclasses may change this order, use no guaranteed order, deliver
 * notifications on separate threads, or may guarantee that their
 * subclass follows this order, as they choose.
 * <p>
 * Note that this notification mechanism has nothing to do with threads
 * and is completely separate from the {@code wait} and {@code notify}
 * mechanism of class {@code Object}.
 * <p>
 * When an observable object is newly created, its set of observers is
 * empty. Two observers are considered the same if and only if the
 * {@code equals} method returns true for them.
 *
 * @param <Event> the enumeration of the event that this observable is emitting
 *                This class is a Generic Implementation of the deprecated {@link java.util.Observable}.
 * @see #notifyObservers()
 * @see #notifyObservers(Enum)
 * @see Observer
 * @see Observer#update(Observable, Enum)
 */
@SuppressWarnings("deprecation")
public class Observable<Event extends Enum<Event>> {

    /**
     * Thread pool used to notify observers
     */
    //TODO
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);
    /**
     * Vector of observers
     */
    private final Vector<Observer<? extends Observable<Event>, Event>> obs;
    /**
     * Flag that indicates if the observable has changed
     */
    private boolean changed = false;

    /**
     * Construct an Observable with zero Observers.
     */

    public Observable() {
        super();
        this.obs = new Vector<>();
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    public synchronized void addObserver(Observer<? extends Observable<Event>, Event> o) {
        if (o == null)
            throw new NullPointerException();
        if (!this.obs.contains(o)) {
            this.obs.addElement(o);
            ((Observer<Observable<Event>, Event>) o).update(this, null);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing {@code null} to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    public synchronized void deleteObserver(Observer<? extends Observable<Event>, Event> o) {
        this.obs.removeElement(o);
    }

    /**
     * If this object has changed, as indicated by the
     * {@code hasChanged} method, then notify all of its observers
     * and then call the {@code clearChanged} method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its {@code update} method called with two
     * arguments: this observable object and {@code null}. In other
     * words, this method is equivalent to:
     * <blockquote>{@code
     * notifyObservers(null)}</blockquote>
     *
     * @see #clearChanged()
     * @see #hasChanged()
     * @see Observer#update(Observable, Enum)
     */
    public void notifyObservers() {
        this.notifyObservers(null);
    }

    /**
     * If this object has changed, as indicated by the
     * {@code hasChanged} method, then notify all of its observers
     * and then call the {@code clearChanged} method to indicate
     * that this object has no longer changed.
     * <p>
     * Each observer has its {@code update} method called with two
     * arguments: this observable object and the {@code arg} argument.
     *
     * @param arg any object.
     * @see #clearChanged()
     * @see #hasChanged()
     * @see Observer#update(Observable, Enum)
     */
    public void notifyObservers(Event arg) {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!this.changed)
                return;
            arrLocal = this.obs.toArray();
            this.clearChanged();
        }

        this.executorService.submit(() -> {
            for (int i = arrLocal.length - 1; i >= 0; i--)
                ((Observer<Observable<Event>, Event>) arrLocal[i]).update(this, arg);
        });
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    public synchronized void deleteObservers() {
        this.obs.removeAllElements();
    }

    /**
     * Marks this {@code Observable} object as having been changed; the
     * {@code hasChanged} method will now return {@code true}.
     */
    protected synchronized void setChanged() {
        this.changed = true;
    }

    /**
     * Indicates that this object has no longer changed, or that it has
     * already notified all of its observers of its most recent change,
     * so that the {@code hasChanged} method will now return {@code false}.
     * This method is called automatically by the
     * {@code notifyObservers} methods.
     *
     * @see #notifyObservers()
     * @see #notifyObservers(Enum)
     */
    protected synchronized void clearChanged() {
        this.changed = false;
    }

    /**
     * Tests if this object has changed.
     *
     * @return {@code true} if and only if the {@code setChanged}
     * method has been called more recently than the
     * {@code clearChanged} method on this object;
     * {@code false} otherwise.
     * @see #clearChanged()
     * @see #setChanged()
     */
    public synchronized boolean hasChanged() {
        return this.changed;
    }

    /**
     * Returns the number of observers of this {@code Observable} object.
     *
     * @return the number of observers of this object.
     */
    public synchronized int countObservers() {
        return this.obs.size();
    }
}
