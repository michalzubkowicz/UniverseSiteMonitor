package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import play.Logger;
import play.modules.mongojack.MongoDB;

import java.util.Date;
import java.util.List;

public class Service extends AbstractModel implements AbstractModelInterface {

    public static JacksonDBCollection<Service, String> collection = MongoDB.getCollection("services", Service.class, String.class);
    public static JacksonDBCollection<ServiceView, String> publiccollection = MongoDB.getCollection("services", ServiceView.class, String.class);

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private Date lastcheck;

    public Date getLastcheck() {
        return lastcheck;
    }

    public void setLastcheck(Date lastcheck) {
        this.lastcheck = lastcheck;
    }

    private String lastresponse;

    public String getLastresponse() {
        return lastresponse;
    }

    public void setLastresponse(String lastresponse) {
        this.lastresponse = lastresponse;
    }

    private String lastresponsecode;

    public String getLastresponsecode() {
        return lastresponsecode;
    }

    public void setLastresponsecode(String lastresponsecode) {
        this.lastresponsecode = lastresponsecode;
    }

    private Boolean notified;

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    private Boolean ok;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
        if(ok.equals(Boolean.TRUE)) this.setNotified(false);
        if(ok.equals(Boolean.FALSE) && this.notified.equals(Boolean.FALSE)) {
            this.sendNotification();
            this.setSeen(new Date());
        }
    }

    private Date seen;

    public Date getSeen() {
        return seen;
    }

    public void setSeen(Date seen) {
        this.seen = seen;
    }

    private Boolean guestaccess;

    public Boolean getGuestaccess() {
        return guestaccess;
    }

    public void setGuestaccess(Boolean guestaccess) {
        this.guestaccess = guestaccess;
    }

    private String expectedtext;

    public String getExpectedtext() {
        return expectedtext;
    }

    public void setExpectedtext(String expectedtext) {
        this.expectedtext = expectedtext;
    }

    private Integer interval=1;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    private Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void update() {
        collection.updateById(this.id,this);
        try {
            L.INSTANCE.reloadLabels();
        } catch(Exception e) {
            Logger.error(e.getMessage());
        }
    }

    public void save() {
        if(this.id==null) {
            this.id = collection.insert(this).getSavedId();
        } else {
            this.update();
        }
    }

    public void remove() {
        collection.removeById(this.id);
    }


    public static void removeById(String id) {
        collection.removeById(id);
    }

    public static Service createFromJson(JsonNode j) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        if(j.hasNonNull("_id")) {
            Service g = collection.findOneById(j.get("_id").asText());
            return mapper.readerForUpdating(g).readValue(j.toString());
        } else {
            return mapper.readValue(j.toString(), Service.class);
        }
    }

    public static List<Service> getActiveServices() {
        return Service.collection.find(DBQuery.is("active",true)).toArray();
    }

    public static List<ServiceView> getPublicServices() {
        return Service.publiccollection.find(DBQuery.is("active",true).is("guestaccess",true)).toArray();
    }

    public void sendNotification() {
        this.setNotified(true);
        try {

            Email email = new SimpleEmail();
            email.setHostName(play.Play.application().configuration().getString("email.host"));
            email.setSmtpPort(play.Play.application().configuration().getInt("email.port"));
            email.setAuthenticator(new DefaultAuthenticator(play.Play.application().configuration().getString("email.login"), play.Play.application().configuration().getString("email.pass")));
            if (play.Play.application().configuration().getInt("email.port") != 25)
                email.setSSLOnConnect(true);
            email.setFrom(play.Play.application().configuration().getString("email.from"));
            email.setSubject("Website " + this.getName() + " is unavailable!");
            email.setMsg("Please visit Universe Site Monitor for more details");
            email.addTo(play.Play.application().configuration().getString("email.to"));
            email.send();
        } catch (Exception me) {
            Logger.error("Error when sending notification email " + me.getMessage(), me);
        }
    }

}