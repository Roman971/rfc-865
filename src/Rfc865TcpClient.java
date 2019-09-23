import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Rfc865TcpClient {

    static int QOTD_PORT = 17;
    static String SERVER_NAME = "localhost";

    public static void main(String[] args) {

        /* Establish TCP connection with server */
        Socket socket = null;
        try {
            InetAddress serverAddress = InetAddress.getByName(SERVER_NAME);
            socket = new Socket(serverAddress, QOTD_PORT);
            System.out.println("TCP Client connected on port " + QOTD_PORT + " to server: " + serverAddress.getCanonicalHostName());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            /* Prepare request content */
            String content = "Lasnier Roman Nicolas, TS3, " + InetAddress.getLocalHost().getHostAddress();
            byte[] buf = content.getBytes("UTF-8");
            System.out.println("Content to send: " + content);

            /* Send TCP request to server */
            OutputStream os = socket.getOutputStream();
            System.out.println("Writing request to output stream...");
            os.write(buf);
            System.out.println("Request sent to server");

            /* Receive TCP reply from server */
            byte[] replyBuf = new byte[512];
            InputStream is = socket.getInputStream();
            System.out.println("Reading reply from input stream...");
            is.read(replyBuf);

            /* Process the reply */
            String replyContent = new String(replyBuf);
            System.out.println("Received reply: " + replyContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
