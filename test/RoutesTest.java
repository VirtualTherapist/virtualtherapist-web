import org.junit.Test;
import play.mvc.Result;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.assertThat;

 /**
 * Created by wahid on 10/10/14.
 */
public class RoutesTest {
   @Test
   public void testGetLogin() {
       Result result = route(fakeRequest(GET, "/login"));
       assertThat(result).isNotNull();
   }
}
