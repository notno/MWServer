import com.cycling74.max.*;
import java.math.BigInteger;

/**
 * fibonacci - calculates the nth fibonacci number
 * using the BigInteger class.
 * 
 * created in Feb 2004
 * @author bbn
 */
public class fibonacci extends MaxObject {

	private BigInteger a = BigInteger.valueOf(0);
	private BigInteger b = BigInteger.valueOf(1);
	private BigInteger c = BigInteger.valueOf(1);
	
	int radix = 10; //start with base 10
	
	public fibonacci(Atom[] args) {
		declareAttribute("radix");
	}
	
	public void inlet(int i) {
		if (i<=0) {
			error("fibonacci: needs an integer index i > 0");
			return;
		}
		a = BigInteger.valueOf(0);
		b = BigInteger.valueOf(1);
		
		for (int j = 2; j <= i; j++) {
			c=a.add(b);
			a=b;
			b=c;
		}
		outlet(0,b.toString(radix));
	}
}