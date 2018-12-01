import javax.swing.*;
import java.awt.event.*;

public final class Client extends Client_GUI implements ActionListener, WindowListener, KeyListener{
    private ClientNetwork network = new ClientNetwork(this);
    public static void main(String args[]) {
        new Client();
    }
    private Client(){
        Login_init();// 로그인화면 구성 메소드
        F_R_init();// 접속자및 방리스트 구성 메소드
        Register_init();// 회원가입 구성 메소드
        start();
    }

    private void send_message_GUI(String name){
        String room_name = Chat_GUI.getTitle();
        network.send_message("Chatting/" + room_name + "/" + name);
    }

    private void start() {// 프로그램 시작시 액션리스너 활성
        login_btn.addActionListener(this);
        go_rg.addActionListener(this);
        invite_btn.addActionListener(this);
        create_room_btn.addActionListener(this);
        join_room_btn.addActionListener(this);
        register_btn.addActionListener(this);
        rock_btn.addActionListener(this);
        sissor_btn.addActionListener(this);
        paper_btn.addActionListener(this);
        //send_btn.addActionListener(this);
        //Chat_tf.addKeyListener(this);
        Chat_GUI.addWindowListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object event = e.getSource();
        if (event == login_btn) {// 로그인 버튼 누를시
            network.IP = IP_tf.getText().trim();
            network.ID = Login_ID_tf.getText().trim();
            network.Password = String.valueOf(Loginpf.getPassword());// 회원정보 전송
            network.session();
            network.send_message("login/" + network.ID + "/" + network.Password);
        } else if (event == go_rg) {// 회원가입창 열기
            Rg_GUI.setVisible(true);
        } else if (event == register_btn) {// 회원가입 버튼 누를시
            network.IP = rg_server_IP.getText().trim();
            network.session();
            network.ID = rg_ID_tf.getText().trim();
            network.Password = String.valueOf(rg_pf_1.getPassword()).trim();
            network.send_message("register/" + network.ID +"/"+network.Password);
        } // 회원정보 전송

        else if (event == create_room_btn) {// 방생성 버튼 누를시
            String room_name = JOptionPane.showInputDialog("방 이름");
            String room_password = JOptionPane.showInputDialog("방 비밀번호");// 방정보
            // 받아오고
            if (room_name != null) {
                if(!(network.Curent_Room_Name.equals(""))){
                    network.send_message("RoomOut/ok");// 기존에 접속해있던 방 나와서
                }
                network.send_message("CreateRoom/" + room_name + "/" + room_password);// 방정보
                // 보내주고
            } else {
                JOptionPane.showMessageDialog(null, "방이름을 적어주세요", "알림", JOptionPane.ERROR_MESSAGE);// 방이름
                // 안적었을떄
            }
        } else if (event == invite_btn) {// 초대 버튼 누를시
            String fr = fr_list.getSelectedValue();
            String room = JOptionPane.showInputDialog("방이름");// 방정보 받아오기
            if (fr != null && room != null) {
                network.send_message("inviteRoom/" + fr + "/" + room);// 초대할 친구 이름이랑 방정보
                // 보내기
            }
        } else if (event == rock_btn || event == sissor_btn || event == paper_btn) {// 가위바위보 버튼 누름시
            send_message_GUI(((JButton)event).getText());
        }
        else if (event == join_room_btn) {// 방 참가 버튼 누를시
            String room_name = Room_list.getSelectedValue();
            String Password = JOptionPane.showInputDialog("방 비밀번호");// 방정보
            // 사용자로부터
            // 받아오고
            network.send_message("RoomOut/ok");// 기존에 있던 방에서 나오고
            network.send_message("JoinRoom/" + room_name + "/" + Password);// 접속 시도
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {


    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        network.send_message("RoomOut/ok");

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
