import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class ChromaJPasswordField extends JPasswordField {
	private ChromaHash ch = new ChromaHash();
	List<ChromaHash.Chroma> cl = new ArrayList<ChromaHash.Chroma>(); 
	
	public ChromaJPasswordField(String text) {
		setText(text);
		cl = ch.hashify(getPassword().toString());
		
		getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {	cl = ch.hashify(getPassword().toString());	}
			public void insertUpdate(DocumentEvent e) {	cl = ch.hashify(getPassword().toString());	}
			public void changedUpdate(DocumentEvent e) { cl = ch.hashify(getPassword().toString()); }
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		paintBars(g, 255);
		super.paintComponent(g);
		paintBars(g, 128);
	}
	
	private void paintBars(Graphics g, int opacity) {
		for (int i=0; i<cl.size(); i++) { 
			g.setColor(new Color(cl.get(i).r+128, cl.get(i).g+128, cl.get(i).b+128, opacity));
			g.fillRect(getWidth()-10*i-10-2, 0, 10, getHeight());
		}
	}
	    	
}