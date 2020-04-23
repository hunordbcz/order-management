package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import model.Client;
import model.Invoice;
import model.Order;
import model.Product;
import util.Constants;
import util.FileManager;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Controller {

    public Controller() {

    }

    /**
     * Prints out a message of under-stock in case if an order can't be made due to under-stock
     */
    public static void outOfStock() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        View<Order> pdf = new View<>();
        pdf.addMessage("Product is under-stock, can't be bought");
        pdf.print("unable-to-bill" + ts + ".pdf");
    }

    /**
     * Prints an invoice for a given order
     *
     * @param order The order which contains the details
     */
    public static void printOrder(Order order) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        View<Order> pdf = new View<>();

        String[] names = new String[]{"Name", "Product", "Quantity", "Price", "Total"};
        String[] fields = new String[Constants.getOrderReportSize()];

        getOrderFields(fields, order);

        pdf.addTableHeader(names);
        pdf.addRows(fields);
        pdf.print("invoice-" + ts + ".pdf");
    }

    /**
     * Gets the human-readable field names for an order
     *
     * @param fields The result string array
     * @param order  The given order to process
     */
    private static void getOrderFields(String[] fields, Order order) {
        Invoice invoice = order.getX_invoices().get(0);
        Product product = invoice.getProductObj();
        Client client = order.getClientObj();

        fields[0] = client.getName();
        fields[1] = product.getName();
        fields[2] = Double.toString(invoice.getProduct_quantity());
        fields[3] = product.getPrice().toString();
        fields[4] = Double.toString(product.getPrice() * invoice.getProduct_quantity());
    }

    /**
     * Processes the insert command from the input file
     *
     * @param model     The type of the object that will be inserted into the DB
     * @param arguments The fields that are needed for inserting the object
     */
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

    /**
     * Processes the delete command from the input file
     *
     * @param model     The type of the object that will be deleted from the DB
     * @param arguments The fields that are needed to find the object
     */
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

    /**
     * Processes the order command from the input file
     * @param arguments The fields that describe the order that will be made
     */
    public void order(String[] arguments) {
        ClientBLL clientBLL = new ClientBLL();
        ProductBLL productBLL = new ProductBLL();
        OrderBLL orderBLL = new OrderBLL();

        Client client = clientBLL.findByName(arguments[0].trim());
        Product product = productBLL.findByName(arguments[1].trim());
        double quantity = Double.parseDouble(arguments[2].trim());

        orderBLL.make(client, product, quantity);
    }

    /**
     * Lists every client from the database with their details
     */
    private void reportClient() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        View<Client> pdf = new View<>();
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAll();

        if (clients.isEmpty()) {
            pdf.addMessage("No clients were found");
        } else {
            pdf.addTableHeader(clients.get(0));
            pdf.addRows(clients);
        }

        pdf.print("report-client" + ts + ".pdf");
    }

    /**
     * Lists every product from the database with their details
     */
    private void reportProduct() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        View<Product> pdf = new View<>();
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAll();

        if (products.isEmpty()) {
            pdf.addMessage("No products were found");
        } else {
            pdf.addTableHeader(products.get(0));
            pdf.addRows(products);
        }

        pdf.print("report-product" + ts + ".pdf");
    }

    /**
     * Lists every order from the database with their details
     */
    private void reportOrder() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        View<Order> pdf = new View<>();
        OrderBLL orderBLL = new OrderBLL();

        String[] names = new String[]{"Name", "Product", "Quantity", "Price", "Total"};
        String[] fields = new String[Constants.getOrderReportSize()];

        pdf.addTableHeader(names);

        List<Order> orders = orderBLL.findAll();
        for (Order order : orders) {
            getOrderFields(fields, order);
            pdf.addRows(fields);
        }

        pdf.print("report-order" + ts + ".pdf");
    }

    /**
     * Processes the report command
     *
     * @param model The given model which should be reported
     */
    public void report(String model) {
        switch (model) {
            case "client":
                reportClient();
                break;
            case "product":
                reportProduct();
                break;
            case "order":
                reportOrder();
                break;
            default:
                break;
        }
    }

    /**
     * Parses and processes the input commands
     *
     * @param input The name of the input file
     * @throws IOException Thrown on wrong input file name
     */
    public void parseFile(String input) throws IOException {
        if (input != null && !input.isEmpty()) {
            Constants.setInputFile(input);
        }

        String line;
        while ((line = FileManager.input.readLine()) != null) {

            String[] statement = line.split(":");
            String[] arguments = null;
            if (statement.length > 1) {
                arguments = Arrays.stream(statement[1].split(",")).map(String::trim).toArray(String[]::new);
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
