import com.cycling74.max.*;
import java.lang.Math;

public class help extends MaxObject implements Executable {

    private MaxClock _clock = new MaxClock(this);
	private int numShifts = 0;
	private int dir = 1;
	private static final int DEFAULT_MAXLENGTH = 75;	
	private double interval = 300; //default to 50ms clock interval	
	public double holdInterval = 2222;
	private StringBuffer version = 
		new StringBuffer("mxj max 4.5");
		
	private int[] fromIdx = new int[DEFAULT_MAXLENGTH];
	private int[] toIdx = new int[DEFAULT_MAXLENGTH];
	
	private boolean _in_cc;	
    public help(Atom args[]) {
        declareIO(1,1);
		_in_cc = true;
    }

	public void loadbang() {
		_clock.delay(0.0);
	//look how cool we are using max threads and java threads together.
		Thread t = new Thread(new Runnable(){
			public void run()
			{		
				int r = 5, g = 5, b = 5, dir = 1;	
				final MaxPatcher p = getParentPatcher();	
				p.getWindow().setGrow(false);
				while(_in_cc)
				{
				
					if(r == 255 || r == 0)
						dir = -dir;
					r+=dir;g+=dir;b+=dir;					
					final int fr = r;
					final int fg = g;
					final int fb = b;		
					MaxSystem.deferLow(new Executable(){
						public void execute()
						{
							// check if there's object(s) in the patch (test if the patcher is still valid)
							if (p.getCount() > 0)
								p.setBackgroundColor(fr,fg,fb);
						}
						});
					try{
						Thread.sleep(60);
					}catch(Exception e){}
				}
			}						
		});
		t.start();

	}
	
	public void notifyDeleted() {
		_in_cc = false;
		_clock.release();

	}

	public void maxLength(int i) {		
		fromIdx = new int[i];
		toIdx = new int[i];
		numShifts = 0;
	}

	private int randomInt(int max) {
		return (int)((double)max * Math.random());
	}
	
	private void charSwap(StringBuffer sb, int from, int to) {
		char c = sb.charAt(from);
		sb.deleteCharAt(from);
		sb.insert(to, c);
	}

    //clock callback
    public void execute() {
		if (numShifts == 0) {
			_clock.delay(holdInterval);
			numShifts++;
		} else if (dir==1) {
			_clock.delay(interval);
			fromIdx[numShifts] = randomInt(version.length()-1);
			toIdx[numShifts] = randomInt(version.length()-1);
			charSwap(version, fromIdx[numShifts], toIdx[numShifts]);	
			numShifts++;
			if (numShifts >= fromIdx.length-1) 
				dir = -1;
			interval *= 0.9;
		} else {
			_clock.delay(interval);
			charSwap(version, toIdx[numShifts], fromIdx[numShifts]);
			numShifts--;
			if (numShifts <= 0) 
				dir = 1;
			interval *= 1/0.9;
		}
		
		outlet(0, Atom.parse(version.toString(),false));
	}
}




