
public class Student {
	private String num;
	private String name;
	private String phoneNum;
	private String dues;
	public Student(String num, String name, String phoneNum, String dues) {
		super();
		this.num = num;
		this.name = name;
		this.phoneNum = phoneNum;
		this.dues = dues;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getDues() {
		return dues;
	}
	public void setDues(String dues) {
		this.dues = dues;
	}
}
