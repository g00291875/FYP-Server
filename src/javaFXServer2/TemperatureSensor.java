package javaFXServer2;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.common.base.*;
import com.google.common.collect.Lists;

/**
 * Created by user on 20/11/2015.
 */
public class TemperatureSensor {

    private float currentReading = 67;
    private boolean stopWasRequested = false;
    ExecutorService service = Executors.newCachedThreadPool();
    private final List<TemperatureSensorListener> listeners = Lists.newArrayList();

    public TemperatureSensor(){
        service.submit(new Runnable() {
            @Override
            public void run() {
                while (!stopWasRequested) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentReading--;
                    fireTemperatureChangeEvent();
                }
            }

            private void fireTemperatureChangeEvent() {
                for (TemperatureSensorListener listener : listeners){
                    listener.onReadingChange();
            }
        }
        });//service.submit new runnable
    }//TempSensor


    public void addListener(TemperatureSensorListener listener) {
        Preconditions.checkNotNull(listener);
        listeners.add(listener);
    }

    public float getCurrentReading(){
        return currentReading;
    }

    public void shutdown(){
        stopWasRequested = true;
    }

}
