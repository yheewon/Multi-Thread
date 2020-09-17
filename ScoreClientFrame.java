import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;


public class ScoreClientFrame extends JFrame {
	private JTextField choiceTf = new JTextField(7);//��ȣ �Է�â
	private JTextField nameTf = new JTextField(7);//�̸� �Է�â
	private JTextField fileTf = new JTextField(7);//�̸� �Է�â
	private JLabel resLabel = new JLabel("������");
	private JTextArea t =new JTextArea();
	private Socket socket = null;
	private BufferedReader in = null;
	private BufferedWriter out = null;

	public ScoreClientFrame() {
		super("����üũ Ŭ���̾�Ʈ");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //������ ���� ��ư(X)�� Ŭ���ϸ� ���α׷� ����
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("�����̸� �Է�: "));
		c.add(fileTf);
		c.add(new JLabel("1.��ü����  2.�̸��˻�  3.���� \n"));
		c.add(choiceTf);
		c.add(new JLabel("�̸� �Է� \n"));
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
						return; // �Էµ��� �ʾ���

					out.write(file + "\n");
					out.flush();
				} catch (IOException e1) {
					System.out.println("Ŭ���̾�Ʈ : �����κ��� ���� ����");
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
						return; // �Էµ��� �ʾ���

					out.write(choice + "\n");
					out.flush();
					
					if("1".equals(choice)) {
						resLabel.setText(null);
						c.remove(nameTf);
						int size=in.read();//�ؽ��� ������
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
					System.out.println("Ŭ���̾�Ʈ : �����κ��� ���� ����");
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
	                  return; // �Էµ��� �ʾ���
	               
	               out.write(name + "\n");
	               out.flush();
	               
	               String num = in.readLine();
	               String phoneNum = in.readLine();
	               String dues = in.readLine();
	               resLabel.setText(num+" "+phoneNum+" "+dues);
	            } catch (IOException e1) {
	               System.out.println("Ŭ���̾�Ʈ : �����κ��� ���� ����");
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