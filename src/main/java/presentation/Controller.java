package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import exceptions.OutOfStock;
import model.Client;
import model.Product;
import util.Constants;
import util.FileManager;

import java.io.IOException;

public class Controller {

    public Controller() {

    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public void insert(String model, String[] arguments) {
        ClientBLL clientBll = new ClientBLL();
        ProductBLL productBLL = new ProductBLL();

        String name, address;
        double quantity, price;

        switch (model) {
            case "client":
                name = arguments[0];
                address = arguments[1];
                clientBll.insert(name, address);
                break;
            case "product":
                name = arguments[0];
                quantity = Double.parseDouble(arguments[1]);
                price = Double.parseDouble(arguments[2]);
                productBLL.insert(name, quantity, price);
                break;
            default:
                break;
        }
    }

    public void delete(String model, String[] arguments) {
        ClientBLL clientBll = new ClientBLL();
        ProductBLL productBLL = new ProductBLL();

        String name = arguments[0];
        switch (model) {
            case "client":
                clientBll.delete(name);
                break;
            case "product":
                productBLL.delete(name);
                break;
            default:
                break;
        }
    }

    public void order(String[] arguments) {
        ClientBLL clientBLL = new ClientBLL();
        ProductBLL productBLL = new ProductBLL();
        OrderBLL orderBLL = new OrderBLL();

        Client client = clientBLL.findByName(arguments[0]);
        Product product = productBLL.findByName(arguments[1]);
        double quantity = Double.parseDouble(arguments[2]);

        try {
            orderBLL.make(client, product, quantity);
        } catch (OutOfStock outOfStock) {
            outOfStock.printStackTrace();
        }


    }

    public void report(String model) {

    }

    public void parseFile(String input) throws IOException {
        if (input != null && !input.isEmpty()) {
            Constants.setInputFile(input);
        }

        String line;
        while ((line = FileManager.input.readLine()) != null) {

            String[] statement = line.split(":");
            String[] arguments = null;
            if (statement.length > 1) {
                arguments = statement[1].split(",");
            }
            statement = statement[0].split(" ");
            String command = statement[0].toLowerCase();
            switch (command) {
                case "insert":
                    insert(statement[1].toLowerCase(), arguments);
                    break;
                case "delete":
                    delete(statement[1].toLowerCase(), arguments);
                    break;
                case "order":
                    order(arguments);
                    break;
                case "report":
                    report(statement[1].toLowerCase());
                default:
                    break;
            }


        }
    }
}
