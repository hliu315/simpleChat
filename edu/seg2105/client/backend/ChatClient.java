// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
  // Instance variables **********************************************

  /**
   * User login id.
   */
  String id;
  /**
   * The interface type variable. It allows the implementation of
   * the display method in the client.
   */
  ChatIF clientUI;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the chat client.
   *
   * @param id       The id of client.
   * @param host     The server to connect to.
   * @param port     The port number to connect on.
   * @param clientUI The interface type variable.
   */

  public ChatClient(String id, String host, int port, ChatIF clientUI) throws IOException {
    super(host, port); // Call the superclass constructor
    this.id = id;
    this.clientUI = clientUI;
    login();
  }

  // Instance methods ************************************************

  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) {
    clientUI.display(msg.toString());

  }

  /**
   * This method handles all data coming from the UI
   *
   * @param message The message from the UI.
   */
  public void handleMessageFromClientUI(String message) {
    try {
      if (message.startsWith("#")) {
        message = message.substring(1);
        if (message.equals("quit")) {
          quit();
        } else if (message.equals("logoff")) {
          closeConnection();
        } else if (message.startsWith("sethost")) {
          if (isConnected()) {
            clientUI.display("Cannot set new host because there exists alive connection.");
          } else {
            String host = message.substring(7).trim();
            setHost(host);
          }
        } else if (message.startsWith("setport")) {
          if (isConnected()) {
            clientUI.display("Cannot set new port because there exists alive connection.");
          } else {
            int port = Integer.parseInt(message.substring(7).trim());
            setPort(port);
          }
        } else if (message.equals("login")) {
          if (isConnected()) {
            clientUI.display("Client has connected already.");
          } else {
            login();
          }
        } else if (message.equals("gethost")) {
          clientUI.display("Current host name: " + getHost());
        } else if (message.equals("getport")) {
          clientUI.display("Current host name: " + getHost());
        }
      } else {
        sendToServer(message);
      }
    } catch (IOException e) {
      clientUI.display("Could not send message to server.  Terminating client.");
      quit();
    }
  }

  /**
   * This method terminates the client.
   */
  public void quit() {
    try {
      closeConnection();
    } catch (IOException e) {
    }
    System.exit(0);
  }

  /**
   * This method log in the client.
   */
  public void login() throws IOException {
    openConnection();
    sendToServer("#login " + id);
    clientUI.display(id + " has logged on.");
  }

  /**
   * Method called after the connection has been closed.
   */
  public void connectionClosed() {
    clientUI.display(id + " has logged off. Connection closed.");
  }

  /**
   * Method called each time an exception is thrown by the client's
   * thread that is waiting for messages from the server.
   * 
   * @param exception the exception raised.
   */
  public void connectionException(Exception exception) {
    clientUI.display(id + " has logged off. Server has shut down.");
  }
}
// End of ChatClient class
