import main.Controller;

public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Controller controller = new Controller();
		Controller.setup();
		controller.test();
		Controller.tearDown();
	}
}
