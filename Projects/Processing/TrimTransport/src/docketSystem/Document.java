package docketSystem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.jopendocument.model.OpenDocument;
import org.jopendocument.panel.ODSViewerPanel;
import org.jopendocument.print.DefaultDocumentPrinter;


/**
 * Takes the information stored in an invoice and writes it to and .ods (Open document spreadsheet).
 * @author divo
 *
 */
public class Document {
	final int BUFFER = 2048;
	Sheet sheet;
	OpenDocument doc;
	Invoice invoice; // Get data from this
	Date date;

	public Document(Invoice invoice) {
		this.invoice = invoice;
		date = new Date();
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

	/**
	 * Write the Invoice out to an .ods file
	 */
	private void write() {
		writeDate();
		sheet.getCellAt("B14").setValue(formatCell(invoice.getEqupNo()));
		sheet.getCellAt("E14").setValue(formatCell(invoice.getCustomerRefer()));
		sheet.getCellAt("B18").setValue(formatCell(invoice.getBerth()));
		sheet.getCellAt("F18").setValue(formatCell(invoice.getSeal()));
		writeAddress(invoice.getTo(), "F");
		writeAddress(invoice.getFrom(), "B");
		sheet.getCellAt("H18").setValue(formatCell(invoice.getWeight()));
		sheet.getCellAt("D18").setValue(formatCell(invoice.getSize()));
		if(invoice.isReturnEmpty()){
			sheet.getCellAt("B34").setValue(formatCell("Yes"));
		}else{
			sheet.getCellAt("B34").setValue(formatCell("No"));
		}
		writeDes();
		if (invoice instanceof InvoiceHaz) {
			// System.out.println("Haz");
			InvoiceHaz invoice = (InvoiceHaz) this.invoice;
			sheet.getCellAt("D34").setValue(
					new String("UN " + invoice.getUnNo() + ", "
							+ invoice.getName() + "," + invoice.getClass1()
							+ " " + invoice.getClass2() + ",PG"
							+ invoice.getPg() + "," + invoice.getTunnel()));
		}else{
			sheet.getCellAt("D34").setValue(formatCell("No"));
		}

	}

	/*
	 * Write to the address boxes. Since the ODS libs have trouble with multiple lines the text is broken up
	 * (based on the \n chars) and placed in cells underneath eachother. If there are two many lines
	 * the extra ones are shoved into the last cell because I'm lazy
	 */
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
	//Write to the description line. Cuts the string up based on an arbitray number of characters....
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
	
	private void writeDate(){
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		sheet.getCellAt("H14").setValue(format.format(date));
	}
	
	//Pads out the input with a space at the start. Make the text look prettier :3
	private String formatCell(String input){
		return new String(" " + input);
	}
}
