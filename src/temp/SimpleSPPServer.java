package temp;

import java.io.*;

import javax.bluetooth.*;
import javax.microedition.io.*;

/**
 * Class that implements an SPP Server which accepts single line of
 * message from an SPP client and sends a single line of response to the client.
 */

public class SimpleSPPServer {
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;


    //start server
    private void startServer() throws IOException{

        //Create a UUID for SPP
        UUID uuid = new UUID("1101", true);
        //Create the servicve url
        String connectionString = "btspp://localhost:" + uuid +";name=Sample SPP Server";

        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier)Connector.open( connectionString );

        //Wait for client connection
        System.out.println("\nServer Started. Waiting for clients to connect...");
        StreamConnection connection=streamConnNotifier.acceptAndOpen();

        RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
        System.out.println("Remote device address: "+dev.getBluetoothAddress());
        System.out.println("Remote device name: "+dev.getFriendlyName(true));

        //read string from spp client
        //InputStream inStream=connection.openInputStream();
        //BufferedReader bReader=new BufferedReader(new InputStreamReader(inStream));
        //String lineRead=bReader.readLine();
        //System.out.println(lineRead);
        /**********************/
        dataInputStream = new DataInputStream(connection.openInputStream());
        dataOutputStream = new DataOutputStream(connection.openOutputStream());

        /*********************/
        while (true) {
            if(dataInputStream.available() > 0){
                byte[] msg = new byte[dataInputStream.available()];
                dataInputStream.read(msg, 0, dataInputStream.available());

                System.out.print(new String(msg)+"\n");
            }
        }



        //send response to spp client
       // OutputStream outStream=connection.openOutputStream();
       // PrintWriter pWriter=new PrintWriter(new OutputStreamWriter(outStream));
       // pWriter.write("Response String from SPP Server\r\n");
       // pWriter.flush();

       // pWriter.close();
       // streamConnNotifier.close();

    }


    public static void main(String[] args) throws IOException {

        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: "+localDevice.getBluetoothAddress());
        System.out.println("Name: "+localDevice.getFriendlyName());

        SimpleSPPServer sampleSPPServer=new SimpleSPPServer();
        sampleSPPServer.startServer();

    }
}
