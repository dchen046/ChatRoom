import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/*
    The chat server that accepts connections and
    relays messages to all connected clients
 */
public class ChatServer extends Thread {
    private ServerSocket serverSocket;
    private static HashSet<PrintStream> souts;

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        souts = new HashSet<>();
    }

    public void run() {
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                PrintStream sout = new PrintStream(clientSocket.getOutputStream());
                souts.add(sout);
                new ProcessConnections(clientSocket).start();
            }
        } catch (IOException io) { io.printStackTrace(); }
    }

    // class to handle client connections
    private static class ProcessConnections extends Thread {
        private Socket client;
        private PrintStream sout;
        private Scanner sin;

        public ProcessConnections(Socket client) {
            this.client = client;
        }

        public void run() {
            try {
                sout = new PrintStream(client.getOutputStream());
                sin = new Scanner(client.getInputStream());
                while (sin.hasNextLine()) {
                    String message = sin.nextLine();
                    // send message to every client
                    for (PrintStream ps : souts){
                        ps.println(message);
                    }
                }
            } catch (IOException io) {
                System.out.println("IOException when processing connection on server");
            } finally {
                if (sout != null) {
                    souts.remove(sout);
                }
                try {
                    client.close();
                } catch (IOException io){
                    System.out.println("Error when closing a client");
                }
            }
        }
    }
}
