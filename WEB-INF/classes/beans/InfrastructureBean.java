package beans;

public class InfrastructureBean {
	int id;
	int infra_type;
	int capacity;
	String name;
	public int getId() {
		return id;
	}
	public int getInfra_type() {
		return infra_type;
	}
	public int getCapacity() {
		return capacity;
	}
	public String getName() {
		return name;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setInfra_type(int infra_type) {
		this.infra_type = infra_type;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public void setName(String name) {
		this.name = name;
	}
}
