package controllers;


import com.avaje.ebean.Ebean;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Created by bas on 9-10-14.
 */
public class AnalysisController extends Controller {

    public static Result analysisPageForUser(Integer userId) {
        User user = null;
        if(userId != null) {
            user = Ebean.find(User.class, userId);
        }
        return ok(analysis.render("Analyse overzicht", user));
    }

    public static Result analysisPage() {
        return ok(analysis.render("Analyse overzicht", null));
    }

}
