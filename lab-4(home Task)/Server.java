import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(5004)) {
            System.out.println("Server is waiting...");

            try (Socket socket = serverSocket.accept();
                 DataInputStream dis = new DataInputStream(socket.getInputStream());
                 FileOutputStream f1 = new FileOutputStream("output1.txt");
                 FileOutputStream f2 = new FileOutputStream("output2.txt");
                 FileOutputStream f3 = new FileOutputStream("output3.txt")) {

                System.out.println("Client connected.");

                while (true) {
                    int frameSize = dis.readInt();  // will throw EOFException when done
                    byte[] frame = new byte[frameSize];
                    dis.readFully(frame);

                    System.out.print("Received frame: ");
                    for (int i = 0; i < frame.length; i += 2) {
                        int streamId = frame[i];
                        byte data = frame[i + 1];
                        char datac=(char) data;
                        System.out.print("[" + streamId + ":" + datac + "] ");

                        if (streamId == 1) f1.write(data);
                        else if (streamId == 2) f2.write(data);
                        else if (streamId == 3) f3.write(data);
                    }
                    System.out.println();
                }

            } // all resources automatically closed here

        }
    }
}
