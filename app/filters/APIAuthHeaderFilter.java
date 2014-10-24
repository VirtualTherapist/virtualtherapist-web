package filters;

import com.avaje.ebean.Ebean;
import models.User;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by bas on 21-10-14.
 */
public class APIAuthHeaderFilter extends Action.Simple {

    @Override
    public F.Promise<Result> call(Http.Context context) throws Throwable {
        Http.Request request = context.request();
        String secret = request.getHeader("authentication");

        boolean isAuthenticated = authenticate(secret);

        if(isAuthenticated) {
            return delegate.call(context);
        }else{
            return F.Promise.pure((Result) unauthorized());
        }
    }

    public static boolean authenticate(String secret) {
        User user = Ebean.find(User.class).where()
                .eq("password", secret).findUnique();

        if(user != null)
            return true;
        else
            return false;
    }
}
