package docketSystem;
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
	
	public Invoice(){
		;
	}

	public String getEqupNo() {
		return equpNo;
	}

	public void setEqupNo(String equpNo) {
		this.equpNo = equpNo;
	}

	public String getPayLen() {
		return payLen;
	}

	public void setPayLen(String payLen) {
		this.payLen = payLen;
	}

	public String getBerth() {
		return berth;
	}

	public void setBerth(String berth) {
		this.berth = berth;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCustomerRefer() {
		return customerRefer;
	}

	public void setCustomerRefer(String customerRefer) {
		this.customerRefer = customerRefer;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public int getDocNo() {
		return docNo;
	}

	public void setDocNo(int docNo) {
		this.docNo = docNo;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
