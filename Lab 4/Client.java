import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5004);
        
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        System.out.println("Client connected to the server on Handshaking port 5004");
        System.out.println("Client’s Communication Port: " + socket.getLocalPort());
        System.out.println("Client is Connected");


        FileInputStream f1 = new FileInputStream("input1.txt");
        FileInputStream f2 = new FileInputStream("input2.txt");
        FileInputStream f3 = new FileInputStream("input3.txt");

        boolean done1 = false, done2 = false, done3 = false;
              int round = 1;
        int N=3, T=2;
        while (!(done1 && done2 && done3)) {

            byte[] packet = new byte[N * T];

            String s1 = "", s2 = "", s3 = "";

            for (int i = 0; i < T; i++) {
                int b1 = -1;
                int b2 = -1;
                int b3 = -1;

                if (!done1) {
                    b1 = f1.read();
                }
                if (!done2) {
                    b2 = f2.read();
                }
                if (!done3) {
                    b3 = f3.read();
                }

                char c1,c2,c3;
                if (b1 == -1) {
                    c1 = '#';
                    done1 = true;
                } else {
                    c1 = (char) b1;
                }

                if (b2 == -1) {
                    c2 = '#';
                    done2 = true;
                } else {
                    c2 = (char) b2;
                }

                if (b3 == -1) {
                    c3 = '#';
                    done3 = true;
                } else {
                    c3 = (char) b3;
                }

                packet[i]         = (byte) c1;
                packet[i + T]     = (byte) c2;
                packet[i + 2 * T] = (byte) c3;

                s1 += c1;
                s2 += c2;
                s3 += c3;

                if (b1 == -1) done1 = true;
                if (b2 == -1) done2 = true;
                if (b3 == -1) done3 = true;
            }

            dos.write(packet);
            System.out.println("Round " + round + ": " + s1 + " " + s2 + " " + s3);
            round++;
        }

        f1.close();
        f2.close();
        f3.close();
        dos.close();
        socket.close();

    }
}