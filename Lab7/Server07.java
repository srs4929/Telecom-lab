import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server07 {

    public static void main(String[] args) {
        int port = 5000;
        String chip = "10110";

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);

            try (Socket clientSocket = serverSocket.accept();
                    DataInputStream reader = new DataInputStream(clientSocket.getInputStream())) {

                System.out.println("Client connected.");

                List<Integer> received = new ArrayList<>();
                int bit;

                while ((bit = reader.readInt()) != -1) {
                    if (bit == 0 || bit == 1) {
                        received.add(bit);
                    } else {
                        System.out.println("Received invalid bit: " + bit);
                    }
                }

                try (BufferedWriter spreadWriter = new BufferedWriter(new FileWriter("spread.txt"))) {
                    for (int bitValue : received) {

                        if (bitValue == 1)
                            spreadWriter.write('1');
                        else
                            spreadWriter.write('0');
                    }
                }
                System.out.println("Spread bits saved to spread_input1.txt");

                int chiplen = chip.length();
                List<Integer> original = new ArrayList<>();

                for (int i = 0; i + chiplen <= received.size(); i += chiplen) {
                    int ones = 0;

                    for (int j = 0; j < chiplen; j++) {
                        int spreadBit = received.get(i + j);
                        int chipcode = chip.charAt(j) - '0';

                        int xor = spreadBit ^ chipcode;

                        if (xor == 1) {
                            ones++;
                        }
                    }

                    if (ones > chiplen / 2) {
                        original.add(1);
                    } else {
                        original.add(0);
                    }
                }

                StringBuilder recovered = new StringBuilder();
                for (int i = 0; i + 8 <= original.size(); i += 8) {
                    int ascii = 0;
                    for (int j = 0; j < 8; j++) {
                        ascii = (ascii << 1) | original.get(i + j);
                    }
                    recovered.append((char) ascii);
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("recovered.txt"))) {
                    writer.write(recovered.toString());
                }

                System.out.println("Recovered text saved to recovered_input1.txt");
                System.out.println("Recovered message:\n" + recovered);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}