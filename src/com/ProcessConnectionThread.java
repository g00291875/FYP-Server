package com;

import java.awt.event.KeyEvent;

        import java.awt.Robot;
        import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

        import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {

    private StreamConnection mConnection;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ProcessConnectionThread(StreamConnection connection)
    {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
            dataInputStream = new DataInputStream(mConnection.openInputStream());
            dataOutputStream = new DataOutputStream(mConnection.openOutputStream());

            System.out.println("waiting for input");

            while (true) {
                if(dataInputStream.available() > 0){
                    byte[] msg = new byte[dataInputStream.available()];
                    dataInputStream.read(msg, 0, dataInputStream.available());
                    System.out.print(new String(msg)+"\n");
                    sendMessageByBluetooth("request received");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         */
    }

    public boolean sendMessageByBluetooth(String msg){
        try {
            if(dataOutputStream != null){
                dataOutputStream.write(msg.getBytes());
                dataOutputStream.flush();
                return true;
            }else{
               // sendHandler(ChatActivity.MSG_TOAST, context.getString(R.string.no_connection));
                return false;
            }
        } catch (IOException e) {
           // LogUtil.e(e.getMessage());

           // sendHandler(ChatActivity.MSG_TOAST, context.getString(R.string.failed_to_send_message));
            return false;
        }
    }
}
