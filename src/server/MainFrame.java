package server;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Edward
 *
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 195069548300463478L;
	private JPanel contentPane;
	private JTextField targetPort;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("FIT Chat Server Management");

		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 586, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JButton start = new JButton("Click here to start the server");
		start.setIcon(null);
		start.setFont(new Font("Cascadia Code", Font.BOLD, 14));

		JLabel lblNewLabel = new JLabel("NPR Fall 2021");
		lblNewLabel.setFont(new Font("Cascadia Code", Font.BOLD, 16));
		lblNewLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/icon/component/Server/speech-bubble.png")));

		JLabel text = new JLabel("");
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setFont(new Font("Cascadia Code", Font.PLAIN, 16));

		JLabel lblNewLabel_1 = new JLabel("Port:");
		lblNewLabel_1.setFont(new Font("Cascadia Code", Font.PLAIN, 14));

		targetPort = new JTextField();
		targetPort.setHorizontalAlignment(SwingConstants.CENTER);
		targetPort.setFont(new Font("Cascadia Code", Font.PLAIN, 12));
		targetPort.setText("9981");
		targetPort.setColumns(10);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
				gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_contentPane.createSequentialGroup().addContainerGap(147, Short.MAX_VALUE)
										.addComponent(start, GroupLayout.PREFERRED_SIZE, 276,
												GroupLayout.PREFERRED_SIZE)
										.addGap(139))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(194).addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
										.addGap(16)
										.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 45,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(targetPort,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addComponent(lblNewLabel)).addContainerGap(179, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap(88, Short.MAX_VALUE)
								.addComponent(text, GroupLayout.PREFERRED_SIZE, 399, GroupLayout.PREFERRED_SIZE)
								.addGap(75)));
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup()
						.addGap(86).addComponent(lblNewLabel).addGap(37)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_1)
								.addComponent(targetPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18).addComponent(start, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(text, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE).addGap(48)));
		contentPane.setLayout(gl_contentPane);

		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread() {
					public void run() {
						try {
							new Server(Integer.parseInt(targetPort.getText().trim()));
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				};
				t.start();

				targetPort.setEnabled(false);
				start.setEnabled(false);
				text.setText("Server starts successfully!!");
				text.setForeground(new Color(29, 226, 158));
			}
		});
	}
}