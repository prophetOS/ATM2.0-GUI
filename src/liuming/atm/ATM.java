package liuming.atm;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.JOptionPane;

public class ATM {
	
	private float cash = 5000.0f;    //ATM的钱
	private static final float MAX_CASH = 100000.0f;      //ATM最大的钱
	private UserInfo theUser;      //当前用户
	//private HashMap allUsers;   //装所有用户信息
	Date date = new Date();
	SimpleDateFormat simpleDate = new SimpleDateFormat();
	List lis = new ArrayList();
	
	//新建一个Map集合
	Map<String,UserInfo> map = new HashMap();   //装所有用户信息
	//创建一个资源管理对象
	Properties pro = new Properties();
	
	//run方法，负责调用所有方法
	public void run(){
		//欢迎界面
		this.welcome();
		
		//加载数据
		this.loadData();
		
		int count = 0;       //计数器（登陆失败次数）
		boolean login = false;
		//登陆
		do{
			if(this.login() == true){
				JOptionPane.showMessageDialog(null, "登陆成功");
				break;
			}
			count ++;
		}while(count < 3);
		if(count > 3){
			JOptionPane.showMessageDialog(null, "三次登陆失败！");
			System.exit(0);
		}
		//选择业务
		this.choice();
		
		
	}

	//欢迎界面
	private void welcome(){
		JOptionPane.showMessageDialog(null, "欢迎  ICBC 银行 ");
	}
	

	//初始化数据
	private void loadData() {
		try{
			//加载文件
			pro.load(new FileReader("user.properties"));
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "文件未找到！");
		}
		
		Set setKey = pro.stringPropertyNames();//取出所有键，放入Set集合中
		Collection setValues = pro.values();     //取出所有值，放入Collection集合对象中
		Iterator itKey = setKey.iterator();       //迭代Set集合
		Iterator itValues = setValues.iterator();     //迭代Collection集合
		while(itKey.hasNext()){
			while(itValues.hasNext()){
				theUser = new UserInfo();   //新建一个UserInfo对象theUser
				theUser.setName(itKey.next().toString());   //设置theUser对象的用户名
				String[] ary1 = itValues.next().toString().split("\\$");  //将值用$切开，密码放入String数组对象ary1[0],用户的现金放入String数组对象ary1[1]
				String[] ary2 = ary1[1].split("\\|");        //将钱和密码用 | 分开
				theUser.setPassword(ary1[0]);                        //设置密码
				theUser.setAccount(Float.parseFloat(ary2[0]));       //设置用户现金
//===========================================================================================================
//将字符串转换成Date例子
//		method1：  
//				String date = "Wed Sep 07 21:54:58 CST 2011";
//				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
//				Date d=sdf.parse(date);
//				sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				System.out.println(sdf.format(d));
//		method2：
//				String s = "Wed Sep 07 21:54:58 CST 2011";
//				Date date = new Date(s);         
//				Timestamp nousedate = new Timestamp(date.getTime());
//				System.out.println(nousedate);
//===========================================================================================================
				//设置时间格式
				SimpleDateFormat simpleDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.US);
				try {
					//设置每个用户操作时间
					theUser.setDate(simpleDate.parse(ary2[1]));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//设置时间格式
				simpleDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				lis.add(simpleDate.format(theUser.getDate()) + "    用户名：" + theUser.getName());//添加操作时间和用户名到List集合
				System.out.println(theUser.getName() + "  " + theUser.getPassword() + "  " + theUser.getAccount() + "  " + theUser.getDate());
				map.put(theUser.getName(), theUser);                //将设置好的UserInfo对象，按照键（Name）值（UserInfo对象）放入Map集合中
			}
		}
	}

	//登陆
	private boolean login(){
		String user = JOptionPane.showInputDialog("请输入用户名：");
		//判断用户名是否存在
		if(map.get(user).getName() != null){
			String pwd = JOptionPane.showInputDialog("请输入密码：");
			//判断密码是否匹配
			if(pwd.equals(map.get(user).getPassword())){
				theUser = map.get(user);    //当用户名，密码都正确后，将这个对象当做当前对象
				//设置"user.properties"文件相应的键值对
				//lis.set(lis.indexOf(theUser.getDate() + "    用户名：" + theUser.getName()),date + "    用户名：" + theUser.getName() );
				pro.setProperty(theUser.getName(), theUser.getPassword()+"$"+theUser.getAccount()+"|"+date.toString());
				try {
					//写入"user.properties"文件
					pro.store(new FileWriter("user.properties"), null);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "文件未找到！");
				}
				return true;                //并返回true
			}
			else{
				//否则，输出密码错误！请重新输入。
				JOptionPane.showMessageDialog(null, "密码错误！请重新输入。");
			}
		}
		else{
			//输出用户名错误！
			JOptionPane.showMessageDialog(null,"用户名错误！");
			
		}
		return false;     //返回false
	}
	//选择业务
	private void choice(){
		do{
			//提示用户选择业务，并接受用户的输入
			int choice = Integer.parseInt(JOptionPane.showInputDialog(
					"1、查询\n2、取款\n3、存款\n4、改密码\n5、查询最后三个用户的操作时间\n6、退出\n请选择："));
			switch(choice){
			case 1:
				this.queryMoney();    //调用查询功能
				break;
			case 2:
				this.getMoney();      //调用取款功能
				break;
			case 3:
				this.storeMoney();    //调用存款功能
				break;
			case 4:
				this.changePWD();     //调用改密码功能
				break;
			case 5:
				this.FindListUserDate();//调用查询最后三个用户的操作时间
				break;
			case 6:
				this.exit();          //调用退出功能
				break;
				default:
					JOptionPane.showMessageDialog(null, "输入错误！请重新输入。");
			}
		}while(true);
	}
	//查询功能
	private void queryMoney(){
		System.out.println("查询功能实现");
		JOptionPane.showMessageDialog(null,"用户名:" + theUser.getName() + "\n你的余额为："+theUser.getAccount());
	}
	//存款功能
	private void storeMoney(){
		System.out.println("存款功能实现");
		String tempMoney = JOptionPane.showInputDialog("请输入存款数：");
		float inputMoney  =  Float.parseFloat(tempMoney);      //将用户输入的存款转化为float型
		//用正则表达式匹配用户的输入
		if(tempMoney.matches("(1[0-9]{1,}00|[1-9]00|2000)")){
			cash += inputMoney;//ATM的钱+用户输入的存款
			theUser.setAccount(theUser.getAccount()+inputMoney);  //设置当前用户的现金
			//map.put(theUser.getName(), theUser);             //将修改后的数据，放入Map集合中
			//设置"user.properties"文件相应的键值对
			pro.setProperty(theUser.getName(), theUser.getPassword()+"$"+theUser.getAccount()+"|"+date.toString());
			try {
				//写入"user.properties"文件
				pro.store(new FileWriter("user.properties"), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "文件未找到！");
			}
		}
		else if (tempMoney.matches("-[0-9]{0,}")) {
			System.out.println("你们家银行存负数和0？");
			return;
		}
		else if (inputMoney > this.MAX_CASH - this.cash) {
			System.out.println("对不起，本机装不下这么多钱。你太有了......");
			return;
		}
	}
	//取款功能
	private void getMoney(){
		System.out.println("款功能实现");
		String tempMoney = JOptionPane.showInputDialog("请输入取款数：");
		float outputMoney = Float.parseFloat(tempMoney);       //将用户输入的存款转化为float型
		//用正则表达式匹配用户的输入
		if(tempMoney.matches("(1[0-9]00|[1-9]00|2000)")){
			cash -= outputMoney;  //ATM的钱-用户输入的存款
			theUser.setAccount(theUser.getAccount()-outputMoney);  //设置当前用户的现金         
			//map.put(theUser.getName(), theUser);             //将修改后的数据，放入Map集合中
			//设置"user.properties"文件相应的键值对
			pro.setProperty(theUser.getName(), theUser.getPassword()+"$"+theUser.getAccount()+"|"+date.toString());
			try {
				//写入"user.properties"文件
				pro.store(new FileWriter("user.properties"), null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "文件未找到！");
			}
		}
		else if (outputMoney <= 0) {
			System.out.println("你们家银行取负数和0？");
			return;
		}
		else if (outputMoney >= this.theUser.getAccount()) {
			System.out.println("对不起，你账户上的余额不足！");
			return;
		}
		else if (outputMoney > this.cash) {
			System.out.println("对不起，本机上的现金不足！");
			return;
		}
	}
	//修改密码功能
	private void changePWD(){
		System.out.println("修改密码功能实现");
		String newPWD = JOptionPane.showInputDialog("请输入新密码：");
		theUser.setPassword(newPWD);              //修改当前用户的密码
		//设置"user.properties"文件相应的键值对
		pro.setProperty(theUser.getName(), theUser.getPassword()+"$"+theUser.getAccount()+"|"+date.toString());
		try {
			//写入"user.properties"文件
			pro.store(new FileWriter("user.properties"), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "文件未找到！");
		}
	}
	//退出
	private void exit(){
		JOptionPane.showMessageDialog(null,"谢谢使用！");
		System.exit(0);    //结束虚拟机
	}
	//查询最后三个用户的操作时间
	private void FindListUserDate(){
		Collections.sort(lis);
		Collections.reverse(lis);
		String str = "";
		for(int i = 0 ; i < 3 ; i++){
			str += lis.get(i) + "\n";
		}
		JOptionPane.showMessageDialog(null, str);
	}
}
