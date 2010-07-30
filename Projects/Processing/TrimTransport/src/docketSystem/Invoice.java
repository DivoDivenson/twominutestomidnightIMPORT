package docketSystem;
import java.util.Date;

public class Invoice {

	private String equpNo, size, berth, from, to, weight, customerRefer,
			descript, seal;
	private int docNo;
	Date date;


	
	public Invoice(){
		;
	}

	public String getEqupNo() {
		return equpNo;
	}

	public void setEqupNo(String equpNo) {
		this.equpNo = equpNo;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
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

	public String getSeal() {
		return seal;
	}

	public void setSeal(String seal) {
		this.seal = seal;
	}

}
