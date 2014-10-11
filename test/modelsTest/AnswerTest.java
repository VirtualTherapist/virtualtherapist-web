package modelsTest;

import models.Answer;
import models.Question;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by wahid on 10/10/14.
 */
public class AnswerTest extends BaseModelTest {

    @Test
    public void save(){
        Question question = new Question();
        question.question = "Test Vraag";
        question.save();

        Answer answer = new Answer();
        answer.answer = "Antwoord op testvraag";
        answer.question = question;
        answer.save();

        assertThat(answer.id).isNotNull();
    }
}
