
import javax.swing.*;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;


import java.io.*;
import java.net.Socket;

abstract class AbstractNetwork{
    protected Socket socket;
    public String IP;
    private static final int port = 810;
    protected InputStream is;
    protected OutputStream os;
    protected DataInputStream dis;
    protected DataOutputStream dos;
    public abstract void in_message(String message);
    public abstract void IOError(IOException e);
    protected void Connect(){
        try{
            socket = new Socket(IP,port);
            setStream();
        }catch(IOException e){
            IOError(e);
        }
    }
    public void send_message(String message){
        try {
            dos.writeUTF(message);
        } catch (IOException e) {
            IOError(e);
        }
    }
    protected void setStream(){
        try{
            is = socket.getInputStream();
            dis = new DataInputStream(is);
            os = socket.getOutputStream();
            dos = new DataOutputStream(os);
        }catch (IOException e){
            IOError(e);
        }
    }
}

public class ClientNetwork extends AbstractNetwork{
    private Client_GUI GUI;
    public String ID;
    public String Password;
    public String Curent_Room_Name="";
    private Vector<String> fr_List = new Vector<>();
    private Vector<String> room_List = new Vector<>();
    private Vector<String> Contact_user_list = new Vector<>();
    ClientNetwork(Client_GUI GUI){
        this.GUI = GUI;
    }
    @Override
    public void in_message(String message) {
        StringTokenizer st = new StringTokenizer(message, "/");

        String protocol = st.nextToken();
        String Message = st.nextToken();

        System.out.println("프로토콜" + protocol);
        System.out.println("내용" + Message);
        switch (protocol) {
            case "connect": // 접속 성공시
                System.out.println("접속 성공");
                break;
            case "login_ok": // 로그인 성공시
                GUI.Login_GUI.setVisible(false);
                GUI.F_R_GUI.setVisible(true);
                fr_List.add(ID);
                GUI.fr_list.setListData(fr_List);
                break;
            case "login_fail": // 로그인 실패시
                JOptionPane.showMessageDialog(null, "로그인 실패", "알림", JOptionPane.ERROR_MESSAGE);
                break;
            case "Register": // 회원가입 성공시
                JOptionPane.showMessageDialog(null, "회원가입 되셨습니다", "알림", JOptionPane.INFORMATION_MESSAGE);
                GUI.Rg_GUI.setVisible(false);
                break;
            case "RegisterFail": // 회원가입 실패시
                JOptionPane.showMessageDialog(null, "이미 똑같은 ID가 있습니다", "알림", JOptionPane.ERROR_MESSAGE);
                break;
            case "JoinRoom": // 방접속 성공시
                Curent_Room_Name = Message;
                Contact_user_list.removeAllElements();
                GUI.Chatting_area.setText("");
                GUI.Chat_init(Message);// 채팅창 구성 메소드
                GUI.Chat_GUI.setVisible(true);
                break;
            case "CreateRoom": // 방생성 성공시
                Contact_user_list.removeAllElements();
                GUI.Chatting_area.setText("");
                GUI.Chat_init(Message);// 채팅창 구성 메소드

                GUI.Chat_GUI.setVisible(true);
                break;
            case "New_Room": // 새로운 방알림
                room_List.add(Message);
                GUI.Room_list.setListData(room_List);
                break;
            case "Chatting": // 채팅 처리
                String msg = st.nextToken();
                GUI.Chatting_area.append(Message + msg);
                break;
            case "OldUser": // 기존 유저목록 받아오기
                fr_List.add(Message);
                break;
            case "New User": // 새로운 접속자
                fr_List.add(Message);
                break;
            case "OldRoom": // 기존 방목록 받아오기
                room_List.add(Message);
                break;
            case "room_list_update": // 방목록 GUI갱신
                GUI.Room_list.setListData(room_List);
                break;
            case "user_list_update": // 접속자목록 GUI갱신
                GUI.fr_list.setListData(fr_List);
                break;
            case "InviteFail": // 초대 실패시
                JOptionPane.showMessageDialog(null, "초대 실패", "알림", JOptionPane.ERROR_MESSAGE);
                break;
            case "RemoveRoom": // 방이 제거될시
                room_List.remove(Message);
                GUI.Room_list.setListData(room_List);
                break;
            case "invite": // 초대를 받았을시
                int ask = JOptionPane.showConfirmDialog(null, "채팅방에 초대를 받았습니다 수락하시겠습니까?(현재채팅방에선 나가게 됩니다)", Message + "로부터",
                        JOptionPane.YES_NO_OPTION);
                if (ask == JOptionPane.YES_OPTION) {
                    String password = st.nextToken();
                    send_message("RoomOut/ok");
                    send_message("JoinRoom/" + Message + "/" + password);
                }
                break;
            case "contact_user": // 방에 접속시 방의 접속된 기존 접속자들 목록
                // 받아오기
                Contact_user_list.add(Message);
                GUI.contact_user_list.setListData(Contact_user_list);
                break;
            case "Out_User": // 방에서 접속자가 나갔을때
                Contact_user_list.remove(Message);
                GUI.contact_user_list.setListData(Contact_user_list);
                break;
            case "UserOut": // 접속자가 접속을 끊었을시
                fr_List.remove(Message);
                break;
        }
    }
    public void session() {
        Connect();
        send_message("connect/ok");
        Runnable r = () -> {
            while (true) {
                try {
                    String msg = dis.readUTF();
                    System.out.println("서버로부터 수신된 메세지" + msg);
                    in_message(msg);
                } catch (IOException e) {
                    try {
                        os.close();
                        is.close();
                        dos.close();
                        dis.close();
                        socket.close();
                        JOptionPane.showMessageDialog(null, "서버와의 접속 끊어짐", "알림", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "서버와의 접속이 정상적으로 끊어지지 않았습니다", "알림", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                }
            }
        };
        Thread th = new Thread(r);
        th.start();
    }

    @Override
    public void IOError(IOException e) {
        JOptionPane.showMessageDialog(null, "연결 실패\n"+e, "알림", JOptionPane.ERROR_MESSAGE);
    }
}