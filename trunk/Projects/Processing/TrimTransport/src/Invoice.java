import java.util.Date;

public class Invoice {

	private String equpNo, payLen, berth, from, to, weight, customerRefer,
			descript;
	private int docNo;
	Date date;

	public Invoice(String equpNo, String paylen, String berth, String from,
			String to, String weight, String customerRefer, String descript,
			int docNo) {
		
		this.equpNo = equpNo;
		this.payLen = payLen;
		this.berth = berth;
		this.from = from;
		this.to = to;
		this.weight = weight;
		this.customerRefer = customerRefer;
		this.descript = descript;
		this.docNo = docNo;
		
		
		

	}

}
