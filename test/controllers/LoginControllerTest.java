package controllers;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import models.UserRole;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.api.mvc.Request;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeRequest;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

/**
 * LoginControllerTest, tests the Login controller with all its methods
 */
public class LoginControllerTest extends DatabaseHelper {

    @Test
    public void getLoginTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Result result = callAction(
                        routes.ref.LoginController.login()
                );

                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("text/html");
                assertThat(charset(result)).isEqualTo("utf-8");
                assertThat(contentAsString(result)).contains("Login");
            }
        });
    }

    @Test
    public void validateLoginTest(){
        running(fakeApp, new Runnable(){
            public void run(){

                Result initDb = callAction(
                        routes.ref.LoginController.initializeDB()
                );

                Map<String, String> data = new HashMap<String, String>();
                data.put("email", "admin@therapist.com");
                data.put("password", "password");

                Result result = callAction(
                        routes.ref.LoginController.validateLogin(),
                        new FakeRequest(POST, "/login").withFormUrlEncodedBody(data)
                );

                assertThat(redirectLocation(result)).isEqualTo("/");

                assertThat(cookie("PLAY_SESSION",result)).isNotNull();
            }
        });
    }

    @Test
    public void createUserTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                initSession();
                Map<String, String> data = new HashMap<String, String>();
                data.put("first_name", "Piet");
                data.put("last_name", "Jansen");
                data.put("password", "pietjansen");
                data.put("email", "piet@jansen.nl");
                data.put("userrole", "Gebruiker");

                Result result = callAction(
                        routes.ref.LoginController.createUser(),
                        new FakeRequest(POST, "/profile/save").withCookies(cookie).withFormUrlEncodedBody(data)
                );

                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentType(result)).isEqualTo("text/html");
                assertThat(contentAsString(result)).contains("Gebruiker aangemaakt!");

            }
        });
    }
}
