package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mongodb.BasicDBObject;
import org.mongojack.JacksonDBCollection;
import play.Logger;
import play.modules.mongojack.MongoDB;

import java.util.Date;
import java.util.List;


public class Response extends AbstractModel implements AbstractModelInterface {

    public static JacksonDBCollection<Response, String> collection = MongoDB.getCollection("responses", Response.class, String.class);

    private Service service;

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


    private Date created=new Date();

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    private String responsecode;

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public void save() {
        if(this.id==null) {
            this.id = collection.insert(this).getSavedId();
        } else {
            this.update();
        }
    }

    public void update() {
        Logger.error("Update called. Should not happend");
    }


    public void remove() {
        Logger.error("Remove called. Should not happend");
    }


    @JsonIgnore
    private static int pagelimit=100;

    public static List<Response> getPaginatedResponses(int skip) {
        return Response.collection.find().sort(new BasicDBObject("created",-1)).skip(skip).limit(pagelimit).toArray();
    }

}