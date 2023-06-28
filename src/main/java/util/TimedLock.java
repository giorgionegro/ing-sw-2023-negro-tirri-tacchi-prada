package util;

/**
 * This class implements a thread-lock that can be interrupted setting a value to be stored, the lock can be indefinite
 * or have a timeout, in this case a default value is set and stored after the timeout
 * @param <ValueType> the type of the stored value
 */
public class TimedLock<ValueType>{
    /**
     * If the lock has been unlocked or not
     */
    private boolean unlocked;
    /**
     * The value the lock is storing
     */
    private ValueType value;

    /**
     * Construct an instance and set given value as default value
     * @param defaultValue the value to be stored
     */
    public TimedLock(ValueType defaultValue){
        this.unlocked = false;
        this.value = defaultValue;
    }

    /**
     * This method resets the lock storing the given value as default value
     * @param defaultValue the value to be stored
     */
    public synchronized void reset(ValueType defaultValue){
        this.unlocked = false;
        this.value = defaultValue;
    }

    /**
     * This method returns whether lock has been unlocked after a reset
     * @return {@link #unlocked}
     */
    @SuppressWarnings( "BooleanMethodIsAlwaysInverted")
    public synchronized boolean hasBeenUnlocked(){
        return this.unlocked;
    }

    /**
     * This method returns the stored value
     * @return {@link #value}
     */
    public synchronized ValueType getValue(){
        return this.value;
    }

    /**
     * This method locks the calling thread, the lock is indefinite if the given timeout value is &lt;=0, otherwise locks for
     * the given timeout amount of time
     * @param timeoutMillis the timeout time amount
     * @throws InterruptedException if an error occurred during lock
     */
    public synchronized void lock(long timeoutMillis) throws InterruptedException {
        this.unlocked = false;
        if(timeoutMillis<=0){
            this.wait();
        }else {
            this.wait(timeoutMillis);
        }
    }

    /**
     * This method stores the given value and unlocks the waiting threads
     * @param newValue the value to be stored
     */
    public synchronized void unlock(ValueType newValue){
        this.unlocked = true;
        this.value = newValue;
        this.notifyAll();
    }

}
