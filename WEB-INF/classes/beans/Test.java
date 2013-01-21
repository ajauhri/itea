package beans;

public class Test {
	String username;
	String password;
	public Test(){
	}
	public void init(){
	}
	public void terminate(){
	}
	public void setUsername(String username){
		this.username=username;
	}
	public void setPassword(String password){
		this.password=password;
	}
	public String getUsername(){
		return username;
	}
	public String getPassword(){
		return password;
	}
}