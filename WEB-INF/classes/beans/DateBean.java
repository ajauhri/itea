package beans;

import java.util.StringTokenizer;

public class DateBean {
	String dd;
	String mm;
	String yyyy;

	public DateBean(){
		setDd(null);
		setMm(null);
		setYyyy(null);
	}
	
	public DateBean(String date){
		StringTokenizer st=new StringTokenizer(date,"/");
		setDd(st.nextToken());
		setMm(st.nextToken());
		setYyyy(st.nextToken());
	}

	public String toString(){
		return getDd()+"/"+getMm()+"/"+getYyyy();
	}
	
	public String getDd() {
		return dd;
	}

	public String getMm() {
		return mm;
	}

	public String getYyyy() {
		return yyyy;
	}

	public void setDd(String dd) {
		this.dd = dd;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

	public void setYyyy(String yyyy) {
		this.yyyy = yyyy;
	}
}
