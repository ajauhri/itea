package beans;

public class PreferenceBean {
	int day_no;
	SlotBean[] slot_no;
	public int getDay_no() {
		return day_no;
	}
	public SlotBean[] getSlot_no() {
		return slot_no;
	}
	public void setDay_no(int day_no) {
		this.day_no = day_no;
	}
	public void setSlot_no(SlotBean[] slot_no) {
		this.slot_no = slot_no;
	}	
}
