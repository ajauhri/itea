package beans;

public class MessageBean {
	String id;
	String sender;
	int msg_type;
	String recipient;
	String rec_type;
	String content;
	String subject;
	DateBean date;

	public MessageBean() {
		setId(null);
		setSender(null);
		setMsg_type(0);
		setRecipient(null);
		setRec_type(null);
		setContent(null);
		setSubject(null);
		setDate(null);
	}

	public String getId() {
		return id;
	}

	public String getSender() {
		return sender;
	}

	public int getMsg_type() {
		return msg_type;
	}

	public String getRecipient() {
		return recipient;
	}

	public String getRec_type() {
		return rec_type;
	}

	public String getContent() {
		return content;
	}

	public String getSubject() {
		return subject;
	}

	public DateBean getDate() {
		return date;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public void setMsg_type(int type) {
		this.msg_type = type;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public void setRec_type(String rec_type) {
		this.rec_type = rec_type;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setDate(DateBean date) {
		this.date = date;
	}

}
