package javaFXServer;

import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by user on 19/11/2015.
 */
public abstract class NetworkConnection {

    private Consumer<Serializable> onReceiveCallback;

    public NetworkConnection(Consumer<Serializable> onReceiveCallback){
        this.onReceiveCallback - onReceiveCallback;
    }

    public void startConnection() throws Exception {

    }

    public void send(Serializable data) throws Exception{

    }

    public void closeConnection() throws Exception{

    }

    protected abstract Boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    private class ConnectionThread extends Thread {
        private Socket socket;
        

        @Override
        public void Run(){
            try{
                ServerSocket serverSocket = new ServerSocket(getPort());
            }
            catch (IOException e){

            }
        }
    }
}
