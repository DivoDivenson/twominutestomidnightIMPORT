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
	Invoice invoice; // Get data from this

	public Document(Invoice invoice) {
		this.invoice = invoice;
		open();

	}

	private void open() {

		try {
			File file = new File("src/data/tmp/invoice.ods");
			sheet = SpreadSheet.createFromFile(file).getSheet(0);
			// sheet.getCellAt("B12").setValue("Hello, this is a test");
			write();
			File outputFile = new File("src/data/invoice1.ods");
			sheet.getSpreadSheet().saveAs(outputFile);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void write() {
		sheet.getCellAt("B14").setValue(invoice.getEqupNo());
		sheet.getCellAt("E14").setValue(invoice.getCustomerRefer());
		sheet.getCellAt("B18").setValue(invoice.getBerth());
		// sheet.getCellAt("H14").setValue DATE HERE
		// sheet.getCellAt("B43").setValue DOCKET NUMBER
		sheet.getCellAt("F18").setValue(invoice.getSeal());
		//sheet.getCellAt("B22").setValue(invoice.getFrom());
		// sheet.getCellAt("F22").setValue(invoice.getTo());
		writeAddress(invoice.getTo(), "F");
		writeAddress(invoice.getFrom(), "B");
		sheet.getCellAt("H18").setValue(invoice.getWeight());
		sheet.getCellAt("D18").setValue(invoice.getSize());
		//sheet.getCellAt("B29").setValue(invoice.getDescript());
		writeDes();
		if (invoice instanceof InvoiceHaz) {
			// System.out.println("Haz");
			InvoiceHaz invoice = (InvoiceHaz) this.invoice;
			sheet.getCellAt("D34").setValue(
					new String("UN " + invoice.getUnNo() + ", "
							+ invoice.getName() + "," + invoice.getClass1()
							+ " " + invoice.getClass2() + ",PG"
							+ invoice.getPg() + "," + invoice.getTunnel()));
		}

	}

	private void writeAddress(String input, String col) {
		String[] address = input.split("\\n");
		if (address.length > 4) {
			for (int i = 4; i < address.length; i++) {
				address[3] = new String(address[3] + " " + address[i]);
			}
			for (int i = 0; i < 4; i++) {
				sheet.getCellAt(new String(col + (i + 22))).setValue(
						new String(" " + address[i]));
			}
		} else {
			for (int i = 0; i < address.length; i++) {
				sheet.getCellAt(new String(col + (i + 22))).setValue(
						new String(" " + address[i]));
			}
		}
	}
	
	private void writeDes(){
		String temp = invoice.getDescript();
		int i;
		if(temp.length() > 75){ //Half assed, but it'll work, mostly
			for(i = 74; i != 0; i--){
				if(temp.charAt(i) == ' '){
					break;
				}
			}
			sheet.getCellAt("B29").setValue(temp.substring(0, i));
			System.out.println(i);
			sheet.getCellAt("B30").setValue(temp.substring(i));
		}else{
			sheet.getCellAt("B29").setValue(temp);
		}
		
	}
}
