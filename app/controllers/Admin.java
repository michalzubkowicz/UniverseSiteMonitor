package controllers;


import com.mongodb.BasicDBObject;
import models.Label;
import models.Service;
import models.User;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.admin;

import java.util.List;

@Security.Authenticated(Secured.class)
public class Admin extends Controller {


    public static Result index() {
        return ok(admin.render());
    }


    public static Result indexLabel() {
        List<Label> o = Label.collection.find().sort(new BasicDBObject("name ASC",-1)).toArray();

        return ok(Json.toJson(o));
    }

    public static Result getLabel(String id) {
        try {
            Label o = Label.collection.findOneById(id);
            return status(202, Json.toJson(o));
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            return badRequest(e.getMessage());
        }
    }

    public static Result putLabel() {
        try {
            Label o = Label.createFromJson(request().body().asJson());
            o.update();
            return status(202, Json.toJson(o));
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            return badRequest(e.getMessage());
        }
    }

    public static Result deleteLabel(String id) {
        try {
            Label.removeById(id);
            return status(202, "Object deleted");
        } catch(Exception e) {
            Logger.error(e.getMessage(),e);
            return badRequest(e.getMessage());
        }
    }

    public static Result indexResponse() {
        List<Service> o = Service.collection.find().sort(new BasicDBObject("created DESC",-1)).toArray();
        return ok(Json.toJson(o));
    }

    public static Result indexService() {
        List<Service> o = Service.collection.find().sort(new BasicDBObject("name ASC",-1)).toArray();
        return ok(Json.toJson(o));
    }

    public static Result getService(String id) {
        try {
            Service o = Service.collection.findOneById(id);
            return status(202, Json.toJson(o));
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            return badRequest(e.getMessage());
        }
    }

    public static Result putService() {
        try {
            Service o = Service.createFromJson(request().body().asJson());
            o.save();
            return status(202, Json.toJson(o));
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
            return badRequest(e.getMessage());
        }
    }

    public static Result deleteService(String id) {
        try {
            Service.removeById(id);
            return status(202, "Object deleted");
        } catch(Exception e) {
            Logger.error(e.getMessage(),e);
            return badRequest(e.getMessage());
        }
    }

    public static Result changepassword() {
        try {
            User.changeCurrentUserPassword(request().body().asFormUrlEncoded().get("password")[0]);
        } catch(Exception e) {
            Logger.error("Error when changing password:"+e.getMessage(),e);
            return internalServerError(e.getMessage());
        }
        return ok("");
    }



}