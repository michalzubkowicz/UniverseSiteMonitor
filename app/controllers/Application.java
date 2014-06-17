package controllers;

import models.Check;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
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
}
