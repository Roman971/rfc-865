import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Rfc865TcpServer {

    static int QOTD_PORT = 17;
    static String QUOTE = "Goals transform a random walk into a chase. - Mihaly Csikszentmihalyi";

    public static void main(String[] args) {

        /* Open TCP socket */
        ServerSocket parentSocket = null;
        try {
            parentSocket = new ServerSocket(QOTD_PORT);
            System.out.println("TCP Server listening on port " + QOTD_PORT);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            while (true) {
                /* Wait to establish TCP connection with client */
                System.out.println("Waiting to establish connection with new client...");
                Socket childSocket = parentSocket.accept();
                System.out.println("Client conection request received");

                /* Create new thread to handle client connection */
                System.out.println("Creating thread to handle client connection...");
                ClientHandler client = new ClientHandler(childSocket);
                Thread thread = new Thread(client);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
        System.out.println("Established connection with client: " + socket.getInetAddress().getCanonicalHostName());
    }

    public void run() {
        try {
            /* Listen for TCP request from client */
            byte[] buf = new byte[512];
            InputStream is = this.socket.getInputStream();
            System.out.println("Waiting for request...");
            is.read(buf);

            /* Process the request */
            String requestContent = new String(buf);
            System.out.println("Received request: " + requestContent);

            /* Send TCP reply to client */
            String replyContent = Rfc865TcpServer.QUOTE;
            byte[] replyBuf = replyContent.getBytes("UTF-8");
            System.out.println("Reply content: " + replyContent);

            OutputStream os = this.socket.getOutputStream();
            System.out.println("Sending reply...");
            os.write(replyBuf);
            System.out.println("Reply sent");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
