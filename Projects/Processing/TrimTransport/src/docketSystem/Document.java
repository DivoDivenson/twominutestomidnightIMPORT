package docketSystem;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.jopendocument.model.OpenDocument;
import org.jopendocument.panel.ODSViewerPanel;
import org.jopendocument.print.DefaultDocumentPrinter;

public class Document {
	final int BUFFER = 2048;
	Sheet sheet;
	OpenDocument doc;

	public Document() {
		open();

	}

	private void open() {

		try {
			File file = new File("src/data/tmp/invoice.ods");
			sheet = SpreadSheet.createFromFile(file).getSheet(0);
			sheet.getCellAt("B12").setValue("Hello, this is a test");
			File outputFile = new File("src/data/invoice1.ods");
			sheet.getSpreadSheet().saveAs(outputFile);

			final OpenDocument doc = new OpenDocument();
			doc.loadFrom("src/data/tmp/invoice.ods");

			// Show time !
			final JFrame mainFrame = new JFrame("Viewer");
			DefaultDocumentPrinter printer = new DefaultDocumentPrinter();

			ODSViewerPanel viewerPanel = new ODSViewerPanel(doc, printer, true);

			mainFrame.setContentPane(viewerPanel);
			mainFrame.pack();
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setVisible(true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
