package beans;

public class StudentBean {
	String gr_no;
	int roll_no;
	int class_id;
	PersonalDetailBean personal_details;
	int year_of_admission;
	MessageBean feedback;
	public String getGr_no() {
		return gr_no;
	}
	public int getRoll_no() {
		return roll_no;
	}
	public int getClass_id() {
		return class_id;
	}
	public PersonalDetailBean getPersonal_details() {
		return personal_details;
	}
	public int getYear_of_admission() {
		return year_of_admission;
	}
	public MessageBean getFeedback() {
		return feedback;
	}
	public void setGr_no(String gr_no) {
		this.gr_no = gr_no;
	}
	public void setRoll_no(int roll_no) {
		this.roll_no = roll_no;
	}
	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}
	public void setPersonal_details(PersonalDetailBean personal_details) {
		this.personal_details = personal_details;
	}
	public void setYear_of_admission(int year_of_admission) {
		this.year_of_admission = year_of_admission;
	}
	public void setFeedback(MessageBean feedback) {
		this.feedback = feedback;
	}
}