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
	Invoice invoice; //Get data from this

	public Document(Invoice invoice) {
		this.invoice = invoice;
		open();

	}

	private void open() {

		try {
			File file = new File("src/data/tmp/invoice.ods");
			sheet = SpreadSheet.createFromFile(file).getSheet(0);
			//sheet.getCellAt("B12").setValue("Hello, this is a test");
			write();
			File outputFile = new File("src/data/invoice1.ods");
			sheet.getSpreadSheet().saveAs(outputFile);

			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void write(){
		/*sheet.getCellAt("B14").setValue(invoice.getEqupNo());
		sheet.getCellAt("E14").setValue(invoice.getSize());
		sheet.getCellAt("G14").setValue(invoice.getBerth());
		sheet.getCellAt("B18").setValue(invoice.getFrom());
		sheet.getCellAt("F18").setValue(invoice.getTo());
		sheet.getCellAt("F26").setValue(invoice.getWeight());
		sheet.getCellAt("F26").setValue(invoice.getCustomerRefer());
		sheet.getCellAt("B30").setValue(invoice.getDescript());*/
		
	}
}
