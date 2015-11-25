package server1;

/**
 * Created by user on 19/11/2015.
 */
import java.io.*;
import java.net.*; import java.util.*;
import javax.microedition.io.*;
import javax.bluetooth.*;
import javax.bluetooth.UUID;

public class rfcommserver {
    UUID uuid = new UUID("8848",true);

    public void startserver() {
        try {
            String url = "btspp://localhost:" + uuid +
                    //  new UUID( 0x1101 ).toString() +
                    ";name=File Server";
            StreamConnectionNotifier service =
                    (StreamConnectionNotifier) Connector.open( url );

            StreamConnection con =
                    (StreamConnection) service.acceptAndOpen();
            OutputStream dos = con.openOutputStream();
            InputStream dis = con.openInputStream();

            InputStreamReader daf = new InputStreamReader(System.in);
            BufferedReader sd = new BufferedReader(daf);
            RemoteDevice dev = RemoteDevice.getRemoteDevice(con);
            while (true) {
                //String greeting = sd.readLine();
                //      String greeting = "JSR-82 RFCOMM server says hello";
                //      dos.write( greeting.getBytes() );
                if (dev !=null)
                {
                    File f = new File("src/test.xml");
                    BufferedInputStream filein =
                            new BufferedInputStream(new FileInputStream(f));
                    byte buf[] = new byte[1024];
                    int len;
                    while ((len=filein.read(buf))>0)
                        dos.write(buf,0,len);
                  //  System.out.println("Filecontent                   +f.getName()+"R:"+f.length());
                     dos.flush();
                }
                byte buffer[] = new byte[1024];
                int bytes_read = dis.read( buffer );
                String received = new String(buffer, 0, bytes_read);
                System.out.println
                        ("Message:"+ received +"From:"
                                +dev.getBluetoothAddress());
                dos.flush();
            }
            // con.close();
        } catch ( IOException e ) {
            System.err.print(e.toString());
        }
    }

//    public static void main( String args[] ) {
//        try {
//            LocalDevice local = LocalDevice.getLocalDevice();
//            System.out.println("Serverted:\n"
//                    +local.getBluetoothAddress()
//                    +"\n"+local.getFriendlyName());
//
//            rfcommserver ff = new rfcommserver();
//            while (true) {
//                ff.startserver();
//            } //while
//        }  //try
//        catch (Exception e) {System.err.print(e.toString());
//        }
//    }
}  //main
