package edu.seg2105.server.ui;

import java.util.Scanner;

import edu.seg2105.server.backend.EchoServer;
import edu.seg2105.client.common.ChatIF;

/**
 * This class constructs the UI for a server client. It implements the
 * chat interface in order to activate the display() method.
 */
public class ServerConsole implements ChatIF {
  // Class variables *************************************************

  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;

  // Instance variables **********************************************

  /**
   * The instance of the client that created this ConsoleChat.
   */
  EchoServer server;

  /**
   * Scanner to read from the console
   */
  Scanner fromConsole;

  // Constructors ****************************************************

  /**
   * Constructs an instance of the ServerConsole UI.
   *
   * @param port The port to connect on.
   */
  public ServerConsole(int port) {
    server = new EchoServer(port, this);
    try {
        server.listen(); // Start listening for connections
      } catch (Exception ex) {
        System.out.println("ERROR - Could not listen for clients!");
      }

    // Create scanner object to read from console
    fromConsole = new Scanner(System.in);
  }

  // Instance methods ************************************************

  /**
   * This method waits for input from the console. Once it is
   * received, it sends it to the server's message handler.
   */
  public void accept() {
    try {

      String message;

      while (true) {
        message = fromConsole.nextLine();
        server.handleMessageFromServerUI(message);
      }
    } catch (Exception ex) {
      System.out.println("Unexpected error while reading from console!");
    }
  }

  /**
   * This method overrides the method in the ChatIF interface. It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
  public void display(String message) {
    System.out.println("> " + message);
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of the Server UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) {
    int port = DEFAULT_PORT;

    try {
      port = Integer.parseInt(args[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      port = DEFAULT_PORT;
    }
    ServerConsole chat = new ServerConsole(port);
    chat.accept(); // Wait for console data
  }
}
// End of ServerChat class
