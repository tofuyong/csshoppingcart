import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {

        System.out.println("Starting shopping cart server on port 5000");
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Waiting for incoming connection");
        Socket conn = server.accept();
        System.out.println("Connection received");

        OutputStream os = conn.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = conn.getInputStream();
        DataInputStream dis = new DataInputStream(is);

        ArrayList<String> shoppingCart = new ArrayList<>();
        List<String> itemsList;
        String username = "";
        List<String> usernameDir = new ArrayList<>();
        boolean flag = true;
        boolean loadedCart = false;

        while (flag) {
            String input = dis.readUTF();
            int userSelection = Integer.parseInt(input);

            if (userSelection == 1) {
                if (shoppingCart.isEmpty()) {
                    dos.writeUTF("Your cart is empty");
                } else {
                    String indexedItems = "";
                    for (int i = 0; i < shoppingCart.size(); i++) {
                        indexedItems += ((i + 1) + ". " + shoppingCart.get(i).trim() + "\n");
                    }
                    dos.writeUTF(indexedItems);
                }
            }

            if (userSelection == 2) {
                dos.writeUTF("Input items you wish to add, separated by a space");
                String itemsToAdd = dis.readUTF();
                String[] itemsArray = itemsToAdd.trim().split(" ");
                itemsList = Arrays.asList(itemsArray);
                dos.writeUTF(itemsList + " added to cart");
                System.out.println(itemsList + " added to cart");
                shoppingCart.addAll(itemsList);
                System.out.println("Shopping cart contains: " + shoppingCart);

            }
            if (userSelection == 3) {
                dos.writeUTF("Input items you wish to delete by its number");
                String itemToDelete = dis.readUTF();
                int itemNumber = Integer.parseInt(itemToDelete);
                if (itemNumber <= shoppingCart.size()) {
                    dos.writeUTF(shoppingCart.get(itemNumber - 1) + " is removed");
                    shoppingCart.remove(shoppingCart.get(itemNumber - 1));
                } else {
                    dos.writeUTF("Item out of range, please select again");
                }
            }
            if (userSelection == 4) {
                dos.writeUTF("Exiting cart. Cart contains " + shoppingCart.size() + " items: " + shoppingCart);
                flag = false;
            }
            if (userSelection == 5) {
                if (username == "") {
                    dos.writeUTF("What is your username?");
                    username = dis.readUTF();
                }

                File file = new File("/Users/andreayong/IBFB2/csshoppingcart/shoppingcarts/" + username + ".cart");
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                        for (String groceries : shoppingCart) {
                            bw.write(groceries + "\n");
                        }
                }
                dos.writeUTF("Items saved in " + username + "'s shopping cart.");
                usernameDir.add(username);
                loadedCart = true;
            }
            if (userSelection == 6) {
                if (loadedCart == true) {
                    dos.writeUTF("Cart has been loaded. Select '1' to view cart items, '4' to exit cart.");
                } else {
                    dos.writeUTF("What is your username?");
                    username = dis.readUTF();
                    if (usernameDir.contains(username)) {
                        System.out.println("Saved cart found. Select '6' to load saved cart first before adding items");
                    } else {
                        File file = new File("/Users/andreayong/IBFB2/csshoppingcart/shoppingcarts/" + username + ".cart");

                        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                            String groceries;
                            while ((groceries = (br.readLine())) != null) {
                                shoppingCart.add(groceries);
                            }
                            loadedCart = true;
                            if (shoppingCart.isEmpty()) {
                                dos.writeUTF("Your cart is empty");
                            } else {
                                String indexedItems = "";
                                for (int i = 0; i < shoppingCart.size(); i++) {
                                    indexedItems += ((i + 1) + ". " + shoppingCart.get(i).trim() + "\n");
                                }
                                dos.writeUTF(indexedItems);
                            }
                        } catch (FileNotFoundException f){
                            System.out.println("User cart not found");
                        }
                    }
                }
            }
        }
        dos.close();
        dis.close();
        conn.close();
    }



}

