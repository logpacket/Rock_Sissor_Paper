
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.StringTokenizer;
import java.io.*;

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

public class User_info extends AbstractNetwork implements Runnable{
    public String ID = "Guest";
    private String Password="";
    private String Current_Room_name;

    User_info(Socket socket){
        this.socket = socket;
        setStream();
    }

    @Override
    public void run() {
        while(true){
            try{
                String msg = dis.readUTF();
                Server.textArea.append(ID + "사용자로부터 들어온 메세지 " + msg + "\n");
                in_message(msg);
            }catch(IOException e){
                Server.textArea.append(ID+" 사용자 접속 끊어짐\n");
                try{
                    dos.close();
                    dis.close();
                    socket.close();
                    Server.Room_List.get(Current_Room_name).Room_User_List.remove(this.ID);
                    Server.BroadCast("UserOut/"+this.ID);
                    Server.BroadCast("user_list_update/ok");
                    Server.User_List.remove(this.ID);
                }catch(IOException e1){
                    IOError(e1);
                }catch(NullPointerException e2){
                    Server.BroadCast("UserOut/"+this.ID);
                    Server.BroadCast("user_list_update/ok");
                    Server.User_List.remove(this.ID);
                }
                break;
            }
        }
    }
    @Override
    public void in_message(String message) {

        StringTokenizer st = new StringTokenizer(message, "/");
        String protocol = st.nextToken();
        String Message = st.nextToken();
        System.out.println("프로토콜" + protocol);
        System.out.println("메세지" + message);
        switch (protocol) {
            case "Chatting": //채팅시에 프로토콜
                String msg = st.nextToken();
                Server.Room_List.get(Message).BroadCast_Room(("Chatting/" + ID + "/" + msg));
                break;
            case "JoinRoom": //방 입장시 프로토콜
                String ps = st.nextToken();
                Room_info r = Server.Room_List.get(Message);
                if (r.Room_Password.equals(ps)) {
                    // 새로운 사용자 알림
                    send_message("JoinRoom/" + Message);
                    r.BroadCast_Room("contact_user/" + this.ID);
                    // 사용자 추가
                    r.Add_User(this);
                    for(Map.Entry<String,User_info> u:r.Room_User_List.entrySet())
                        send_message("contact_user/"+u.getKey());
                    Current_Room_name = r.Room_name;
                }
                else send_message("JoinFail/sorry");
                break;
            case "connect": //접속 성공
                send_message("connect/ok");
                break;
            case "register": //회원가입시
                ID = Message;
                Password = st.nextToken();
                if (Server.db.Register(ID, Password)) {//회원가입 성공시
                    send_message("Register/ok");
                } else {//회원가입 실패시
                    send_message("RegisterFail/sorry");
                }
                break;
            case "login": //로그인시
                ID = Message;
                Password = st.nextToken();
                if (Server.User_List.get(ID) == null) {//중복이 아닐시
                    if (Server.db.Login(ID,Password)) {//회원정보가 맞다면
                        send_message("login_ok/" + ID);
                        //접속자 알림
                        Server.BroadCast("New User/" + ID);
                        for(Map.Entry<String,Room_info> room:Server.Room_List.entrySet()){
                            send_message("OldRoom/"+room.getKey());
                        }
                        for(Map.Entry<String,User_info> u:Server.User_List.entrySet()){
                            if(!(u.getKey().equals(this.ID))){
                                send_message("OldUser/"+u.getKey());
                            }
                        }
                        Server.User_List.put(this.ID,this);
                        Server.BroadCast("user_list_update/ok");
                        Server.BroadCast("room_list_update/ok");
                    } else{//회원정보가 맞지않았을 시
                        send_message("login_fail/ok");
                    }
                }
                break;
            case "CreateRoom": //방생성시
                String password = st.nextToken();
                // 1.현재 같은 방이 존재하는지 확인한다
                if(Server.Room_List.get(Message) == null){
                    Room_info new_room = new Room_info(Message, this, password);
                    Server.Room_List.put(new_room.Room_name,new_room);//전체 방 리스트에 추가
                    send_message("CreateRoom/" + Message);
                    send_message("contact_user/" + this.ID);
                    Current_Room_name = new_room.Room_name;
                    //방을 접속자들에게 알림
                    Server.BroadCast("New_Room/" + Message);
                    Server.BroadCast("room_list_update/ok");
                }else{
                    send_message("CreateRoomFail/ok");
                    break;
                }
                break;
            case "inviteRoom": //접속자 초대시
                String Room_name = st.nextToken();
                User_info u = Server.User_List.get(Message);
                Room_info room = Server.Room_List.get(Room_name);
                if((room.Room_User_List.get(this.ID) != null) && room.Room_name.equals(Room_name))
                    u.send_message("invite/"+room.Room_name+"/"+room.Room_Password);
                else send_message("InviteFail/ok");
                break;
            case "RoomOut": //방에서 나갔을시
                Room_info Room = Server.Room_List.get(Current_Room_name);
                try{
                    if(Room.Room_User_List.get(this.ID) != null){
                        Room.BroadCast_Room("Out_User/"+this.ID);
                        Room.Room_User_List.remove(this.ID);
                        Current_Room_name = null;
                    }
                }catch(NullPointerException e){
                    break;
                }
                if(Room.Room_User_List.size() == 0){
                    Server.BroadCast("RemoveRoom/"+Room.Room_name);
                    Server.Room_List.remove(Room.Room_name);
                }
                break;
        }
    }

    @Override
    public void IOError(IOException e) {
        Server.textArea.append("스트림 에러"+e);
    }
}
