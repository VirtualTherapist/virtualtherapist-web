package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result index()
    {
        if( session("loggedin") != null && session("loggedin").equals("true") )
        {
            return ok(index.render("Your new application is ready."));
        }
        else
        {
            return redirect("/login");
        }
    }

}
