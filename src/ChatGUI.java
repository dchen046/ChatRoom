import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
    The user interface for the chat room
    Allows user to specify a username and the port number of the
    local server
 */
public class ChatGUI {
    private JFrame frame;
    private JTextArea chatlog, messageBox;
    private JButton send, exit;
    private JScrollPane scroll;
    private Connection conn;
    private static boolean open;

    public ChatGUI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setResizable(false);

        chatlog = new JTextArea();
        messageBox = new JTextArea();
        send = new JButton("Send");
        exit = new JButton("Quit");
        setButtonActions();
        chatlog.setEnabled(false);
        chatlog.setLineWrap(true);
        messageBox.setLineWrap(true);
        scroll = new JScrollPane(chatlog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel base = new JPanel(new GridLayout(2,1,0,5));
        JPanel bot = new JPanel(new GridLayout(1,2,5,0));
        JPanel butPanel = new JPanel(new GridLayout(2,1,0,5));
        base.add(scroll);
        base.add(bot);
        bot.add(messageBox);
        bot.add(butPanel);
        butPanel.add(send);
        butPanel.add(exit);
        frame.add(base);
    }

    public void show() {
        // get username and port before showing client
        ConnGUI connGUI = new ConnGUI();
        connGUI.show();
        String username = connGUI.getUsername();
        String addr = connGUI.getAddr();
        int port = connGUI.getPort();
        conn = new Connection(username, addr, port);
        frame.setTitle("Chat Client: " + username);
        frame.setVisible(true);
        open = true;
        new ProcessMessages(conn.getClient(), chatlog, scroll).start();
    }

    private void setButtonActions() {
        send.addActionListener(e -> {
            if (!messageBox.getText().equals("")) {
                String message = messageBox.getText();
                conn.write(message);
            }
            messageBox.setText("");
        });

        exit.addActionListener(e -> {
            open = false;
            conn.close();
            System.exit(0);
        });
    }

    private static class ProcessMessages extends Thread {
        private Socket client;
        private JTextArea chatlog;
        private JScrollPane scroll;

        public ProcessMessages(Socket client, JTextArea chatlog, JScrollPane scroll) {
            this.client = client;
            this.chatlog = chatlog;
            this.scroll = scroll;
        }

        public void run() {
            while (open) {
                try {
                    Scanner sin = new Scanner(client.getInputStream());;
                    while (sin.hasNextLine()) {
                        String message = sin.nextLine();
                        chatlog.append(message + "\n");
                        updateScroll();
                    }
                } catch (IOException io) {
                    System.out.println("IOException when processing messages");
                }
            }
        }

        private void updateScroll() {
            JScrollBar vert = scroll.getVerticalScrollBar();
            vert.setValue(vert.getMaximum());
        }
    }
}
