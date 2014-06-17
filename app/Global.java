import com.mongodb.BasicDBObject;
import models.Label;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {

        // Check if the database is empty
        if (!play.api.Play.isTest(play.api.Play.current())) {
            if(Label.collection.count()==0) {
                Label l = new Label();
                l.setName("test");
                l.save();
                l.remove();
            }
            Label.collection.ensureIndex(new BasicDBObject("name", 1), null, true);

            if(User.collection.count()==0) {
                User u = new User();
                u.setNewPassword("DefaultPassword123");
                u.setUsername("ad");
                User.collection.insert(u);
                Logger.info("Creating default user: ad , password: DefaultPassword123");
                //nowe u mnie to 123
            }

            User.collection.ensureIndex(new BasicDBObject("username", 1), null, true);
        }



    }




}