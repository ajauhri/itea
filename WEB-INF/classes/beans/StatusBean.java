package beans;

public class StatusBean {
	String valid;
	
	public StatusBean() {
		setStatus(null);
		
	}

	public String getStatus() {
		return valid;
	}

	public void setStatus(String valid) {
		this.valid = valid;
	}

}
