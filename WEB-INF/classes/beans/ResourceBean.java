package beans;

public class ResourceBean {
	int id;
	int subject_id;
	String title;
	String description;
	String filename;
	String cl_filename;
	DateBean date;

	public int getId() {
		return id;
	}

	public int getSubject_id() {
		return subject_id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getFilename() {
		return filename;
	}

	public String getCl_filename() {
		return cl_filename;
	}

	public DateBean getDate() {
		return date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setCl_filename(String cl_filename) {
		this.cl_filename = cl_filename;
	}

	public void setDate(DateBean date) {
		this.date = date;
	}
}
