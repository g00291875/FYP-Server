package swingWorker; /**
 * Created by user on 18/09/2015.
 */

import com.ProcessConnectionThread;
import server1.rfcommserver;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.*;

/**
 * Author : Thomas Flynn
 * Student ID: G00291875
 * Subject: Client Server Programming
 * Assignment 1&2: Streams & Thread Safe Swing
 * */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MyGui extends JFrame {

    private JButton load;
    private JButton reverseContents;
    private JButton reverseWords;
    private JButton countWords;
    private JTextArea jTextArea1;
    private JTextArea jTextArea2;
    private JTextArea jTextArea3;
    private JTextArea jTextArea4;

    //JFileChooser extends JComponent, implements Accessible
    private JFileChooser fileChooser;
    private JButton openButton;
    private File file;
    int returnVal;
    Rearrange rr = null;
    String fileAbsolutePath;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public MyGui() {
        createView();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();// automatically pack everything tightly together
        setLocationRelativeTo(null);
        setResizable(false);
        //setPreferredSize(new Dimension(278, 179));
        setLayout(null);
    }

    private void createView() {

        try {
            //********************** first gui **********************//
            load = new JButton("load");
            load.addActionListener(new ActionListener1());

            reverseContents = new JButton("Reverse Contents");
            reverseContents.addActionListener(new ActionListener2());

            reverseWords = new JButton("Reverse Words");
            reverseWords.addActionListener(
                    new ButtonCounterActionListener3());

            countWords = new JButton("Count Words");
            countWords.addActionListener(
                    new ButtonCounterActionListener4());


            fileChooser = new JFileChooser();
            openButton = new JButton("choose file");
            openButton.addActionListener(
                    new ButtonCounterActionListener5());

            jTextArea1 = new JTextArea(5, 5);
            jTextArea2 = new JTextArea(5, 5);
            jTextArea3 = new JTextArea(5, 5);
            jTextArea4 = new JTextArea(5, 5);

            //adjust size and set layout
            setPreferredSize(new Dimension(667, 393));
            setLayout(null);

            //add components
            add(load);
            add(reverseContents);
            add(reverseWords);
            add(countWords);
            add(openButton);
            add(jTextArea1);
            add(jTextArea2);
            add(jTextArea3);
            add(jTextArea4);

            //set component bounds (only needed by Absolute Positioning)
            load.setBounds(40, 5, 140, 25);
            reverseContents.setBounds(465, 5, 150, 25);
            reverseWords.setBounds(45, 190, 140, 25);
            countWords.setBounds(465, 190, 150, 25);
            openButton.setBounds(300, 150, 80, 25);
            jTextArea1.setBounds(0, 30, 250, 155);
            jTextArea2.setBounds(440, 30, 230, 155);
            jTextArea3.setBounds(0, 215, 245, 165);
            jTextArea4.setBounds(440, 215, 230, 150);

            rr = new Rearrange();
        } catch (Exception guicons) {
            guicons.printStackTrace();
        }
    }

    /*************************************************
     * Start Load words code
     *******************************/
    private class ActionListener1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadWords();
        }
    }

    void loadWords() {
        SwingWorker<String[], Void> worker1 = new SwingWorker<String[], Void>() {


            @Override
            protected String[] doInBackground() throws Exception {
                String[] lineArray = new String [rr.getarraysize()];

                try {
                    lineArray = rr.getArrayOfLines();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return lineArray;
            }//doInBackground()

            @Override
            protected void done(){
                try {
                    String[] lineArray = get();
                    for(String W: lineArray)
                        jTextArea1.append(W + "\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }//done
        };//swing worker
        worker1.execute();
    }//loadWords()




    /*************************************************
     * Start Reverse words code
     **************************************/
    private class ActionListener2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        waitthread();
                    }
                });//SwingUtilities.invokeLater(new Runnable() {
            }//try
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    void waitthread(){
        SwingWorker<Boolean, String> worker2 = new SwingWorker<Boolean, String>() {

            @Override
            protected Boolean doInBackground() throws Exception {
                Boolean flag = false;
                // retrieve the local Bluetooth device object
                StreamConnectionNotifier notifier = null;
                StreamConnection connection = null;
                try {
                    LocalDevice local = null;
                    local = LocalDevice.getLocalDevice();
                    local.setDiscoverable(DiscoveryAgent.GIAC);
                    UUID uuid = new UUID(80087355); // "04c6093b-0000-1000-8000-00805f9b34fb"
                    String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
                    notifier = (StreamConnectionNotifier) Connector.open(url);
                    connection = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // waiting for connection
                while (flag == false) {
                    try {
                        System.out.println("waiting for connection...");
                        connection = notifier.acceptAndOpen();
                        System.out.println("connected!");
                        //Thread processThread = new Thread(new ProcessConnectionThread(connection));
                       // processThread.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                }
            }

//            @Override
//            protected void process(String msg) {
//                jTextArea2.append( msg + "\n");
//            }

            @Override
            protected void done() {
                System.out.println("finsihed");
            }//done
        };//swing worker
        worker2.execute();
    }//loadWords()

    void ReverseWords() {
        SwingWorker<String[], Void> worker2 = new SwingWorker<String[], Void>() {


            @Override
            protected String[] doInBackground() throws Exception {
                String[] lineArray = new String [rr.getarraysize()];
                try {
                    rr.extractWords();
                    rr.reverseContents();
                    lineArray = rr.getArrayOfLines();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return lineArray;
            }//doInBackground()

            @Override
            protected void done() {
                try {
                    String[] lineArray ;//= new String [rr.getarraysize()];
                    lineArray = get();

                    for (String W : lineArray)
                        jTextArea2.append(W + "\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }//done
        };//swing worker
        worker2.execute();
    }//loadWords()

    /*************************************************
     * Start Reverse pairs code
     **************************************/

    private class ButtonCounterActionListener3 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        ReversePairs();
                    }
                });
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    void ReversePairs() {
        SwingWorker<String[], Void> worker3 = new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground()  {
                String[] lineArray = null;
                try {
                    rr.extractWords();//have to reset contents for reversing words
                    rr.reversePairs();
                    lineArray = rr.getArrayOfLines();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return lineArray;
            }//doInBackground()

            @Override
            protected void done(){
                try {

                    String[] lineArray = get();
                    for(String W: lineArray)
                        jTextArea3.append(W + "\n");
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }//done
        };//swing worker
        worker3.execute();
    }//start()

    /*************************************************
     * Start Reverse word count code
     **************************************/

    private class ButtonCounterActionListener4 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            wordCount();
        }
    }

    @SuppressWarnings("rawtypes")
    void wordCount() {
        SwingWorker<Map, Void> worker4 = new SwingWorker<Map, Void>() {

            @SuppressWarnings("unchecked")
            @Override
            protected Map<String, Integer> doInBackground() {
                Map<String, Integer> LocalWordCountMap = new HashMap<String, Integer>();
                try {
                    rr.countWordsFunc();
                    LocalWordCountMap = rr.getWordCount();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return LocalWordCountMap;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void done() {
                Map<String, Integer> LocalWordCountMap = new HashMap<String, Integer>();
                try {
                    LocalWordCountMap = get();
                    for (Map.Entry<String, Integer> entry : LocalWordCountMap.entrySet()) {
                        jTextArea4.append("''" + (entry.getKey() + "''  occurs "+entry.getValue() + " times\n"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }//done

        };//swing worker
        worker4.execute();
    }//start()

    /***************************************************************
     * Listener 5 is the JFileChooser button
     * it take the path of the file and creates an instance of the Rearrange object
     * The Rearrange object knows the contents of the text file and has various methods to interact with the text contents
     * ***********************************************************/

    private class ButtonCounterActionListener5 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getSource() == openButton) {//The object (openButton = new JButton)  on which the Event initially occurred.
                    returnVal = fileChooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        file = fileChooser.getSelectedFile();

                        fileAbsolutePath = file.getAbsolutePath();

                        startIOThread();
                    }
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }//void actionPerformed
    }//ButtonCounterActionListener5


    void startIOThread() {
        SwingWorker<Void, Void> worker0 = new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                    try {
                        rr = new Rearrange(fileAbsolutePath);
                        return null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                return null;
                }//doInBackground()
        };
        worker0.execute();
    }//startIOThread()

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MyGui().setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






