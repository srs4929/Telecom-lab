import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5004); // use server IP if needed
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        FileInputStream f1 = new FileInputStream("input1.txt");
        FileInputStream f2 = new FileInputStream("input2.txt");
        FileInputStream f3 = new FileInputStream("input3.txt");

        boolean done1 = false, done2 = false, done3 = false;
        int round = 1;
        int N=3;
    
       
        while (!(done1 && done2 && done3)) {

            // Max N stream ID + data pairs → N*2 bytes max
            byte[] frame = new byte[N*2];
            int idx = 0;

            if (!done1) {
                int b = f1.read();
                if (b != -1) {
                    frame[idx++] = 1;          // Stream ID
                    frame[idx++] = (byte) b;   // Data
                } else {
                    done1 = true;
                }
            }

            if (!done2) {
                int b = f2.read();
                if (b != -1) {
                    frame[idx++] = 2;
                    frame[idx++] = (byte) b;
                } else {
                    done2 = true;
                }
            }

            if (!done3) {
                int b = f3.read();
                if (b != -1) {
                    frame[idx++] = 3;
                    frame[idx++] = (byte) b;
                } else {
                    done3 = true;
                }
            }

            
            // Send only the part of the array that's filled
            dos.writeInt(idx);           // First send frame size
            dos.write(frame, 0, idx);    // Then send that many bytes
            System.out.println("Round " + round +" frame : " );
           
             for (int i = 0; i < idx; i += 2) {
             int streamId = frame[i];
             char datac = (char) frame[i + 1];
             System.out.print("[" + streamId + ":" + datac + "] ");
             }
             System.out.println();
             round++;
        }

        f1.close();
        f2.close();
        f3.close();
        dos.close();
        socket.close();
        System.out.println("Client done sending all data.");
    }
}
