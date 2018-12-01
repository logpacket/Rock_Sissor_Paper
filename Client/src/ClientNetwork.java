
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
    private String myCard = ""; //나의 패 임시저장
    private String yourCard = ""; //상대의 패 임시저장

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

                if(Message.equals(this.ID) && myCard.equals("")){
                    //내가 보낸 메세지
                    myCard = msg;
                }
                else if(!Message.equals(this.ID) && yourCard.equals("")){
                    //상대가 보낸 메세지
                    yourCard = msg;
                }

                if(!yourCard.equals("") && !myCard.equals("")){ //승패여부 판단시작. 둘다 패를 냈을때
                    System.out.println("myCard :"+myCard);
                    System.out.println("yourCard :"+ yourCard);
                    int winToken = 0;
                    if(yourCard.equals(this.myCard)){
                        winToken = 2;   //무승부
                    }

                    if( (yourCard.equals("가위") && myCard.equals("바위")) || (yourCard.equals("바위") && myCard.equals("종이")) || (yourCard.equals("종이") && myCard.equals("가위")) ){
                        winToken = 1;   //이겼을때
                        System.out.println(this.ID+"win!");
                    }

                    switch(winToken){
                        case 0:
                            JOptionPane.showMessageDialog(null, "졌습니다!");
                            break;
                        case 1:
                            JOptionPane.showMessageDialog(null, "이겼습니다!");
                            break;
                        case 2:
                            JOptionPane.showMessageDialog(null, "비겼습니다!");
                    }

                    myCard = "";
                    yourCard = "";
                }
                /*
                if(Message.equals(this.ID)){    //내가 보낸 메세지일때
                    System.out.println(this.ID+"가 보낸 메세지 " +msg);
                    card = msg;
                    if(!(yourcard.equals(""))){   //상대가 낸거랑 내가 낸거 둘다 있을때

                        if (yourcard.equals(this.card)){
                            JOptionPane.showMessageDialog(null,"비겼습니다!");
                        }
                        else if (yourcard.equals("바위")){
                            if (card.equals("종이")){
                                JOptionPane.showMessageDialog(null,"이겼습니다!");
                            }
                            else if (card.equals("가위")){
                                JOptionPane.showMessageDialog(null,"쪘습니다!");
                            }
                        }
                        else if (yourcard.equals("가위")){
                            if (card.equals("바위")){
                                JOptionPane.showMessageDialog(null,"이겼습니다!");
                            }
                            else if(card.equals("종이")){
                                JOptionPane.showMessageDialog(null,"졌습니다!");
                            }
                        }
                        else if (yourcard.equals("종이")){
                            if(card.equals("가위")){
                                JOptionPane.showMessageDialog(null,"이겼습니다!");
                            }
                            else if(card.equals("바위")){
                                JOptionPane.showMessageDialog(null,"졌습니다!");
                            }
                        }
                        card = "";
                        yourcard = "";
                    }
                }
                else{   //상대가 보낸 메세지일때
                    if(card.equals("")){    //상대가 먼저 패를 냈을 떄
                        yourcard = msg;
                    }
                    else {  //내가 먼저 패를 냈을 때
                        System.out.println("msg : "+msg);
                        if (msg.equals(this.card)){
                            JOptionPane.showMessageDialog(null,"비겼습니다!");
                        }
                        else if (msg.equals("바위")){
                            System.out.println(ID+"는 바위를 냈다");
                            if (card.equals("종이")){
                                JOptionPane.showMessageDialog(null,"이겼습니다!");
                            }
                            else if (card.equals("가위")){
                                JOptionPane.showMessageDialog(null,"쪘습니다!");
                            }
                        }
                        else if (msg.equals("가위")){
                            if (card.equals("바위")){
                                JOptionPane.showMessageDialog(null,"이겼습니다!");
                            }
                            else if(card.equals("종이")){
                                JOptionPane.showMessageDialog(null,"졌습니다!");
                            }
                        }
                        else if (msg.equals("종이")){
                            if(card.equals("가위")){
                                JOptionPane.showMessageDialog(null,"이겼습니다!");
                            }
                            else if(card.equals("바위")){
                                JOptionPane.showMessageDialog(null,"졌습니다!");
                            }
                        }

                        card = "";
                        yourcard = "";
                    }
                }
                */
                GUI.Chatting_area.append(Message + msg+"\n");
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