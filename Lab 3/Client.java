import java.util.*;
import java.io.*;
import java.net.*;

public class Client {

    public static String bitstuffing(String data) {
        String result = "";
        int count = 0;
        for (int i = 0; i < data.length(); i++) {
            result += data.charAt(i);
            if (data.charAt(i) == '1') {
                count++;
                if (count == 5) {
                    result += '0';
                }
            } else {
                count = 0;
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 5000);
        System.out.println("Client connected at server handshaking port " + s.getPort());
        System.out.println("Client’s communication port: " + s.getLocalPort());
        System.out.println("Client is connected");

        BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
        DataOutputStream output = new DataOutputStream(s.getOutputStream());
        String str = "";

        while ((str = reader.readLine()) != null) {
            String stuffed = bitstuffing(str);
            output.writeUTF(stuffed);
            System.out.println("Real message was " + str);
            System.out.println("Stuffed msg was "+stuffed);

        }
       
        
        output.close();
        s.close();
        reader.close();

    }

}
