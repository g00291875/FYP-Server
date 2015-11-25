package javaFXServer3;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by user on 20/11/2015.
 */
public class TemperatureSensor {

    private float currentReading = 67;
    private boolean stopWasRequested = false;
    ExecutorService service = Executors.newCachedThreadPool();
    private final List<TemperatureSensorListener> listeners = Lists.newArrayList();

    private String input = null;

    private TemperatureSensorListener lisener = null;

    public TemperatureSensor(){
        service.submit(new Runnable() {
            @Override
            public void run() {
                while (!stopWasRequested) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentReading--;
                    fireTemperatureChangeEvent();
                }
                /**************************/

                try {
                    Boolean flag = false;
                    // retrieve the local Bluetooth device object
                    StreamConnectionNotifier notifier = null;
                    StreamConnection connection = null;

                    LocalDevice local = null;
                    local = LocalDevice.getLocalDevice();
                    local.setDiscoverable(DiscoveryAgent.GIAC);
                    UUID uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
                    String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
                    notifier = (StreamConnectionNotifier) Connector.open(url);
                    connection = null;

                    // waiting for connection
                    while (flag == false) {
                        try {
                            System.out.println("waiting for connection...");
                            connection = notifier.acceptAndOpen();
                            System.out.println("connected!");
                            flag = true;
                            //Thread processThread = new Thread(new ProcessConnectionThread(connection));
                            // processThread.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    DataInputStream dataInputStream = new DataInputStream(connection.openInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(connection.openOutputStream());

                    System.out.println("waiting for input");

                    while (true) {
                        if(dataInputStream.available() > 0){
                            byte[] msg = new byte[dataInputStream.available()];
                            dataInputStream.read(msg, 0, dataInputStream.available());
                            System.out.print(new String(msg)+"\n");
                           // publish(new String(msg));
                            fireTemperatureChangeEvent();
                        }
                        //return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /****************************/
            }

            private void fireTemperatureChangeEvent() {
               // for (TemperatureSensorListener listener : listeners){
                  //  listeners.get(0).onReadingChange();
                lisener.onReadingChange();
          //  }
        }
        });//service.submit new runnable
    }//TempSensor


    public void addListener(TemperatureSensorListener listener) {
        Preconditions.checkNotNull(listener);
       // listeners.add(listener);
        lisener = listener;
    }

    public float getCurrentReading(){
        return currentReading;
    }

    public String getInputReading(){
        return input;
    }

    public void shutdown(){
        stopWasRequested = true;
    }

}
