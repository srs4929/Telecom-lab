import java.util.*;
import java.io.*;
import java.net.*;

public class Server {

    public static String destuffString(String x) {
        String result = "";
        int count = 0;
        for (int i = 0; i < x.length(); i++) {

            result += x.charAt(i);
            if (x.charAt(i) == '1') {
                count++;
                if (count == 5) {
                    i++; // skip the count
                    count = 0;
                }
            } else {
                count = 0;
            }

        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server is connected at port no: " + server.getLocalPort());
        System.out.println("Server is connecting\n");
        System.out.println("Waiting for the client\n");
        Socket s = server.accept();
        DataInputStream input = new DataInputStream(s.getInputStream());
        String clientmsg=input.readUTF();
        System.out.println("Client message was "+clientmsg);
        String result =destuffString(clientmsg);
        System.out.println("Destuffed one "+result);
        server.close();
        s.close();
        input.close();

    }

}
