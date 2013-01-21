package beans;

public class FacultyBean {
	String gr_no;
	int branch_id;
	int fac_type;
	PersonalDetailBean personal_details;
	PreferenceBean preferences;
	int year_of_joining;
	FeedbackBean feedback;
	public String getGr_no() {
		return gr_no;
	}
	public int getBranch_id() {
		return branch_id;
	}
	public int getFac_type() {
		return fac_type;
	}
	public PersonalDetailBean getPersonal_details() {
		return personal_details;
	}
	public PreferenceBean getPreferences() {
		return preferences;
	}
	public int getYear_of_joining() {
		return year_of_joining;
	}
	public FeedbackBean getFeedback() {
		return feedback;
	}
	public void setGr_no(String gr_no) {
		this.gr_no = gr_no;
	}
	public void setBranch_id(int branch_id) {
		this.branch_id = branch_id;
	}
	public void setFac_type(int fac_type) {
		this.fac_type = fac_type;
	}
	public void setPersonal_details(PersonalDetailBean personal_details) {
		this.personal_details = personal_details;
	}
	public void setPreferences(PreferenceBean preferences) {
		this.preferences = preferences;
	}
	public void setYear_of_joining(int year_of_joining) {
		this.year_of_joining = year_of_joining;
	}
	public void setFeedback(FeedbackBean feedback) {
		this.feedback = feedback;
	}
}