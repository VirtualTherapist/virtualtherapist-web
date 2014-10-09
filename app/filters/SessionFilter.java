package filters;

import play.Logger;
import play.Play;
import play.api.libs.Crypto;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Calendar;
import java.util.Date;

import static play.mvc.Controller.session;

/**
 * Created by Akatchi on 9-10-2014.
 */
public class SessionFilter extends Action.Simple
{
    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable
    {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.getTime();

        if( session(Crypto.encryptAES("expiretime")) != null && Long.valueOf(Crypto.decryptAES(session(Crypto.encryptAES("expiretime")))) > Long.valueOf(cal.getTimeInMillis()) )
        {
            cal.add(Calendar.HOUR_OF_DAY, Integer.valueOf(Play.application().configuration().getString("session.expererationtime")));
            cal.getTime();

            session().remove(Crypto.encryptAES("expiretime"));
            session(Crypto.encryptAES("expiretime"), Crypto.encryptAES(String.valueOf(cal.getTimeInMillis())));

            if (session(Crypto.encryptAES("loggedin")) != null && session(Crypto.encryptAES("loggedin")).equals(Crypto.encryptAES("true")))
            {
                return delegate.call(ctx);
            }
            else
            {
                return F.Promise.pure(redirect("/login"));
            }
        }
        else
        {
            return F.Promise.pure(redirect("/login"));
        }
    }
}
