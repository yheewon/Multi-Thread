import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import javax.swing.*;

public class ScoreServerFrame extends JFrame {
	private ScoreManager scoreManager = null;
	private JTextArea log = new JTextArea();
	public ScoreServerFrame() {
		super("학생 조회 서버");
		setSize(350, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //프레임 종료 버튼(X)을 클릭하면 프로그램 종료
		Container c = getContentPane();
		c.add(new JLabel("학생 조회 서버입니다"));
		c.add(new JScrollPane(log), BorderLayout.CENTER);
		setVisible(true);
		new ServerThread().start(); // 서비스 시작
	}

	class ServerThread extends Thread {
		@Override
		public void run() {
			ServerSocket listener = null;
			Socket socket = null;
			try {
				listener = new ServerSocket(9998);
				while(true) {
					socket = listener.accept();
					log.append("클라이언트 연결됨\n");
					log.append("파일 이름 입력대기\n");
					new ServiceThread(socket).start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				if(listener != null)
					listener.close();
				if(socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ScoreManager {
		private HashMap<String, String> map = new HashMap<String, String>();
		private HashMap<String, Student> smap=new HashMap<String, Student>();
		private boolean fileOn = false;

		public ScoreManager(String fileName) {
			try {
				Scanner reader = new Scanner(new FileReader(fileName));
				while(reader.hasNext()) {
					String num=reader.next();
					String name = reader.next();
					String phoneNum=reader.next();
					String dues = reader.next();
					smap.put(name, new Student(num,name,phoneNum,dues));
				}
				reader.close();
				fileOn = true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				fileOn = false;
			}
		}

		public boolean isFileRead() {
			return fileOn;
		}

		public Student get(String name) {
			return smap.get(name);
		}
		
		public int size() {
			return smap.size();
		}
	}

	class ServiceThread extends Thread {
		private Socket socket = null;
		private BufferedReader in = null;
		private BufferedWriter out = null;
		private String file=null;

		public ServiceThread(Socket socket) { // 클라이언트와 통신할 소켓을 전달받음
			this.socket = socket;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				file=in.readLine();
				scoreManager = new ScoreManager("C:\\Users\\heewon\\Desktop\\"+file+".txt");
				if(scoreManager.isFileRead()) { // 단어 파일이 읽혀졌을 경우 서비스 시작
					log.setText(file+".txt 읽기 완료\n");
				}
			}catch (IOException e) {
				log.append("연결 종료\n");
				System.out.println("연결 종료");
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return; // 스레드 종료
				//e.printStackTrace();
			}
			while(true) {
				try {
					String choice=in.readLine();
					if("1".equals(choice)) {
						int size=scoreManager.size();
						out.write(size);
						Set<String> keys =scoreManager.smap.keySet();
						Iterator<String> it=keys.iterator();
						while(it.hasNext()) {
							String name=it.next();
							String num=scoreManager.smap.get(name).getNum();
							String phoneNum=scoreManager.smap.get(name).getPhoneNum();
							String dues=scoreManager.smap.get(name).getDues();
							out.write(num+"\n");
							out.write(name+"\n");
							out.write(phoneNum+"\n");
							out.write(dues+"\n");
							
							log.append(num+" "+name+" "+phoneNum+" "+dues+"\n");
							out.flush();
						}
					}
					else if("2".equals(choice)) {
						String name = in.readLine(); // 클라이언트로부터 이름 받음
						Student s=scoreManager.get(name);
						if(s == null) {
							out.write("없는 이름\n");
							out.write("\n");
							out.write("\n");
							log.append(name + " 없음\n");
						}
						else { 
							out.write(s.getNum()+"\n");
							out.write(s.getPhoneNum()+"\n");
							out.write(s.getDues()+"\n");
							log.append(name + ":" + s.getNum()+"  "+s.getPhoneNum()+"  "+s.getDues() + "\n");   
						}
						out.flush();
					}
					else if("3".equals(choice)) {
						log.append("연결 종료\n");
						System.out.println("연결 종료");
						socket.close();
					}
				} catch (IOException e) {
					log.append("연결 종료\n");
					System.out.println("연결 종료");
					try {
						socket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return; // 스레드 종료
					//e.printStackTrace();
				}

			}
		}
	}
	public static void main(String[] args) {
		new ScoreServerFrame();
	}

}