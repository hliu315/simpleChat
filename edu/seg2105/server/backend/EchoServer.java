package edu.seg2105.server.backend;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;

import java.io.IOException;

import edu.seg2105.client.common.ChatIF;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The interface type variable. It allows the implementation of
   * the display method in the server.
   */
  ChatIF serverUI;
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ChatIF serverUI) {
    super(port);
    this.serverUI = serverUI;
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    serverUI.display("Message received: " + msg + " from " + client.getInfo("id"));
    String message = msg.toString();
    if (message.startsWith("#login")) {
      String id = message.substring(6).trim();
      if (client.getInfo("id") != null) {
        try {
          client.sendToClient("Error - client has loggged in before.");
          client.close();
        } catch (IOException e) {
        }
      }
      serverUI.display(id + " has logged on.");
      client.setInfo("id", id);
    } else {
      this.sendToAllClients(client.getInfo("id").toString() + "> " + msg);
    }
  }

  /**
   * This method handles all data coming from the Server UI
   *
   * @param message The message from the Server UI.
   */
  public void handleMessageFromServerUI(String msg) {
    if (msg.startsWith("#")) {
      msg = msg.substring(1);
      if (msg.equals("quit")) {
        quit();
      } else if (msg.equals("stop")) {
        stopListening();
      } else if (msg.equals("close")) {
        try {
          close();
        } catch (IOException e) {
          serverUI.display("ERROR - Could not close server.");
        }
      } else if (msg.startsWith("setport")) {
        msg = msg.substring(7);
        int port = Integer.parseInt(msg.trim());
        setPort(port);
      } else if (msg.equals("start")) {
        try {
          listen();
        } catch (IOException e) {
          serverUI.display("ERROR - Could not listen for clients!");
        }
      } else if (msg.equals("getport")) {
        serverUI.display("Current port: " + getPort());
      }
    } else {
      serverUI.display("Message received from Server UI: " + msg);
      this.sendToAllClients("SERVER MSG> " + msg);
    }
  }

  /**
   * This method terminates the server.
   */
  public void quit() {
    try {
      close();
    } catch (IOException e) {
      serverUI.display("ERROR - Could not close server.");
    }
    System.exit(0);
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    serverUI.display("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    serverUI.display("Server has stopped listening for connections.");
  }

  /**
   * Hook method called when the server is closed.
   */
  protected void serverClosed() {
    serverUI.display("Server closed.");
  }

  // Class methods ***************************************************

  /**
   * Method called each time a new client connection is
   * accepted.
   * 
   * @param client the connection connected to the client.
   */
  protected void clientConnected(ConnectionToClient client) {
    serverUI.display("A new Client has connected to the server.");
  }

  /**
   * Method called each time a client disconnects.\
   *
   * @param client the connection with the client.
   */
  synchronized protected void clientDisconnected(ConnectionToClient client) {
    serverUI.display(client.getInfo("id") + " has disconnected.");
  }

  // /**
  // * This method is responsible for the creation of
  // * the server instance (there is no UI in this phase).
  // *
  // * @param args[0] The port number to listen on. Defaults to 5555
  // * if no argument is entered.
  // */
  // public static void main(String[] args) {
  // int port = 0; // Port to listen on
  //
  // try {
  // port = Integer.parseInt(args[0]); // Get port from command line
  // } catch (Throwable t) {
  // port = DEFAULT_PORT; // Set port to 5555
  // }
  //
  //
  // EchoServer sv = new EchoServer(port);
  //
  // try {
  // sv.listen(); // Start listening for connections
  // } catch (Exception ex) {
  // serverUI.display("ERROR - Could not listen for clients!");
  // }
  // }
}
// End of EchoServer class
