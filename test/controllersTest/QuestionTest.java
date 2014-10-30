package controllersTest;

import controllers.routes;
import com.avaje.ebean.Ebean;
import org.junit.Test;
import org.junit.Ignore;
import test.BaseTest;
import modelsTest.DatabaseFunctions;
import models.Question;
import play.test.FakeRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

@Ignore
public class QuestionTest extends BaseTest
{
    @Test
    public void testDeleteQuestion()
    {
        running(fakeApp, new Runnable()
        {
            public void run()
            {
                Question question = DatabaseFunctions.addQuestion();
                callAction(routes.ref.QuestionController.deleteQuestion(question.id));
                
                question = Ebean.find(Question.class, question.id);
                assertThat(question).isNull();
            }
        });
    }

    @Test
    public void testUpdateQuestion()
    {
        running(fakeApp, new Runnable()
        {
            public void run()
            {
                Question question = DatabaseFunctions.addQuestion();

                Map<String, String> data = new HashMap<>();
                data.put("pk", Integer.toString(question.id));
                data.put("value", "Has this question changed?");

                FakeRequest request = new FakeRequest().withFormUrlEncodedBody(data);
                callAction(routes.ref.QuestionController.updateQuestion(question.id), request);
                
                question = Ebean.find(Question.class, question.id);
                assertThat(question.question).isEqualTo("Has this question changed?");
            }
        });
    }
}
