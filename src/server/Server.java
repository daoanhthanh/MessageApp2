package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * 
 * @author Edward
 *
 */
public class Server {
	private Object lock;
	private final String SECRET = "234rf2d2%TT4";

	private SSLServerSocket s;
	private Socket socket;
	static ArrayList<AccountManagement> clients = new ArrayList<AccountManagement>();
	private String dataFile = "accounts.txt";
	private static final String KEY_STORE_PATH = "SSLStore";
	private static final String KEY_STORE_PW = "123456";

	static {
		System.setProperty("javax.net.ssl.keyStore", KEY_STORE_PATH);
		System.setProperty("javax.net.ssl.keyStorePassword", KEY_STORE_PW);
	}

	private void loadAccounts() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "utf8"));

			String info = br.readLine();
			while (info != null && !(info.isEmpty())) {
				clients.add(new AccountManagement(info.split(",")[0], info.split(",")[1], false, lock));
				info = br.readLine();
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveAccounts() {
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File(dataFile), "utf8");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		for (AccountManagement client : clients) {
			pw.print(client.getUsername() + "," + client.getPassword() + "\n");
		}
		pw.println("");
		if (pw != null) {
			pw.close();
		}
	}

	public Server(int port) throws IOException {
		try {
			lock = new Object();

			this.loadAccounts();
			SSLServerSocketFactory socketServerFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			s = (SSLServerSocket) socketServerFactory.createServerSocket(port);

			// s = new ServerSocket(port);

			while (true) {
				socket = s.accept();

				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

				String request = dis.readUTF();

				if (request.equals("Sign up")) {

					String username = dis.readUTF();
					String password = dis.readUTF();

					if (isExisted(username) == false) {

						AccountManagement newAccountManagement = new AccountManagement(socket, username, password, true,
								lock);
						clients.add(newAccountManagement);

						this.saveAccounts();
						dos.writeUTF("Sign up successful");
						dos.flush();

						Thread t = new Thread(newAccountManagement);
						t.start();

						updateOnlineUsers();
					} else {

						dos.writeUTF("This username is being used");
						dos.flush();
					}
				} else if (request.equals("Log in")) {

					String username = dis.readUTF();
					String password = dis.readUTF();

					if (isExisted(username) == true) {
						for (AccountManagement client : clients) {
							if (client.getUsername().equals(username)) {

								if (password.equals(client.getPassword())) {

									AccountManagement newAccountManagement = client;
									newAccountManagement.setSocket(socket);
									newAccountManagement.setIsLoggedIn(true);

									dos.writeUTF("Log in successful");
									dos.flush();

									Thread t = new Thread(newAccountManagement);
									t.start();

									updateOnlineUsers();
								} else {
									dos.writeUTF("Password is not correct");
									dos.flush();
								}
								break;
							}
						}

					} else {
						dos.writeUTF("This username is not exist");
						dos.flush();
					}
				}

			}

		} catch (Exception ex) {
			System.err.println(ex);
		} finally {
			if (s != null) {
				s.close();
			}
		}
	}

	public boolean isExisted(String name) {
		for (AccountManagement client : clients) {
			if (client.getUsername().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static void updateOnlineUsers() {
		String message = " ";
		for (AccountManagement client : clients) {
			if (client.getIsLoggedIn() == true) {
				message += ",";
				message += client.getUsername();
			}
		}
		for (AccountManagement client : clients) {
			if (client.getIsLoggedIn()) {
				try {
					client.getDos().writeUTF("Online users");
					client.getDos().writeUTF(message);
					client.getDos().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

class AccountManagement implements Runnable {

	private Object lock;
	private final String SECRET = "234rf2d2%TT4";
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String username;
	private String password;
	private boolean isLoggedIn;

	public AccountManagement(Socket socket, String username, String password, boolean isLoggedIn, Object lock)
			throws IOException {
		this.socket = socket;
		this.username = username;
		this.password = password;
		this.dis = new DataInputStream(socket.getInputStream());
		this.dos = new DataOutputStream(socket.getOutputStream());
		this.isLoggedIn = isLoggedIn;
		this.lock = lock;
	}

	public AccountManagement(String username, String password, boolean isLoggedIn, Object lock) {
		this.username = username;
		this.password = password;
		this.isLoggedIn = isLoggedIn;
		this.lock = lock;
	}

	public void setIsLoggedIn(boolean IsLoggedIn) {
		this.isLoggedIn = IsLoggedIn;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void closeSocket() {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean getIsLoggedIn() {
		return this.isLoggedIn;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public DataOutputStream getDos() {
		return this.dos;
	}

	@Override
	public void run() {

		while (true) {
			try {
				String message = null;

				message = dis.readUTF();

				String receiver = dis.readUTF();
				String content = dis.readUTF();

				for (AccountManagement client : Server.clients) {
					if (client.getUsername().equals(receiver)) {
						synchronized (lock) {
							client.getDos().writeUTF(this.username);
							client.getDos().writeUTF(content);
							client.getDos().flush();
							break;
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}