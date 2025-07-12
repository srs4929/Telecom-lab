import java.io.*;
import java.net.*;
import java.util.Random;


public class Server7 {

    public static String xor(String a, String b) {

        String result = "";
        for (int i = 1; i < b.length(); i++) {
            if (a.charAt(i) == b.charAt(i))
                result += '0';
            else
                result += '1';

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

    public static String flip(String data) {
        Random rand = new Random();
        int index = rand.nextInt(data.length());
        
        char flipped;
        if (data.charAt(index) == '0')
            flipped = '1';
        else
            flipped = '0';
        String newData = data.substring(0, index) + flipped + data.substring(index + 1);
        System.out.println("Simulated bit flip at position: " + index);
        return newData;
    }

    public static void main(String[] args) {
        String generator = "1101";

        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is connected at port no: 5000");
            System.out.println("Waiting for the client...");

            try (Socket clientSocket = serverSocket.accept();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                System.out.println("Client request accepted at port no: " + clientSocket.getPort());

                String received = reader.readLine();
                System.out.println("Received Codeword: " + received);

                // Uncomment below to simulate error
                Random r=new Random();
                int x=r.nextInt(5)+1;
                if(x<5){
                received = flip(received);
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

                if (hasError) {
                    System.out.println("Error detected in transmission!");
                } else {
                    System.out.println("No error detected in transmission.");
                }

            }

            System.out.println("Server connection closed.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
