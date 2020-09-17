import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class ScoreClientFrame extends JFrame {
	private JTextField choiceTf = new JTextField(7);//번호 입력창
	private JTextField nameTf = new JTextField(7);//이름 입력창
	private JTextField fileTf = new JTextField(7);//이름 입력창
	private JLabel resLabel = new JLabel("상세정보");
	private JTextArea t =new JTextArea();
	private Socket socket = null;
	private BufferedReader in = null;
	private BufferedWriter out = null;

	public ScoreClientFrame() {
		super("스펠체크 클라이언트");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //프레임 종료 버튼(X)을 클릭하면 프로그램 종료
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("파일이름 입력: "));
		c.add(fileTf);
		c.add(new JLabel("1.전체보기  2.이름검색  3.종료 \n"));
		c.add(choiceTf);
		c.add(new JLabel("이름 입력 \n"));
		c.add(nameTf);
		c.add(resLabel);
		c.add(t);
		setVisible(true);
		setupConnection();
		
		fileTf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					String file = tf.getText().trim();               
					if(file.length() == 0)
						return; // 입력되지 않았음

					out.write(file + "\n");
					out.flush();
				} catch (IOException e1) {
					System.out.println("클라이언트 : 서버로부터 연결 종료");
					return;
					// e.printStackTrace();
				}

			}

		});

		
		choiceTf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField tf = (JTextField)e.getSource();
				try {
					String choice = tf.getText().trim();               
					if(choice.length() == 0)
						return; // 입력되지 않았음

					out.write(choice + "\n");
					out.flush();
					
					if("1".equals(choice)) {
						resLabel.setText(null);
						c.remove(nameTf);
						int size=in.read();//해쉬맵 사이즈
						for(int i=0;i<size;i++) {
							String num=in.readLine();
							String name=in.readLine();
							String phoneNum=in.readLine();
							String dues=in.readLine();
							t.append(num+" "+name+" "+phoneNum+" "+dues+"\n");
						}
					}
					else if("2".equals(choice))
					{
						t.setText(null);
						c.add(nameTf);
					}
					else if("3".equals(choice))  System.exit(0);
				} catch (IOException e1) {
					System.out.println("클라이언트 : 서버로부터 연결 종료");
					return;
					// e.printStackTrace();
				}

			}

		});
		
		 nameTf.addActionListener(new ActionListener() {

	         @Override
	         public void actionPerformed(ActionEvent e) {
	            JTextField tf = (JTextField)e.getSource();
	            try {
	               String name = tf.getText().trim();               
	               if(name.length() == 0)
	                  return; // 입력되지 않았음
	               
	               out.write(name + "\n");
	               out.flush();
	               
	               String num = in.readLine();
	               String phoneNum = in.readLine();
	               String dues = in.readLine();
	               resLabel.setText(num+" "+phoneNum+" "+dues);
	            } catch (IOException e1) {
	               System.out.println("클라이언트 : 서버로부터 연결 종료");
	               return;
	               // e.printStackTrace();
	            }
	            
	         }
	         
	      });

	}

	public void setupConnection() {
		try {
			socket = new Socket("localhost", 9998);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ScoreClientFrame();
	}

}