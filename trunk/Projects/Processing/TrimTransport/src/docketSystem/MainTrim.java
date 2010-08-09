package docketSystem;

import java.io.File;

import javax.swing.UIManager;

public class MainTrim {

	public static void main(String Args[]) {
		// Document doc = new Document();
		setNativeLookAndFeel();
		File file = new File("");
		System.out.println(file.getAbsolutePath());
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Interface().setVisible(true);
			}
		});

	}
	
	 public static void setNativeLookAndFeel() {
		    try {
		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } catch(Exception e) {
		      System.out.println("Error setting native LAF: " + e);
		    }
	 }
}
