import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        Socket clientConn = new Socket("", 5000);
        System.out.println("Connected to server on localhost:5000");

        OutputStream os = clientConn.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = clientConn.getInputStream();
        DataInputStream dis = new DataInputStream(is);

        System.out.println(showOptions());

        String input = "";

        while (!input.contains("4")) {
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            dos.writeUTF(input);
            dos.flush();
            System.out.println(dis.readUTF());
        }
        dis.close();
        is.close();
        dos.close();
        os.close();
        clientConn.close();
    }

    public static String showOptions() {
        return """
                Available actions:
                1 - List contents of cart
                2 - Add item(s) to cart separated by space
                3 - Delete item from cart by numbered item
                4 - Exit cart 
                5 - Save cart
                6 - Load saved cart
                Enter a number for the action you want to perform:
                ---------------------------------------------------""";
    }
}


