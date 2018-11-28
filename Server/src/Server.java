import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class Server extends Server_GUI implements ActionListener{
    public static Map<String,Room_info> Room_List = new LinkedHashMap<>();
    public static Map<String,User_info> User_List = new LinkedHashMap<>();
    private ServerSocket server_socket;
    static DBController db;
    public static void main(String args[]){
        new Server();
    }
    private Server(){
        init();
        start();
    }
    private void start() {//액션리스너 활성
        server_launch_btn.addActionListener(this);
        server_stop_btn.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {//액션 리스너처리
        if (e.getSource() == server_launch_btn) {
            System.out.println("실행버튼 클릭");
            ServerStart();
            server_launch_btn.setEnabled(false);
            server_stop_btn.setEnabled(true);
        } else if (e.getSource() == server_stop_btn) {
            try {
                server_socket.close();
                User_List.clear();
                Room_List.clear();
                server_launch_btn.setEnabled(true);
                textArea.append("서버 중지\n");
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "서버 중지 실패", "알림", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("중지버튼 클릭");
        }
        }
    public static void BroadCast(String message){
        for(Map.Entry<String,User_info> u:User_List.entrySet()){
            u.getValue().send_message(message);
        }
    }
    private void ServerStart(){
        try {
            server_socket = new ServerSocket(810);//서버 가동
             db = new DBController();
        } catch (IOException e) {
            //똑같은 IP에서 똑같은 port로 서버를 열었을시 에러처리
            JOptionPane.showMessageDialog(null, "이미 사용중인 포트입니다", "알림", JOptionPane.ERROR_MESSAGE);
        }
        if (server_socket != null) {
            Connection();
        }
    }
    private void Connection(){
        Thread th = new Thread(() -> {
            while (true) {
                try {
                    textArea.append("사용자 접속 대기중\n");
                    Socket socket = server_socket.accept();
                    textArea.append("사용자 접속\n");
                    Runnable user = new User_info(socket);
                    Thread User_Thread = new Thread(user);
                    User_Thread.start();// 객체의 스레드 실행
                } catch (IOException e) {
                    break;
                }
            }
        });
        th.start();
    }
}
