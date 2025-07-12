import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static String xor(String a, String b) {
        String result = "";
        for (int i = 1; i < b.length(); i++) {
            result += (a.charAt(i) == b.charAt(i)) ? '0' : '1';
        }
        return result;
    }

    public static String mod2div(String data, String key) {
        int keylen = key.length();
        String part = data.substring(0, keylen);
        int curr = keylen;

        while (curr < data.length()) {
            if (part.charAt(0) == '1') {
                part = xor(key, part) + data.charAt(curr);
            } else {
                part = part.substring(1) + data.charAt(curr);
            }
            curr++;
        }

        if (part.charAt(0) == '1') {
            part = xor(key, part);
        } else {
            part = part.substring(1);
        }

        return part;
    }

    public static String flip(String data, int index) {
        char[] chars = data.toCharArray();

        if (chars[index] == '0')
            chars[index] = '1';
        else
            chars[index] = '0';
        return new String(chars);
    }

    public static String simulateError(String data, String type) {
        Random rand = new Random();

        if (type.equals("single")) {
            int index = rand.nextInt(data.length());
            System.out.println("Simulated single bit flip at position: " + index);
            return flip(data, index);
        } else if (type.equals("burst")) {
            int burstLength = 4; // fixed length burst
            int start = rand.nextInt(data.length() - burstLength + 1);
            System.out.println("Simulated burst error from position: " + start + " to " + (start + burstLength - 1));
            for (int i = 0; i < burstLength; i++) {
                data = flip(data, start + i);
            }
            return data;
        }

        return data;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is connected at port no: 5000");
            System.out.println("Server is connecting");
            System.out.println("Waiting for the client...");

            try (
                    Socket clientSocket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    Scanner sc = new Scanner(System.in)) {
                System.out.println("Client request accepted at port no: " + clientSocket.getPort());

                String received = reader.readLine();
                String generator = reader.readLine();

                System.out.println("Received Codeword: " + received);
                System.out.println("Using Generator Polynomial: " + generator);

                System.out.print("Do you want to simulate error (single/burst/none): ");
                String type = sc.nextLine().toLowerCase();

                if (!type.equals("none")) {
                    received = simulateError(received, type);
                }

                String remainder = mod2div(received, generator);
                System.out.println("Calculated Remainder: " + remainder);

                boolean hasError = false;
                for (char c : remainder.toCharArray()) {
                    if (c == '1') {
                        hasError = true;
                        break;
                    }
                }

                if (!hasError) {
                    System.out.println("No error detected in transmission.");
                } else {
                    System.out.println("Error detected in transmission!");

                    int found = -1;// no index found

                    for (int i = 0; i < received.length(); i++) {
                        String flipped = flip(received, i);
                        String testRemainder = mod2div(flipped, generator);

                        if (!testRemainder.contains("1")) {
                            if (found == -1) {
                                found = i;
                                break; // first index found
                            }
                        }
                    }

                    if (found >= 0) {
                        String corrected = flip(received, found);
                        System.out.println("Single-bit error corrected at position: " + found);
                        System.out.println("Corrected Codeword: " + corrected);
                    } else {
                        System.out.println("No single-bit correction possible. Error uncorrectable.");
                    }
                }
            }

            System.out.println("Server connection closed.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
