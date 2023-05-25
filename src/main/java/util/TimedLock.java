package util;

public class TimedLock<ValueType>{

    private boolean notified;
    private ValueType value;
    public TimedLock(ValueType defaultValue){
        notified = false;
        value = defaultValue;
    }

    public synchronized void reset(){
        notified = false;
    }

    @SuppressWarnings( "BooleanMethodIsAlwaysInverted")
    public synchronized boolean hasBeenNotified(){
        return notified;
    }

    public synchronized void setValue(ValueType value){
        this.value = value;
    }

    public synchronized ValueType getValue(){
        return value;
    }

    public synchronized void lock(long timeoutMillis) throws InterruptedException {
        this.notified = false;
        if(timeoutMillis<=0){
            this.wait();
        }else {
            this.wait(timeoutMillis);
        }
    }

    public synchronized void notify(ValueType value){
        this.notified = true;
        this.value = value;
        this.notifyAll();
    }
}
