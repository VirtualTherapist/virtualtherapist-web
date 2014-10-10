package modelsTest;

import models.Question;
import org.junit.Test;
import static org.fest.assertions.Assertions.*;

/**
 * Created by wahid on 10/10/14.
 */
public class QuestionTest extends BaseModelTest {

    @Test
    public void save(){
        Question question = new Question();
        question.question = "Test vraag";
        question.save();

        assertThat(question.id).isNotNull();
    }
}
