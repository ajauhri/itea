package beans;

public class TranscriptBean {
	String subject;
	int marks;
	public TranscriptBean(){
		setSubject(null);
		setMarks(0);
	}
	public String getSubject() {
		return subject;
	}
	public int getMarks() {
		return marks;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setMarks(int marks) {
		this.marks = marks;
	}
}
