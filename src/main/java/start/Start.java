package start;

import presentation.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Start {
	protected static final Logger LOGGER = Logger.getLogger(Start.class.getName());

	public static void main(String[] args) throws SQLException {

		Controller controller = new Controller();
		try {
			controller.parseFile(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		StudentBLL studentBll = new StudentBLL();
//		OrderBLL orderBLL = new OrderBLL();
//		ProductBLL productBLL = new ProductBLL();
//		InvoiceBLL invoiceBLL = new InvoiceBLL();
//		try {
//			Order order = orderBLL.findById(1);
//			Product product = productBLL.findById(1);
//			Invoice invoice = invoiceBLL.findById(1);
//			productBLL.insert(product);
//			Document document = new Document();
//			PdfWriter.getInstance(document, new FileOutputStream("iTextHelloWorld.pdf"));
//
//			document.open();
//			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
//			Chunk chunk = new Chunk("Hello World", font);
//
//			document.add(chunk);
//			document.close();
//			System.out.println("x");
////			student1 = studentBll.findById(1245);
////			student1.setId(1);
////			studentBll.insert(student1);
////			student1.setId(20);
////			studentBll.update(student1);
//		} catch (Exception ex) {
//			LOGGER.log(Level.INFO, ex.getMessage());
//		}

	}

}
