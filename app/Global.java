import com.mongodb.BasicDBObject;
import models.Check;
import models.Label;
import models.Response;
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.libs.Akka;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

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

        if(!Response.collection.isCapped()) {

            //MongoClient.connect().eval("db.runCommand(\"convertToCapped\": \"responses\", size: 10485760});"); //TODO
            Logger.info("Response collection is not capped, if you want to be capped run manually: db.runCommand({\"convertToCapped\": \"responses\", size: 1000});");
        }

        Akka.system().scheduler().schedule(
                Duration.create(1, TimeUnit.MINUTES),Duration.create(1,TimeUnit.MINUTES),
                new Runnable() {
                    public void run() {
                        try {
                            Check.now();
                        } catch(Exception e) {
                            Logger.error(e.getMessage());
                        }
                    }
                },
                Akka.system().dispatcher()
        );



    }




}