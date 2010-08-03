package docketSystem;

public class InvoiceHaz extends Invoice {

	private String name, tunnel, pg, class1, class2, unNo; //All strings cous im lazy and fuck it

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTunnel() {
		return tunnel;
	}

	public void setTunnel(String tunnel) {
		this.tunnel = tunnel;
	}

	public String getPg() {
		return pg;
	}

	public void setPg(String pg) {
		this.pg = pg;
	}

	public String getClass1() {
		return class1;
	}

	public void setClass1(String class1) {
		this.class1 = class1;
	}

	public String getClass2() {
		if((class2.compareTo("None") != 0)){
			return new String("(" + class2 + ")");
		}else{
			return new String("");
		}
	}

	public void setClass2(String class2) {
		this.class2 = class2;
	}

	public String getUnNo() {
		return unNo;
	}

	public void setUnNo(String unNo) {
		this.unNo = unNo;
	}
}
