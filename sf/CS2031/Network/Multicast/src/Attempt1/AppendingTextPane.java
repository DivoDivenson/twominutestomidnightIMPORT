package Attempt1;

import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class AppendingTextPane extends JTextPane{

	public AppendingTextPane(){
		super();
	}

	public void appendText(String text){
		try {
			Document doc = getDocument();

			// Move the insertion point to the end
			setCaretPosition(doc.getLength());

			// Insert the text
			replaceSelection(text);

			// Convert the new end location
			// to view co-ordinates
			Rectangle r = modelToView(doc.getLength());

			// Finally, scroll so that the new text is visible
			if (r != null) {
				scrollRectToVisible(r);
			}
		} catch (BadLocationException e) {
			System.out.println("Failed to append text: " + e);
		}
	}



}



