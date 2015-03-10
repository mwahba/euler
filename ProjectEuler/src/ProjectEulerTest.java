import java.math.BigInteger;
import static org.junit.Assert.*;
import org.junit.*;


public class ProjectEulerTest {

	@Test
	public void testBigIntegerDivideTwoByTwo() {
		BigInteger Two = new BigInteger("2"), answer = new BigInteger("1");
		assertTrue(Two.divide(new BigInteger("2")).equals(answer));
	}
}
