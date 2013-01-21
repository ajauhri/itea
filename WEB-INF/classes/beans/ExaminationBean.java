package beans;

public class ExaminationBean {
	String id;
	String subject_id;
	String duration;
	DateBean db;
	TimeBean tb;

	public ExaminationBean() {
		setId(null);
		setSubjectId(null);
		setDuration(null);
		setDateBean(null);
		setTimeBean(null);
	}

	public String getId() {
		return id;
	}

	public String getSubjectId() {
		return subject_id;
	}

	public String getDuration() {
		return duration;
	}

	public DateBean getDateBean() {
		return db;
	}

	public TimeBean getTimeBean() {
		return tb;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSubjectId(String subject_id) {
		this.subject_id = subject_id;
	}

	public void setDuration(String duration) {
		this.duration = duration;

	}

	public void setDateBean(DateBean db) {
		this.db = db;

	}

	public void setTimeBean(TimeBean tb) {
		this.tb = tb;
	}

}
