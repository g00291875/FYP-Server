package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerFX extends Application {
    private static TextField userText;
    private static TextArea chatWindow;
    private static ObjectOutputStream output;
    private static ObjectInputStream input;
    private static ServerSocket server;
    private static Socket connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        HBox message = new HBox();

        userText = new TextField();
        userText.setEditable(false);
        userText.setOnAction(e ->
        {
            sendMessage(e.getEventType().toString());
            userText.setText("");
        });

        message.getChildren().add(userText);
        message.setAlignment(Pos.CENTER);
        message.setPadding(new Insets(10));

        chatWindow = new TextArea();
        chatWindow.setPrefSize(300, 150);
        chatWindow.setVisible(true);

        root.setCenter(chatWindow);
        root.setBottom(message);

        startRunning();
        primaryStage.setTitle("Instant Messenger");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void startRunning() {
        try {
            server = new ServerSocket(8080, 100);
            while (true) {
                try {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }
                //Connect and have a conversation
                catch (EOFException eof) {
                    showMessage("\n Server ended the connection! ");
                } finally {
                    closeStreamsAndSockets();
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //Wait for Connection, then display connectioninformation
    public static void waitForConnection() throws IOException
    {
        showMessage(" Waiting for someone to connect...\n");
        connection = server.accept();
        showMessage(" Now connected to " + connection.getInetAddress().getHostAddress()); //Returns YOUR address and IP address
    }
    //Get stream to send and receive data.
    private static void setupStreams() throws IOException
    {
        //Creating the pathway that allows to connect to another computer. The computer that the socket created.
        output = new ObjectOutputStream(connection.getOutputStream());
        //Sometimes Data has leftovers and that data needs to be 'flushed' everytime you connect to a new chat.
        output.flush();
        //The pathway that allows the CLIENT to send message.
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams are now setup! \n");
    }
    //During the chat conversation.
    private static void whileChatting() throws IOException
    {
        String message = " You are now connected! ";
        sendMessage(message);
        ableToType(true);
        do {
            //Have a conversation
            try {
                message = (String) input.readObject();
                //New line everytime, so it won't get messy.
                showMessage(" \n" + message);
            }
            //This should hopefully NEVER be called.
            catch (ClassNotFoundException e)
            {
                showMessage("\n Weird dude...");
            }
        }
        //When the user types 'END', the program stops executing and stops the chatting.
        while (!message.equals("CLIENT - END"));
    }
    //Close streams and sockets after you are done chatting.

    public static void closeStreamsAndSockets() throws IOException
    {
        showMessage("\n Closing connection.... \n");
        ableToType(false);
        try
        {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    //Send message to client
    private static void sendMessage(String message)
    {
        try {//Sends a message through the output. Create an object and send to your outputstream
            output.writeObject("SERVER - " + message);
            //In case you have any bytes leftover, we flush it for safety and clear the buffer
            output.flush();
            showMessage("\nSERVER - " + message);
        } catch (IOException e) {
            chatWindow.appendText("\n ERROR: Can't send that message. Try again...");
        }
    }
    //Shows the messages // Updates chatwindow
    private static void showMessage(final String text)
    {
        //Creating a thread
        Platform.runLater(() ->
        {
            //Adds a message to the end of document. Updating the Chat Window.
            chatWindow.appendText(text);
        });
    }

    //Able to type


    private static void ableToType(final boolean permission)
    {
        //Creating a thread
        Platform.runLater(() ->
        {
            //Able to type now
            userText.setEditable(permission);
        });
    }
    public static void main(String[] args)
    {
        launch(args);
    }
}

