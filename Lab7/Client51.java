import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Client51 {
    
    

        public static void main(String[] args) {
        try {
           
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Client connected to the server on Handshaking port 5000");
            //String[] inputFiles={"input1.txt","intput2.txt"};

            String chip = "10110";
            double noise = 0.2; // 10% chance of bit flip

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

           
            List<Integer> spreadBits = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader("input1.txt"))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                String binary = String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0');
                System.out.println(binary);
                for (char bit : binary.toCharArray()) {
                    for (char c : chip.toCharArray()) {
                        spreadBits.add((bit - '0') ^ (c - '0')); 
                      }
                  }
              }
           }

           
            Random rand = new Random();
            for (int i = 0; i < spreadBits.size(); i++) {
                if (rand.nextDouble() < noise) {
                  
                    int flipped = spreadBits.get(i) == 0 ? 1 : 0;
                    spreadBits.set(i, flipped);
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("spread_input1.txt"))) {
            for (int bit : spreadBits) {
                writer.write(bit + " ");
            }
            }
           
            for (int bit : spreadBits) {
                out.writeInt(bit);
            }
            out.writeInt(-1);  
            out.flush();

            System.out.println("Spread bits sent to server.");

            out.close();
            socket.close();
            
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}