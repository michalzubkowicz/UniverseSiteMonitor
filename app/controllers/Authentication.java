package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.adminlogin;

import static play.data.Form.form;

public class Authentication extends Controller {

    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(adminlogin.render(loginForm));
        } else {
            session().clear();
            session("username", loginForm.get().adminusername);
            session("usergroup", User.USER_PANEL);
            return redirect(
                    routes.Admin.index()
            );
        }
    }

    public static Result login() {
        return ok(
                adminlogin.render(form(Login.class))
        );
    }


    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Authentication.login()
        );
    }

    public static class Login {

        public String adminusername;
        public String adminpassword;

        public String getAdminusername() {
            return adminusername;
        }

        public void setAdminusername(String adminusername) {
            this.adminusername = adminusername;
        }

        public String getAdminpassword() {
            return adminpassword;
        }

        public void setAdminpassword(String adminpassword) {
            this.adminpassword = adminpassword;
        }

        public String validate() {
            if (!User.authenticate(adminusername, adminpassword)) {
                return "Invalid username or password";
            }
            return null;
        }
    }

}
