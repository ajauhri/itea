package beans;

public class BranchBean {
	int id;
	int course_id;
	String name;
	public int getId() {
		return id;
	}
	public int getCourse_id() {
		return course_id;
	}
	public String getName() {
		return name;
	}
	public String getHod() {
		return hod;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setCourse_id(int course_id) {
		this.course_id = course_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setHod(String hod) {
		this.hod = hod;
	}
	String hod;
}
