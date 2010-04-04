package Attempt1;

import javax.swing.UIManager;

public class Main {

	public static void main(String Args[]){
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception evt) {}
		new Interface().setVisible(true);
	}
}
