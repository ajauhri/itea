package beans;

public class SubjectBean {
	int id;
	int class_id;
	String name;
	int lectures_held;
	int valid;

	public int getId() {
		return id;
	}

	public int getClass_id() {
		return class_id;
	}

	public String getName() {
		return name;
	}

	public int getLectures_held() {
		return lectures_held;
	}

	public int getValid() {
		return valid;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setClass_id(int class_id) {
		this.class_id = class_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLectures_held(int lectures_held) {
		this.lectures_held = lectures_held;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}
}
