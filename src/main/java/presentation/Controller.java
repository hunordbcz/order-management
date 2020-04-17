package presentation;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import exceptions.OutOfStock;
import model.Client;
import model.Invoice;
import model.Order;
import model.Product;
import util.Constants;
import util.FileManager;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Controller {

    public Controller() {

    }

    public static void outOfStock() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        View<Order> pdf = new View<>();
        pdf.addMessage("Product is under-stock, can't be bought");
        pdf.print("unable-to-bill" + ts + ".pdf");
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

    public static void printOrder(Order order) {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());
        View<Order> pdf = new View<>();

        String[] names = new String[]{"Name", "Product", "Quantity", "Price", "Total"};
        String[] fields = new String[Constants.getOrderReportSize()];

        Invoice invoice = order.getX_invoices().get(0);
        Product product = invoice.getProductObj();
        Client client = order.getClientObj();

        fields[0] = client.getName();
        fields[1] = product.getName();
        fields[2] = Double.toString(invoice.getProduct_quantity());
        fields[3] = product.getPrice().toString();
        fields[4] = Double.toString(product.getPrice() * invoice.getProduct_quantity());

        pdf.addTableHeader(names);
        pdf.addRows(fields);
        pdf.print("invoice-" + ts + ".pdf");
    }

    private void reportClient() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        View<Client> pdf = new View<>();
        ClientBLL clientBLL = new ClientBLL();
        List<Client> clients = clientBLL.findAll();

        pdf.addTableHeader(clients.get(0));
        pdf.addRows(clients);
        pdf.print("report-client" + ts + ".pdf");
    }

    private void reportProduct() {
        Date date = new Date();
        Timestamp ts = new Timestamp(date.getTime());

        View<Product> pdf = new View<>();
        ProductBLL productBLL = new ProductBLL();
        List<Product> products = productBLL.findAll();

        pdf.addTableHeader(products.get(0));
        pdf.addRows(products);
        pdf.print("report-product" + ts + ".pdf");
    }

    private void reportOrder() {

    }

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
