package start;

import presentation.Controller;

import java.io.IOException;

public class Start {
	public static void main(String[] args) {

		Controller controller = new Controller();
		try {
			if (args.length > 0) {
				controller.parseFile(args[0]);
			} else {
				controller.parseFile(null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}