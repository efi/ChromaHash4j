import java.awt.Color;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.KeyRep;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ChromaTextField extends TextField {
	private ChromaHash ch = new ChromaHash();
	List<ChromaHash.Chroma> cl; 
	
	public ChromaTextField(String text) {
		setPasswordField(true);
		
		setText(text);
		cl = ch.hashify(getText());
	
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) { cl = ch.hashify(getText()); repaint(); }
		});
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent arg0) { cl = ch.hashify(getText()); repaint(); }
		});
	}
	
	private boolean passwordField;
	public boolean isPasswordField() { return passwordField; }
	public void setPasswordField(boolean passwordField) {
		this.passwordField = passwordField; setEchoChar('•');
		if (passwordField) setEchoChar('•');
		else setEchoChar((char)0);
	}
	
	@Override
	public void paint(Graphics g) {
		for (int i=0; i<cl.size(); i++) { 
			g.setColor(new Color(cl.get(i).r+128, cl.get(i).g+128, cl.get(i).b+128));
			g.fillRect(getWidth()-10*i-10-2, 0, 10, getHeight());
		}
	}
	    	
}