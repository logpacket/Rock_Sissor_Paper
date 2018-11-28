import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.*;


public class DBController {
    private MongoClient mongo_client  = MongoClients.create("mongodb://localhost/test:27017");
    private MongoDatabase db = mongo_client.getDatabase("test");
    private MongoCollection<Document> Users = db.getCollection("Users");
    public Boolean Register(String ID,String Password){
        try{
            Document d = new Document();
            d.put("ID",ID);
            d.put("Password",Password);
            Users.insertOne(d);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public Boolean Login(String ID,String Password){
        Document Query = new Document();
        Query.put("ID",ID);
        for (Document d : Users.find(Query)) {
            if (d.get("Password").equals(Password)) {
                return true;
            }
        }
        return false;
    }
}
