import java.util.LinkedHashMap;
import java.util.Map;



public class Room_info {
    public String Room_name = "";
    public Map<String,User_info> Room_User_List= new LinkedHashMap<>();
    public String Room_Password = "";

    Room_info(String name, User_info u, String Password) {
        this.Room_name = name;
        this.Room_Password = Password;
        Add_User(u);
    }

    public void BroadCast_Room(String str) {
        for(Map.Entry<String,User_info> u : Room_User_List.entrySet())
            u.getValue().send_message(str);
    }

    public void Add_User(User_info u) {
        this.Room_User_List.put(u.ID,u);
    }
}

