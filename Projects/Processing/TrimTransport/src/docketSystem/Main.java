package docketSystem;

public class Main {

	public static void main(String Args[]) {
		// Document doc = new Document();
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Interface().setVisible(true);
			}
		});

	}
}
