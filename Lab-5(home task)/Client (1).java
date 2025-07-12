import java.io.*;
import java.net.*;
import java.util.*;

public class Client {

    public static String xor(String a, String b) {
        String result = "";
        for (int i = 1; i < b.length(); i++) {
            if (a.charAt(i) == b.charAt(i)) {
                result += '0';
            } else {
                result += '1';
            }
        }
        return result;
    }

    public static String mod2Div(String dividend, String divisor) {
        int curr = divisor.length();
        String tmp = dividend.substring(0, curr);

        while (curr < dividend.length()) {
            if (tmp.charAt(0) == '1') {
                tmp = xor(divisor, tmp) + dividend.charAt(curr);
            } else {
                tmp = tmp.substring(1) + dividend.charAt(curr);
            }
            curr++;
        }

        if (tmp.charAt(0) == '1') {
            tmp = xor(divisor, tmp);
        } else {
            tmp = tmp.substring(1);
        }

        return tmp;
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Client connected to the server on Handshaking port 5000");

            System.out.println("Client Communication Port: " + socket.getLocalPort());
            System.out.println("Client is connected");
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            BufferedReader br = new BufferedReader(new FileReader("input.txt"));
            String data = br.readLine();
            br.close();

            System.out.println("File content: " + data);

            String binaryData = "";
            for (char ch : data.toCharArray()) {
                binaryData += (String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0'));
            }

            System.out.println("Converted Binary Data: " + binaryData);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter CRC type (8 / 10 / 16 / 32): ");
            int type = scanner.nextInt();
            scanner.close();

            String generator = "";
            if (type == 8) {
                generator = "100000111"; // CRC-8
            } else if (type == 10) {
                generator = "11000110011"; // CRC-10
            } else if (type == 16) {
                generator = "11000000000000101"; // CRC-16
            } else if (type == 32) {
                generator = "100000100110000010001110110110111"; // CRC-32
            } else {
                System.out.println("Invalid CRC type selected.");
                return;
            }

            String appendedData = binaryData;
            for (int i = 0; i < generator.length() - 1; i++) {
                appendedData += '0';
            }

            System.out.println("After Appending zeros Data to Divide: " + appendedData);

            String remainder = mod2Div(appendedData, generator);
            System.out.println("CRC Remainder: " + remainder);

            String codeword = binaryData + remainder;
            System.out.println("Codeword to send: " + codeword);

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println(codeword); 
            output.println(generator); 

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
