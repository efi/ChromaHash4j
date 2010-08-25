import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;


/**
 * Class for turning a String into a ChromasHash representation
 * Color value compatible to the implementation from
 * http://mattt.github.com/Chroma-Hash/ revision "November 6, 2009" 
 * @author Thomas Efer (mail@thomasefer.de)
 *
 */
public class ChromaHash {

	/**
	 * Objects representing A color, without binding to existing java color representations
	 * @author Thomas Efer (mail@thomasefer.de)
	 *
	 */
	public static class Chroma {
		public Chroma (byte r, byte g, byte b) { this.r = r; this.g = g; this.b = b; }
		public Chroma (String cssColor) {
			cssColor = cssColor.replaceFirst("#", "");
			if (cssColor.length()==6) {
				r=(byte)(Integer.parseInt(cssColor.substring(0,2),16)-128);
				g=(byte)(Integer.parseInt(cssColor.substring(2,4),16)-128);
				b=(byte)(Integer.parseInt(cssColor.substring(4,6),16)-128);
			}
			else {
				r=(byte)((Integer.parseInt(cssColor.substring(0,1)+"0",16))-128);
				g=(byte)((Integer.parseInt(cssColor.substring(1,2)+"0",16))-128);
				b=(byte)((Integer.parseInt(cssColor.substring(2,3)+"0",16))-128);
			}
		}
		public byte r=0;
		public byte g=0;
		public byte b=0;
		public String cssValue() { return "#"+new Formatter().format("%02x%02x%02x",128+r,128+g,128+b); }
	}
	
	private int bars = 3;
	private String salt = "7be82b35cb0199120eea35a4507c9acf";
    private int minimum = 6;
    
    private String[] colors;
    
    MessageDigest md5 = null; 
    
    public ChromaHash() { init(null, null, null); }
    public ChromaHash(Integer bars, String salt, Integer minimum) { init(bars, salt, minimum); }
    
    private void init(Integer bars, String salt, Integer minimum) {
    	if ( bars != null && bars > 0 && bars < 5 ) this.bars = bars;
    	if ( salt != null ) this.salt = salt;
    	if ( minimum != null ) this.minimum = minimum;
    	
    	try { md5 = MessageDigest.getInstance("MD5"); } catch( NoSuchAlgorithmException nsaex ) {}
    }
    
    /**
     * Turns a String into a ChromasHash representation
     * Color value compatible to the implementation from
     * http://mattt.github.com/Chroma-Hash/ revision "November 6, 2009" 
     * @param input the String
     * @return a List of color representing Chroma objects (outermost color bar first)
     */
    public List<Chroma> hashify(String input) {
    	// MD5 Hashing
    	md5.reset();
    	md5.update((input+":"+salt).getBytes(Charset.forName("UTF-8")));
    	byte[] result = md5.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<result.length; i++) {
        	String newByteFull = "00"+Integer.toHexString(0xFF & result[i]); 
        	hexString.append( newByteFull.substring(newByteFull.length()-2) );
        }
    	String md5hash = hexString.toString();
    	
    	colors = new String[] { md5hash.substring(0, 6), md5hash.substring(6, 12), md5hash.substring(12, 18), md5hash.substring(18, 24), md5hash.substring(24, 30) };
  	
    	ArrayList<Chroma> output = new ArrayList<Chroma>();
    	
    	for (int i=0; i< bars; i++) {
    		Integer hashpart = Integer.parseInt(colors[i],16);		
	    	if (input.length() < minimum ) {
	    		byte g = Long.valueOf((hashpart%15)*16-128).byteValue();
	    		output.add(new Chroma(g,g,g));
	    	}
	    	else {	    		
	    		byte r = Integer.valueOf((   ((hashpart>>16)&255)>>4  )*16-128).byteValue();
	    		byte g = Integer.valueOf((   ((hashpart>>8)&255)>>4  )*16-128).byteValue();
	    		byte b = Integer.valueOf((   (hashpart&255)>>4  )*16-128).byteValue();
	    		
	    		// In the original JavaScript implemetation, rgb is combined by a simple join of hex representations
	    		// Considering that colors[i]=="de04c2" results in r=="d0", g=="0", b=="c0"
	    		// the resulting color is "#d00c0" instead of "#d000co".
	    		// This triggers the browser's short color notation mode, croping the color to "#d00"
	    		// So we can sadly not simply write:
	    		//    output.add(new Chroma(r,g,b));
	    		// Instead we need the css style constructor
	    		output.add(new Chroma("#"+Integer.toHexString(r+128)+Integer.toHexString(g+128)+Integer.toHexString(b+128)));
	    	}   	
    	}

    	return output;
    }
	
	
}
