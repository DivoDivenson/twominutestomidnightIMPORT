package Net;

/*
Steven Diviney 08462267
Eoin McCarthy 08484333
Mark O'Meara 08332240
*/


import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class AppendingTextPane extends JTextPane{

	public AppendingTextPane(){
		super();
	}

	public void appendText(String text){

		Document doc = getDocument();

		// Move the insertion point to the end
		setCaretPosition(doc.getLength());

		// Insert the text
		replaceSelection(text);


	}



}



