package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class AbstractClient {

	private Socket socket;
	private BufferedReader readerStream;
	private PrintWriter writerStream;
	private BufferedReader keyboardReader;

	public final void run() {
		try {
			setupClient();
			setupStream();
			startService();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	// set 메서드
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

	// 1. 소켓IP주소, 포트 번호
	protected abstract void setupClient() throws IOException;

	// 2. 스트림 초기화
	private void setupStream() throws IOException {
		writerStream = new PrintWriter(socket.getOutputStream(), true);
		readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 3. 서비스 시작
	private void startService() {
		Thread readThread = createReadThread();
		Thread writeThread = createWriteThread();
		readThread.start();
		writeThread.start();

		try {
			readThread.join();
			writeThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 캡슐화
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = readerStream.readLine()) != null) {
					// 클라이언트 측 콘솔에 출력
					System.out.println("server 측 msg : " + msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					writerStream.println(msg);
					writerStream.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	// 캡슐화 - 소켓 자원 종료
	private void cleanup() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
