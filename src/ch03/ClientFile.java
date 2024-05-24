package ch03;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFile {

	public static void main(String[] args) {

		Socket socket = null;
		try {
			socket = new Socket("localhost", 5001);

			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true); // auto-flush
			writer.println("안녕 반가워~");

			// 서버로부터 데이터를 받기 위한 입력스트림
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = reader.readLine();
			System.out.println("서버측 응답: " + message);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
