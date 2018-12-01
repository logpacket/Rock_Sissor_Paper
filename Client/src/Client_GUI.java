import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Client_GUI extends JFrame {
    // Login GUI Field
    public JFrame Login_GUI = new JFrame();
    JTextField IP_tf = new JTextField();
    JTextField Login_ID_tf = new JTextField();
    JPasswordField Loginpf = new JPasswordField();
    JButton login_btn = new JButton("\uC811\uC18D");
    JButton go_rg = new JButton("\uD68C\uC6D0\uAC00\uC785");

    // Friends&Room GUI Field
    public JFrame F_R_GUI = new JFrame();
    JPanel F_RPane = new JPanel();
    public JList<String> fr_list = new JList<>();
    JButton invite_btn = new JButton("친구 초대");
    JButton create_room_btn = new JButton("방 만들기");
    JButton join_room_btn = new JButton("방 참가");
    public JList<String> Room_list = new JList<>();

    // chat GUI Field
    public JFrame Chat_GUI = new JFrame();
    private JPanel ChatPane = new JPanel();
    JButton rock_btn = new JButton("바위");
    JButton sissor_btn = new JButton("가위");
    JButton paper_btn = new JButton("종이");
    public JList<String> contact_user_list = new JList<>();
    public JTextArea Chatting_area = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane();
    private JScrollPane scrollPane_1 = new JScrollPane();

    // Register GUI Field
    public JFrame Rg_GUI = new JFrame();
    private JPanel rg_Pane = new JPanel();
    JTextField rg_ID_tf = new JTextField();
    JPasswordField rg_pf_1 = new JPasswordField();
    JTextField rg_server_IP = new JTextField();
    JButton register_btn = new JButton("회원가입");

    // GUI설정 메소드
    void Login_init() {
        Login_GUI.setTitle("Login Window");
        Login_GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Login_GUI.setBounds(100, 100, 273, 332);
        Login_GUI.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("IP");
        lblNewLabel.setBounds(12, 106, 63, 32);
        Login_GUI.getContentPane().add(lblNewLabel);

        JLabel lblId = new JLabel("ID");
        lblId.setBounds(12, 161, 63, 32);
        Login_GUI.getContentPane().add(lblId);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setBounds(12, 214, 63, 32);
        Login_GUI.getContentPane().add(lblPassword);

        IP_tf.setBounds(87, 112, 145, 21);
        Login_GUI.getContentPane().add(IP_tf);
        IP_tf.setColumns(10);

        Login_ID_tf.setColumns(10);
        Login_ID_tf.setBounds(87, 167, 145, 21);
        Login_GUI.getContentPane().add(Login_ID_tf);

        Loginpf.setBounds(87, 220, 145, 21);
        Login_GUI.getContentPane().add(Loginpf);

        JLabel lblNewLabel_1 = new JLabel("Login");
        lblNewLabel_1.setFont(new Font("한컴 바겐세일 M", Font.PLAIN, 40));
        lblNewLabel_1.setBounds(12, 10, 220, 75);
        Login_GUI.getContentPane().add(lblNewLabel_1);

        login_btn.setBounds(12, 256, 97, 23);
        Login_GUI.getContentPane().add(login_btn);

        go_rg.setBounds(135, 256, 97, 23);
        Login_GUI.getContentPane().add(go_rg);
        Login_GUI.setVisible(true);
    }
    void F_R_init() {
        F_R_GUI.setTitle("Friends&Room Window");
        F_R_GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        F_R_GUI.setBounds(100, 100, 506, 367);
        F_RPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        F_R_GUI.setContentPane(F_RPane);
        F_RPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(14, 10, 196, 248);
        F_RPane.add(scrollPane);

        scrollPane.setViewportView(fr_list);

        invite_btn.setBounds(62, 282, 97, 23);
        F_RPane.add(invite_btn);

        create_room_btn.setBounds(259, 282, 97, 23);
        F_RPane.add(create_room_btn);

        join_room_btn.setBounds(358, 282, 97, 23);
        F_RPane.add(join_room_btn);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(259, 10, 196, 248);
        F_RPane.add(scrollPane_1);

        scrollPane_1.setViewportView(Room_list);
        F_R_GUI.setVisible(false);
    }
    public void Chat_init(String Title) {
        Chat_GUI.setTitle(Title);
        Chat_GUI.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Chat_GUI.setBounds(100, 100, 486, 523);
        ChatPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        Chat_GUI.setContentPane(ChatPane);
        ChatPane.setLayout(null);

        scrollPane.setBounds(104, 10, 354, 409);
        ChatPane.add(scrollPane);

        Chatting_area.setEditable(false);
        scrollPane.setViewportView(Chatting_area);

        rock_btn.setBounds(387,429,71,46);
        ChatPane.add(rock_btn);
        sissor_btn.setBounds(307,429,71,46);
        ChatPane.add(sissor_btn);
        paper_btn.setBounds(227,429,71,46);
        ChatPane.add(paper_btn);

        scrollPane_1.setBounds(12, 10, 82, 409);
        ChatPane.add(scrollPane_1);

        scrollPane_1.setViewportView(contact_user_list);
    }
    void Register_init() {
        Rg_GUI.setTitle("Register Window");
        Rg_GUI.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Rg_GUI.setBounds(100, 100, 334, 388);
        rg_Pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        Rg_GUI.setContentPane(rg_Pane);
        rg_Pane.setLayout(null);

        JLabel lblId = new JLabel("ID");
        lblId.setFont(new Font("Aparajita", Font.PLAIN, 20));
        lblId.setBounds(12, 170, 108, 31);
        rg_Pane.add(lblId);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Aparajita", Font.PLAIN, 20));
        lblPassword.setBounds(12, 203, 108, 44);
        rg_Pane.add(lblPassword);

        JLabel lblRegister = new JLabel("Register");
        lblRegister.setFont(new Font("한컴 바겐세일 M", Font.PLAIN, 40));
        lblRegister.setBounds(26, 10, 250, 75);
        rg_Pane.add(lblRegister);

        JLabel lblServerip = new JLabel("ServerIP");
        lblServerip.setFont(new Font("Aparajita", Font.PLAIN, 20));
        lblServerip.setBounds(12, 95, 108, 31);
        rg_Pane.add(lblServerip);

        rg_ID_tf.setBounds(160, 174, 116, 21);
        rg_Pane.add(rg_ID_tf);
        rg_ID_tf.setColumns(10);

        register_btn.setBounds(26, 302, 250, 38);
        rg_Pane.add(register_btn);

        rg_pf_1.setBounds(160, 214, 116, 21);
        rg_Pane.add(rg_pf_1);

        Rg_GUI.setVisible(false);

        rg_server_IP.setColumns(10);
        rg_server_IP.setBounds(160, 95, 116, 21);
        rg_Pane.add(rg_server_IP);
    }

}
