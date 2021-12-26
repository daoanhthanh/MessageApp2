package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatFrame extends JFrame {

	private static final long serialVersionUID = -8948951261632836065L;

	private JButton btnSend;

	private JScrollPane chatPanel;

	private JLabel lbReceiver = new JLabel(" ");

	private JPanel contentPane;

	private JTextField txtMessage;

	private JTextPane chatWindow;

	JButton btnSelectAll;

	JComboBox<String> onlineUsers = new JComboBox<String>();

	private String username;

	private HashMap<String, JTextPane> chatWindows = new HashMap<String, JTextPane>();

	Thread receiver;

	StyledDocument messageDoc;

	Style userStyleSend;

	private void autoScroll() {
		chatPanel.getVerticalScrollBar().setValue(chatPanel.getVerticalScrollBar().getMaximum());
	}

	public void setUsername(String username) {
		this.username = username;
	}

	// private void newEmoji(String username, String emoji, Boolean isMyMessage) {

	// StyledDocument doc;
	// if (username.equals(this.username)) {
	// doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
	// } else {
	// doc = chatWindows.get(username).getStyledDocument();
	// }

	// Style userStyle = doc.getStyle("User style");
	// if (userStyle == null) {
	// userStyle = doc.addStyle("User style", null);
	// StyleConstants.setBold(userStyle, true);
	// }

	// if (isMyMessage == true) {
	// StyleConstants.setForeground(userStyle, Color.red);
	// } else {
	// StyleConstants.setForeground(userStyle, Color.BLUE);
	// }

	// try {
	// doc.insertString(doc.getLength(), username + ": ", userStyle);
	// } catch (BadLocationException e) {
	// }

	// Style iconStyle = doc.getStyle("Icon style");
	// if (iconStyle == null) {
	// iconStyle = doc.addStyle("Icon style", null);
	// }
	// String emojiPath = emoji.substring(emoji.lastIndexOf("/icon"));
	// StyleConstants.setIcon(iconStyle, new
	// ImageIcon(getClass().getResource(emojiPath)));

	// try {
	// doc.insertString(doc.getLength(), "invisible text", iconStyle);
	// } catch (BadLocationException e) {
	// }

	// try {
	// doc.insertString(doc.getLength(), "\n", userStyle);
	// } catch (BadLocationException e) {
	// }

	// autoScroll();
	// }

	private void newMessage(String username, String message, Boolean isMyMessage) {

		StyledDocument doc;
		if (username.equals(this.username)) {
			doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
		} else if (username.equals(" ")) {
			doc = chatWindows.get(" ").getStyledDocument();
		} else {
			doc = chatWindows.get(username).getStyledDocument();
		}

		Style userStyle = doc.getStyle("User style");
		if (userStyle == null) {
			userStyle = doc.addStyle("User style", null);
			StyleConstants.setBold(userStyle, true);
		}

		if (isMyMessage == true) {
			StyleConstants.setForeground(userStyle, Color.red);
		} else {
			StyleConstants.setForeground(userStyle, Color.BLUE);
		}

		try {
			doc.insertString(doc.getLength(), username + ": ", userStyle);
		} catch (BadLocationException e) {
		}

		Style messageStyle = doc.getStyle("Message style");
		if (messageStyle == null) {
			messageStyle = doc.addStyle("Message style", null);
			StyleConstants.setForeground(messageStyle, Color.BLACK);
			StyleConstants.setBold(messageStyle, false);
		}

		try {
			doc.insertString(doc.getLength(), message + "\n", messageStyle);
		} catch (BadLocationException e) {
		}

		autoScroll();
	}

	/**
	 * Group chat
	 */
	private void groupMessage(String username, String message, Boolean isMyMessage) {

		StyledDocument doc;
		if (username.equals(this.username)) {
			doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
		} else if (username.equals(" ")) {
			doc = chatWindows.get(" ").getStyledDocument();
		} else {
			doc = chatWindows.get(username).getStyledDocument();
		}

		Style userStyle = doc.getStyle("User style");
		if (userStyle == null) {
			userStyle = doc.addStyle("User style", null);
			StyleConstants.setBold(userStyle, true);
		}

		if (isMyMessage == true) {
			StyleConstants.setForeground(userStyle, Color.red);
		} else {
			StyleConstants.setForeground(userStyle, Color.BLUE);
		}

		userStyleSend = userStyle;
		Style messageStyle = doc.getStyle("Message style");
		if (messageStyle == null) {
			messageStyle = doc.addStyle("Message style", null);
			StyleConstants.setForeground(messageStyle, Color.BLACK);
			StyleConstants.setBold(messageStyle, false);
		}
		messageDoc = doc;

		autoScroll();
	}

	/**
	 * Create the frame.
	 */
	public ChatFrame(String username, DataInputStream dis, DataOutputStream dos) {
		setTitle("Logging as: " + username);
		this.username = username;
		receiver = new Thread(new Receiver(dis));
		receiver.start();

		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 669, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);

		txtMessage = new JTextField();
		txtMessage.setBounds(15, 393, 483, 49);
		txtMessage.setFont(new Font("Cascadia Code", Font.PLAIN, 12));

		txtMessage.setColumns(10);

		btnSend = new JButton("");
		btnSend.setBounds(586, 393, 50, 50);
		btnSend.setToolTipText("Send to this one");
		btnSend.setBackground(Color.WHITE);
		btnSend.setForeground(new Color(0, 206, 209));
		btnSend.setEnabled(false);

		btnSend.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/component/ChatFrame/send_2.png")));

		chatPanel = new JScrollPane();
		chatPanel.setFont(new Font("Cascadia Code", Font.PLAIN, 10));
		chatPanel.setBounds(15, 75, 483, 307);
		chatPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(508, 75, 137, 307);
		leftPanel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(240, 248, 255)));
		leftPanel.setBackground(Color.LIGHT_GRAY);

		btnSelectAll = new JButton("");
		btnSelectAll.setBackground(Color.WHITE);
		btnSelectAll.setBounds(508, 393, 59, 49);
		// btnSelectAll.setEnabled(false);
		btnSelectAll
				.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/component/ChatFrame/024-discussion.png")));
		btnSelectAll.setToolTipText("Send message to all ");
		btnSelectAll.setFont(new Font("OCR A Extended", Font.PLAIN, 11));

		btnSelectAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtMessage.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "You need to text something!");
				} else {
					lbReceiver.setText((String) onlineUsers.getSelectedItem());
					int size = onlineUsers.getItemCount();
					for (int i = 0; i < size; i++) {
						String member = onlineUsers.getItemAt(i);
						try {
							dos.writeUTF("Text");
							dos.writeUTF(member);
							dos.writeUTF("@" + txtMessage.getText());
							dos.flush();
						} catch (IOException e1) {
							e1.printStackTrace();
							newMessage("ERROR", "Network error!", true);
						}

					}

					if (chatWindows.get(" ") != null) {
						lbReceiver.setText(" ");
						groupMessage(username, txtMessage.getText(), true);
						Style messageStyle = messageDoc.getStyle("Message style");
						try {
							messageDoc.insertString(messageDoc.getLength(), username + ": ", userStyleSend);
							messageDoc.insertString(messageDoc.getLength(), txtMessage.getText() + "\n", messageStyle);
							txtMessage.setText("");
						} catch (BadLocationException ex) {
							Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
						}
					} else {
						JTextPane temp = new JTextPane();
						temp.setFont(new Font("Arial", Font.PLAIN, 14));
						temp.setEditable(false);
						chatWindows.put(" ", temp);
						lbReceiver.setText(" ");

						groupMessage(username, txtMessage.getText(), true);
						Style messageStyle = messageDoc.getStyle("Message style");
						try {

							messageDoc.insertString(messageDoc.getLength(), username + ": ", userStyleSend);
							messageDoc.insertString(messageDoc.getLength(), txtMessage.getText() + "\n", messageStyle);
							txtMessage.setText("");
						} catch (BadLocationException ex) {
							Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
						}
					}

				}
			}

		});

		JPanel leftPanel_1 = new JPanel();
		leftPanel_1.setBounds(15, 5, 630, 64);
		leftPanel_1.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(240, 248, 255)));
		leftPanel_1.setBackground(Color.LIGHT_GRAY);

		JLabel userImage_1 = new JLabel((Icon) null);

		JLabel userImage = new JLabel(
				new ImageIcon(ChatFrame.class.getResource("/icon/component/ChatFrame/santa-claus.png")));

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 239, 213));

		JLabel lbUsername = new JLabel(this.username);
		lbUsername.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		panel.add(lbUsername);

		GroupLayout gl_leftPanel_1 = new GroupLayout(leftPanel_1);
		gl_leftPanel_1.setHorizontalGroup(gl_leftPanel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_leftPanel_1.createSequentialGroup()
						.addComponent(userImage, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
						.addGroup(gl_leftPanel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_leftPanel_1.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED, 500, Short.MAX_VALUE)
										.addComponent(userImage_1).addGap(66))
								.addGroup(gl_leftPanel_1.createSequentialGroup()
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel,
												GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
										.addContainerGap()))));
		gl_leftPanel_1
				.setVerticalGroup(
						gl_leftPanel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(userImage, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
								.addGroup(gl_leftPanel_1.createSequentialGroup().addContainerGap()
										.addGroup(gl_leftPanel_1.createParallelGroup(Alignment.LEADING)
												.addComponent(panel, GroupLayout.PREFERRED_SIZE, 34,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(userImage_1))
										.addContainerGap(16, Short.MAX_VALUE)));
		leftPanel_1.setLayout(gl_leftPanel_1);
		JLabel lblNewLabel_1 = new JLabel("Active Users");
		lblNewLabel_1.setBounds(19, 13, 87, 15);
		lblNewLabel_1.setFont(new Font("PT Serif Caption", Font.BOLD, 11));
		onlineUsers.setBounds(10, 40, 117, 21);
		onlineUsers.setBackground(new Color(245, 245, 220));
		onlineUsers.setFont(new Font("PT Sans Caption", Font.PLAIN, 11));
		onlineUsers.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					lbReceiver.setText((String) onlineUsers.getSelectedItem());
					if (chatWindow != chatWindows.get(lbReceiver.getText())) {
						txtMessage.setText("");
						chatWindow = chatWindows.get(lbReceiver.getText());
						chatPanel.setViewportView(chatWindow);
						chatPanel.validate();
					}

					if (lbReceiver.getText().isEmpty()) {
						btnSend.setEnabled(false);

					} else {
						btnSend.setEnabled(true);
					}
				} else {
					lbReceiver.setText(" ");
					if (chatWindow != chatWindows.get(lbReceiver.getText())) {
						txtMessage.setText("");
						chatWindow = chatWindows.get(lbReceiver.getText());
						chatPanel.setViewportView(chatWindow);
						chatPanel.validate();
						btnSend.setEnabled(true);
					}
				}

			}
		});

		JPanel usernamePanel = new JPanel();
		usernamePanel.setBackground(new Color(255, 250, 205));
		chatPanel.setColumnHeaderView(usernamePanel);

		lbReceiver.setFont(new Font("Arial", Font.BOLD, 16));
		usernamePanel.add(lbReceiver);

		chatWindows.put(" ", new JTextPane());
		chatWindow = chatWindows.get(" ");
		chatWindow.setFont(new Font("Arial", Font.PLAIN, 14));
		chatWindow.setEditable(false);

		chatPanel.setViewportView(chatWindow);

		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtMessage.getText().isEmpty() || lbReceiver.getText().isEmpty()) {
					btnSend.setEnabled(false);
				} else {
					btnSend.setEnabled(true);
				}
			}
		});

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtMessage.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "You need to text something!");
				} else {

					try {
						dos.writeUTF("Text");
						dos.writeUTF(lbReceiver.getText());
						dos.writeUTF(txtMessage.getText());
						dos.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
						newMessage("ERROR", "Network error!", true);
					}

					newMessage(username, txtMessage.getText(), true);
					txtMessage.setText("");
				}
			}
		});

		this.getRootPane().setDefaultButton(btnSend);
		contentPane.setLayout(null);
		contentPane.add(leftPanel_1);
		contentPane.add(btnSelectAll);
		contentPane.add(txtMessage);
		contentPane.add(btnSend);
		contentPane.add(leftPanel);
		leftPanel.setLayout(null);
		leftPanel.add(onlineUsers);
		leftPanel.add(lblNewLabel_1);
		contentPane.add(chatPanel);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				try {
					dos.writeUTF("Log out");
					dos.flush();

					try {
						receiver.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					if (dos != null) {
						dos.close();
					}
					if (dis != null) {
						dis.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

	}

	class Receiver implements Runnable {

		private DataInputStream dis;

		public Receiver(DataInputStream dis) {
			this.dis = dis;
		}

		@Override
		public void run() {
			try {

				while (true) {

					String method = dis.readUTF();

					if (method.equals("Text")) {

						String sender = dis.readUTF();
						String message = dis.readUTF();

						if (message.contains("@")) {
							if (chatWindows.get(" ") != null) {
								lbReceiver.setText(" ");
								groupMessage(lbReceiver.getText(), message.substring(1), false);
								Style messageStyle = messageDoc.getStyle("Message style");
								try {

									messageDoc.insertString(messageDoc.getLength(), sender + ": ", userStyleSend);
									messageDoc.insertString(messageDoc.getLength(), message.substring(1) + "\n",
											messageStyle);
									txtMessage.setText("");
								} catch (BadLocationException ex) {
									Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
								}
							} else {
								JTextPane temp = new JTextPane();
								temp.setFont(new Font("Arial", Font.PLAIN, 14));
								temp.setEditable(false);
								chatWindows.put(" ", temp);
								lbReceiver.setText(" ");

								groupMessage(lbReceiver.getText(), message.substring(1), false);
								Style messageStyle = messageDoc.getStyle("Message style");
								try {

									messageDoc.insertString(messageDoc.getLength(), sender + ": ", userStyleSend);
									messageDoc.insertString(messageDoc.getLength(), message.substring(1) + "\n",
											messageStyle);
									txtMessage.setText("");
								} catch (BadLocationException ex) {
									Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
								}
							}

						} else {

							newMessage(sender, message, false);
						}
					}
					// else if (method.equals("Emoji")) {

					// String sender = dis.readUTF();
					// String emoji = dis.readUTF();

					// newEmoji(sender, emoji, false);
					// }
					else if (method.equals("Online users")) {

						String[] users = dis.readUTF().split(",");
						onlineUsers.removeAllItems();

						String chatting = lbReceiver.getText();

						boolean isChattingOnline = false;

						for (String user : users) {
							if (user.equals(username) == false) {
								onlineUsers.addItem(user);
								if (chatWindows.get(user) == null) {
									JTextPane temp = new JTextPane();
									temp.setFont(new Font("Arial", Font.PLAIN, 14));
									temp.setEditable(false);
									chatWindows.put(user, temp);
								}
							}
							if (chatting.equals(user)) {
								isChattingOnline = true;
							}
							if (chatting.equals("All")) {
								isChattingOnline = true;
							}
						}

						if (isChattingOnline == false) {
							onlineUsers.setSelectedItem(" ");
							JOptionPane.showMessageDialog(null,
									chatting + " is offline!\nYou will be redirect to default chat window");
						} else {
							onlineUsers.setSelectedItem(chatting);
						}

						onlineUsers.validate();
					} else if (method.equals("Safe to leave")) {
						break;
					}

				}

			} catch (IOException ex) {
				System.err.println(ex);
			} finally {
				try {
					if (dis != null) {
						dis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private class SwingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}
}