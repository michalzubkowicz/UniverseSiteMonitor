package controllers;

import models.Check;
import models.Service;
import models.ServiceView;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.List;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }

    public static Result check() {
        try {
            Check.now();
        } catch(Exception e) {
            Logger.error(e.getMessage(), e);
            return ok(e.getMessage());
        }
        return ok("");
    }

    public static Result services() {
        List<ServiceView> o = Service.getPublicServices();
        return ok(Json.toJson(o));
    }


}
