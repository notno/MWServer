/*
	super simple max scripting
*/

import com.cycling74.max.*;

public class scriptoscript extends MaxObject
{
	// global varables and code
	private int vobjectcount = 0;
	private MaxBox obarray[] = new MaxBox[256];
	private double vx = 300;
	private double vy = 300;
	private double vr = 50;
	private double colorA[] = new double[]{ 1., 0., 0., 1.};
	private double colorB[] = new double[]{ 0., 0., 1., 1.};
	
	scriptoscript()
	{
		
	}

	public void center(double x, double y)
	{
		vx = x;
		vy = y;
		objectcount(vobjectcount);
	}

	public void radius(double r)
	{
		vr = Math.abs(r);
		objectcount(vobjectcount);
	}

	public void objectcount(int v)
	{
		int i;
		double left, top, phi;
		double coef;
		Atom a[] = new Atom[5];
		MaxPatcher p = getParentPatcher();
	
		for (i = 0; i < vobjectcount; i++) {
			obarray[i].remove();
		}
	
		vobjectcount = Math.abs(v);
		if (vobjectcount > 256)
			vobjectcount = 256;
		obarray = new MaxBox[vobjectcount];

		if (vobjectcount > 0) {
			phi = 2.*Math.PI / vobjectcount;
			a[0] = Atom.newAtom("@bgcolor");
			for (i = 0; i < vobjectcount; i++) {
				left = vx + vr*Math.cos(i*phi);
				top = vy + vr*Math.sin(i*phi);
			
				// interpolate colors
				coef = (double)i / (double)(vobjectcount - 1);
				a[1] = Atom.newAtom((1. - coef) * colorA[0] + coef * colorB[0]);
				a[2] = Atom.newAtom((1. - coef) * colorA[1] + coef * colorB[1]);
				a[3] = Atom.newAtom((1. - coef) * colorA[2] + coef * colorB[2]);
				a[4] = Atom.newAtom((1. - coef) * colorA[3] + coef * colorB[3]);
				
				// need to make integers since the newobject method doesn't like floats
				left = Math.floor(left);
				top = Math.floor(top);
				
				obarray[i] = p.newDefault((int)left, (int)top, "button", a);
			}
			bang();	
		}
	}

	public void bang()
	{
		int i, j;
		MaxPatcher p = getParentPatcher();
		
		for (i = 0; i < (vobjectcount-1); i++) {
			j = (int)Math.floor(Math.random()*(vobjectcount-1-i)) + (i+1); 
			p.connect(obarray[i], 0, obarray[j], 0);
		}
	}
	
	public void color(double d[])
	{
		if (d.length == 4) {
			for (int i = 0; i < d.length; i++)
				colorA[i] = d[i];
		}
	}
	
	public void color2(double d[])
	{
		if (d.length == 4) {
			for (int i = 0; i < d.length; i++)
				colorB[i] = d[i];
		}
	}
}
