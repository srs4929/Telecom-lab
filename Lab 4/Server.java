import java.io.*;
import java.net.*;

public class Server{
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5004);
        System.out.println("Server is waiting on port 5004...");
        System.out.println("Server is connecting");
        System.out.println("Waiting for the client");

        int t = 2;
        Socket socket = serverSocket.accept();
        System.out.println("Client request is accepted at port no " + socket);
        System.out.println("Server's communication port " + serverSocket);

        DataInputStream data = new DataInputStream(socket.getInputStream());

        FileOutputStream file1 = new FileOutputStream("output1.txt");
        FileOutputStream file2 = new FileOutputStream("output2.txt");
        FileOutputStream file3 = new FileOutputStream("output3.txt");

        byte[] packet = new byte[3 * t]; 

        while (data.read(packet) != -1) {

            System.out.print("Received packet: ");
            for (int i = 0; i < packet.length; i++) {
                System.out.print((char) packet[i]);
            }
            System.out.println();

            for (int i = 0; i < packet.length; i++) {
                if (packet[i] != '#') {
                    if (i < t) {
                        file1.write(packet[i]);
                    } else if (i < 2 * t) {
                        file2.write(packet[i]);
                    } else {
                        file3.write(packet[i]);
                    }
                }
            }
        }

        file1.close();
        file2.close();
        file3.close();
        data.close();
        socket.close();
        serverSocket.close();

        System.out.println("Data received. Files written.");
    }
}