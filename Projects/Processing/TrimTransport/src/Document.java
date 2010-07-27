
import java.io.File;
import java.io.IOException;

import org.jopendocument.dom.OOUtils;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

public class Document {
	final int BUFFER = 2048;
	Sheet sheet;

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
