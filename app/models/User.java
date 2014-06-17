package models;

import org.mindrot.jbcrypt.BCrypt;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import play.modules.mongojack.MongoDB;

import javax.persistence.Entity;

import static play.mvc.Controller.session;


@Entity
public class User extends AbstractModel implements  AbstractModelInterface {
    public final static String USER_PANEL="1";
    public final static String USER_FRONT="0";

    public static JacksonDBCollection<User, String> collection = MongoDB.getCollection("users", User.class, String.class);

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password=password;
    }

    public void setNewPassword(String enteredPassword) {
        this.password=BCrypt.hashpw(enteredPassword, BCrypt.gensalt());
    }

    private Boolean checkPassword(String enteredPassword) {
        return BCrypt.checkpw(enteredPassword, password);
    }

    public static Boolean authenticate(String username, String enteredPassword) {
        try {
            if(username.length()<1 || enteredPassword.length()<1) return false;
            DBQuery.Query q = DBQuery.is("username", username);
            User u = collection.findOne(q);
            return u.checkPassword(enteredPassword);
        } catch(NullPointerException e) {
            return false;
        }
    }

    public void save() {
        if(this.id==null) {
            this.id = collection.insert(this).getSavedId();
        } else {
            this.update();
        }
    }

    public void update() {
        collection.updateById(this.id,this);
    }


    public void remove() {
        collection.removeById(this.id);
    }


    public static void removeById(String id) {
        collection.removeById(id);
    }

    public static void changeCurrentUserPassword(String pass) {
        DBQuery.Query q = DBQuery.is("username", session("username"));
        User u = collection.findOne(q);
        u.setNewPassword(pass);
        u.save();
    }

}
