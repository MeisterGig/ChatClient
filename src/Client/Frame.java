package Client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Frame {

	private JFrame frmChat;
	private JTextField textField;
	private JButton btnSend, btnDisconnect,btnAnhang;
	private JTextPane textPane;
	private JComboBox userList;
	private Converter con;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame window = new Frame();
					window.frmChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame() {
		String ip = JOptionPane.showInputDialog("Server IP:");
		String username = JOptionPane.showInputDialog("Username:");
		initialize();
		con = new Converter(ip, username, this);
	}

	private void initialize() {
		frmChat = new JFrame();
		frmChat.setTitle("Chat");
		frmChat.setBounds(100, 100, 701, 488);
		frmChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		userList = new JComboBox();
		
		btnSend = new JButton("Send");
		frmChat.getRootPane().setDefaultButton(btnSend);
		btnDisconnect = new JButton("Disconnect");
		btnAnhang = new JButton("Anhang");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		
		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(new Font("Arial", Font.PLAIN, 11));
		textPane.setContentType("text/html");
		textPane.setText("<h1>Hallo</h1> <br> Bitte wählen sie einen Nutzer aus um mit ihm zu schreiben.");
		
		GroupLayout groupLayout = new GroupLayout(frmChat.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(textPane, GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(userList, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 454, Short.MAX_VALUE)
							.addComponent(btnDisconnect))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(textField, GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnAnhang)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnSend)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(userList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDisconnect))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textPane, GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE, false)
							.addComponent(btnAnhang, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGroup(groupLayout.createSequentialGroup()
								.addGap(3)
								.addComponent(textField))))
					.addContainerGap())
		);
		frmChat.getContentPane().setLayout(groupLayout);
		actions();
	}
	
	private void actions(){
		
		btnAnhang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        JFileChooser fileChooser = new JFileChooser();
		        int returnValue = fileChooser.showOpenDialog(null);
		        if (returnValue == JFileChooser.APPROVE_OPTION) {
		        	File selectedFile = fileChooser.getSelectedFile();
		        	con.sendFile((String) userList.getSelectedItem(),selectedFile.getPath());
		        	System.err.println(selectedFile.getPath());
		        }
			}
		});
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textField.getText()!=null && (String) userList.getSelectedItem() != null){
					con.sendMessage((String) userList.getSelectedItem(),textField.getText());
					textField.setText(null);
				}
			}
		});
		
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});
		
		userList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                JComboBox selectedChoice = (JComboBox) e.getSource();
                recieveMessages((String) selectedChoice.getSelectedItem());
			}
		});
	}
	
	public void refreshMessages(String from, String messages){
		String selected=(String) userList.getSelectedItem();
		if(selected.equals(from)){
			textPane.setText(messages);
		}
	}
	
	public void recieveMessages(String user){
		textPane.setText(con.loadMessages(user));
	}
	
	public void receiveClients(String clients[]){
		System.out.println("Clientlist updated:");
		userList.removeAllItems();
		for(String s: clients){
			userList.addItem(s);
			System.out.println(s);
		}
	}
}
