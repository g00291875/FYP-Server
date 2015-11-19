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


    // Constant that indicate command from devices
    private static final int EXIT_CMD = -1;
    private static final int KEY_RIGHT = 1;
    private static final int KEY_LEFT = 2;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ProcessConnectionThread(StreamConnection connection)
    {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
            // prepare to receive data


            //InputStream inputStream = mConnection.openInputStream();

            //dataInputStream = new DataInputStream(mConnection.getInputStream());
           // dataOutputStream = new DataOutputStream(mConnection.getOutputStream());

            dataInputStream = new DataInputStream(mConnection.openInputStream());
            dataOutputStream = new DataOutputStream(mConnection.openOutputStream());


            System.out.println("waiting for input");

//            byte[] buf = "hello".getBytes("UTF-8");
//            dataOutputStream.write(buf, 0, buf.length);
//            dataOutputStream.flush();

            while (true) {
                if(dataInputStream.available() > 0){
                    byte[] msg = new byte[dataInputStream.available()];
                    dataInputStream.read(msg, 0, dataInputStream.available());
                    System.out.print(new String(msg)+"\n");
                    //sendHandler(ChatActivity.MSG_BLUETOOTH, nameBluetooth + ": " + new String(msg));

                    sendMessageByBluetooth("received");


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

    /**
     * Process the command from client
     * @param command the command code
     */
    private void processCommand(int command) {
        try {
            Robot robot = new Robot();
            switch (command) {
                case KEY_RIGHT:
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    System.out.println("Right");
                    break;
                case KEY_LEFT:
                    robot.keyPress(KeyEvent.VK_LEFT);
                    System.out.println("Left");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
