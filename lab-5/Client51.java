import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client51 {
    
    public static String xor(String a, String b) {
         String result="";
        for (int i = 1; i < b.length(); i++) {
            if(a.charAt(i)==b.charAt(i)){
                result += '0';
            }else{
                result +='1';
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
           
            Socket socket = new Socket("10.33.2.81", 5000);
            System.out.println("Client connected to the server on Handshaking port 5000");

           
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

           
            BufferedReader br = new BufferedReader(new FileReader("input.txt"));
            String data = br.readLine();
            br.close();

            System.out.println("File content: " + data);

           
            String binaryData ="";
            
            for (char ch : data.toCharArray()) {
                  binaryData+=(String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0'));
             }
            System.out.println("Converted Binary Data: " + binaryData);

           
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter generator polynomial (e.g. 10011): ");
            String generator = scanner.nextLine();
            scanner.close();

            int k = generator.length();

           
            String appendedData = binaryData;

            for(int i=0;i<k-1;i++){
                appendedData +='0';
            }
            System.out.println("After Appending zeros Data to Divide:" + appendedData);

           
            String remainder = mod2Div(appendedData, generator);

            System.out.println("CRC Remainder: " + remainder);

            
            String codeword = binaryData + remainder;
            System.out.println("Codeword to send: " + codeword);

           
            out.writeUTF(codeword);
            out.flush();

           
            socket.close();
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
