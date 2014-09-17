package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import org.mongojack.DBQuery;
import org.mongojack.DBUpdate;
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

    private Boolean notified=true;

    public Boolean getNotified() {
        return notified;
    }

    public void setNotified(Boolean notified) {
        this.notified = notified;
    }

    private Boolean ok=false;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
        if(ok.equals(Boolean.TRUE)) this.setNotified(false);
    }

    public static boolean shouldSendNotification(String id) {
        return !collection.findAndModify(
                DBQuery
                        .is("active",true)
                        .is("ok",false)
                        .is("notified",false)
                        .greaterThanEquals("retries", 1)
                        .is("_id",id),
                DBUpdate.set("notified",true)
        ).getNotified();

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

    private Integer retries=0;

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
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
        return Service.publiccollection.find(DBQuery.is("active",true).is("guestaccess",true)).sort(new BasicDBObject("name", 1)).toArray();
    }

    public static void saveError(String id, String lastresponse, String lastresponsecode) {
        collection.update(
                DBQuery
                        .is("active",true)
                        .is("ok",true)
                        .is("_id",id),
                DBUpdate.set("ok",false)
                        .set("lastresponse",lastresponse)
                        .set("lastresponsecode",lastresponsecode)
                        .set("seen",new Date())
                        .set("notified",false)
                        .inc("retries")
        );
    }

    public static void saveOK(String id) {
        collection.update(
                DBQuery
                        .is("active",true)
                        .is("ok",false)
                        .is("_id",id),
                DBUpdate.set("ok",true)
                        .set("retries", 0)
                        .set("lastresponse","")
                        .set("lastresponsecode","200")
                        .set("notified",false)
        );
    }


}