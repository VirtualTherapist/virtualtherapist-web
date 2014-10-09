package filters;

import play.api.libs.Crypto;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import views.html.register;

import java.util.Calendar;
import java.util.Date;

import static play.mvc.Controller.session;

/**
 * Created by Akatchi on 9-10-2014.
 */
public class LoginFilter extends Action.Simple
{
    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable
    {
        if (session(Crypto.encryptAES("loggedind")) != null && session(Crypto.encryptAES("loggedin")).equals(Crypto.encryptAES("true")))
        {
            return F.Promise.pure(redirect("/"));
        }
        else
        {
            return delegate.call(ctx);
        }
    }


}
