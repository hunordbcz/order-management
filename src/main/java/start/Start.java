package start;

import presentation.Controller;

import java.io.IOException;

public class Start {
	public static void main(String[] args) {

		Controller controller = new Controller();
		try {
			controller.parseFile(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}