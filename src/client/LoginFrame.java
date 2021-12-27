package client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5705400570697424294L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	private String host = "localhost";
	private Socket socket;
	
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private String username;
	private JTextField targetPort;
	private static final String TRUST_STORE_PATH = "truststore";
	private static final String TRUST_STORE_PW = "123456";

	static {
		System.setProperty("javax.net.ssl.trustStore", TRUST_STORE_PATH);
		System.setProperty("javax.net.ssl.trustStorePassword", TRUST_STORE_PW);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public LoginFrame() {
		setTitle("FIT CHAT");
		
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 613, 389);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("InternalFrame.paletteBackground"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(UIManager.getColor("InternalFrame.paletteBackground"));
		
		JLabel lbUsername = new JLabel("Username");
		lbUsername.setFont(new Font("Cascadia Code", Font.PLAIN, 14));
		
		JLabel lbPassword = new JLabel("Password");
		lbPassword.setFont(new Font("Cascadia Code", Font.PLAIN, 14));
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Cascadia Code", Font.PLAIN, 12));
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		
		JPanel buttons = new JPanel();
		buttons.setBackground(new Color(230, 240, 247));
		JPanel notificationContainer = new JPanel();
		notificationContainer.setBackground(UIManager.getColor("InternalFrame.paletteBackground"));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginFrame.class.getResource("/icon/component/Login/hello.png")));

		targetPort = new JTextField();
		targetPort.setHorizontalAlignment(SwingConstants.CENTER);
		targetPort.setFont(new Font("Cascadia Code", Font.PLAIN, 12));
		targetPort.setText("9981");
		targetPort.setColumns(10);

		JLabel lbPort = new JLabel("Port");
		lbPort.setFont(new Font("Cascadia Code", Font.PLAIN, 14));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addGroup(Alignment.TRAILING,
												gl_contentPane.createSequentialGroup()
														.addComponent(headerPanel, GroupLayout.DEFAULT_SIZE, 373,
																Short.MAX_VALUE)
														.addGap(19))
										.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
												.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
														.addComponent(notificationContainer, Alignment.LEADING,
																GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
														.addGroup(Alignment.LEADING, gl_contentPane
																.createSequentialGroup().addGap(48)
																.addGroup(gl_contentPane
																		.createParallelGroup(Alignment.LEADING)
																		.addComponent(lbUsername,
																				GroupLayout.DEFAULT_SIZE, 87,
																				Short.MAX_VALUE)
																		.addComponent(lbPassword,
																				GroupLayout.PREFERRED_SIZE, 87,
																				Short.MAX_VALUE))
																.addPreferredGap(ComponentPlacement.RELATED)
																.addGroup(gl_contentPane
																		.createParallelGroup(Alignment.TRAILING)
																		.addComponent(txtPassword,
																				GroupLayout.PREFERRED_SIZE, 243,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(txtUsername,
																				GroupLayout.PREFERRED_SIZE, 243,
																				GroupLayout.PREFERRED_SIZE)
																		.addGroup(gl_contentPane.createSequentialGroup()
																				.addComponent(lbPort,
																						GroupLayout.PREFERRED_SIZE, 45,
																						GroupLayout.PREFERRED_SIZE)
																				.addGap(47).addComponent(targetPort,
																						GroupLayout.PREFERRED_SIZE,
																						GroupLayout.DEFAULT_SIZE,
																						GroupLayout.PREFERRED_SIZE))))
														.addGroup(Alignment.LEADING,
																gl_contentPane.createSequentialGroup().addGap(44)
																		.addComponent(buttons, GroupLayout.DEFAULT_SIZE,
																				338, Short.MAX_VALUE)))
												.addContainerGap())))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(30)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 262,
														GroupLayout.PREFERRED_SIZE)
												.addContainerGap(50, Short.MAX_VALUE))
										.addGroup(gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 34,
														Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addComponent(notificationContainer, GroupLayout.PREFERRED_SIZE, 33,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.UNRELATED)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(lbUsername).addComponent(txtUsername,
																GroupLayout.PREFERRED_SIZE, 26,
																GroupLayout.PREFERRED_SIZE))
												.addGap(18)
												.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_contentPane.createSequentialGroup()
																.addComponent(lbPassword, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addGap(29))
														.addGroup(gl_contentPane.createSequentialGroup().addGap(3)
																.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE,
																		29, GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(ComponentPlacement.RELATED)))
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
														.addComponent(targetPort, GroupLayout.PREFERRED_SIZE, 27,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(lbPort))
												.addGap(34).addComponent(buttons, GroupLayout.PREFERRED_SIZE, 59,
														GroupLayout.PREFERRED_SIZE)
												.addGap(41))))
		);
		
		JLabel headerContent = new JLabel("LOGIN");
		headerPanel.add(headerContent);
		headerContent.setFont(new Font("Cascadia Code", Font.BOLD, 24));
		
		JLabel notification = new JLabel("");
		notification.setForeground(Color.RED);
		notification.setFont(new Font("Cascadia Code", Font.PLAIN, 13));
		notificationContainer.add(notification);
		
		JButton login = new JButton("Sign in");
		login.setFont(new Font("Papyrus", Font.PLAIN, 12));
		login.setIcon(new ImageIcon(LoginFrame.class.getResource("/icon/component/Login/038-login.png")));
		JButton signup = new JButton("Register");
		signup.setFont(new Font("Papyrus", Font.PLAIN, 12));
		signup.setIcon(new ImageIcon(LoginFrame.class.getResource("/icon/component/Login/065-sign up.png")));
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String response = Login(txtUsername.getText(), String.copyValueOf(txtPassword.getPassword()));
				
				if ( response.equals("Log in successful") ) {
					username = txtUsername.getText();
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								ChatFrame frame = new ChatFrame(username, dis, dos);
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					dispose();
				} else {
					login.setEnabled(false);
					signup.setEnabled(false);
					txtPassword.setText("");
					notification.setText(response);
				}
			}
		});
		login.setEnabled(false);
		buttons.add(login);
		
		signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JPasswordField confirm = new JPasswordField();
				
				
			    int action = JOptionPane.showConfirmDialog(null, confirm,"Comfirm your password",JOptionPane.OK_CANCEL_OPTION);
			    if (action == JOptionPane.OK_OPTION) {
			    	if (String.copyValueOf(confirm.getPassword()).equals(String.copyValueOf(txtPassword.getPassword()))) {
			    		String response = Signup(txtUsername.getText(), String.copyValueOf(txtPassword.getPassword()));
					
						if ( response.equals("Sign up successful") ) {
							username = txtUsername.getText();
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										int confirm = JOptionPane.showConfirmDialog(null, "Sign up successful\nWelcome to FIT CHAT", "Sign up successful", JOptionPane.DEFAULT_OPTION);
										 
										ChatFrame frame = new ChatFrame(username, dis, dos);
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							dispose();
						} else {
							login.setEnabled(false);
							signup.setEnabled(false);
							txtPassword.setText("");
							notification.setText(response);
						}
					} else {
						notification.setText("!NOTE: Confirm password does not match");
			    	}
			    }
			}
		});
		signup.setEnabled(false);
		buttons.add(signup);
		contentPane.setLayout(gl_contentPane);
		
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtUsername.getText().isEmpty() || String.copyValueOf(txtPassword.getPassword()).isEmpty()) {
					login.setEnabled(false);
					signup.setEnabled(false);
				} else {
					login.setEnabled(true);
					signup.setEnabled(true);
				}
			}
		});
		
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtUsername.getText().isEmpty() || String.copyValueOf(txtPassword.getPassword()).isEmpty()) {
					login.setEnabled(false);
					signup.setEnabled(false);
				} else {
					login.setEnabled(true);
					signup.setEnabled(true);
				}
			}
		});
		
		this.getRootPane().setDefaultButton(login);
	}
	

	public String Login(String username, String password) {
		try {
			Connect();
			
			dos.writeUTF("Log in");
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.flush();
			
			String response = dis.readUTF();
			return response;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Network error: Log in fail";
		}
	}
	

	public String Signup(String username, String password) {
		try {
			Connect();
			
			dos.writeUTF("Sign up");
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.flush();
			
			String response = dis.readUTF();
			return response;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Network error: Sign up fail";
		}
	}
	

	public void Connect() {
		try {
			if (socket != null) {
				socket.close();
			}
			SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = socketFactory.createSocket(host, Integer.parseInt(targetPort.getText().trim()));
//			socket =  new Socket(host, Integer.parseInt(targetPort.getText().trim()));
			System.out.println(Integer.parseInt(targetPort.getText()));
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getUsername() {
		return this.username;
	}
}
