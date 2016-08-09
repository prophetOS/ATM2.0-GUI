package liuming.atm;

import java.util.Date;

public class UserInfo {
	
	private String name;//用户名
	
	private String password;//密码
	
	private float account;//现金
	
	private Date date;//记录操作时间
	//获得操作时间
	public Date getDate() {
		return date;
	}
	//设置操作时间
	public void setDate(Date date) {
		this.date = date;
	}
	//获得用户名
	public String getName() {
		return name;
	}
	//设置用户名
	public void setName(String name) {
		this.name = name;
	}
	//获得密码
	public String getPassword() {
		return password;
	}
	//设置密码
	public void setPassword(String password) {
		this.password = password;
	}
	//获得用户卡里的现金
	public float getAccount() {
		return account;
	}
	//设置用户卡里的现金
	public void setAccount(float account) {
		this.account = account;
	}
}
