import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/*
    Handles clients connection to server
 */
public class Connection {
    private String username;
    private Socket client;
    private PrintStream sout;

    public Connection(String username, String addr, int port) {
        this.username = username;
        try {
            this.client = new Socket(addr, port);
            this.sout = new PrintStream(client.getOutputStream());
        } catch (IOException io) { io.printStackTrace(); }
    }

    public void close() {
        try {
            client.close();
        } catch (IOException io) { io.printStackTrace(); }
    }

    public void write(String message) { this.sout.println(username + ": " + message); }

    public Socket getClient() { return client; }
}