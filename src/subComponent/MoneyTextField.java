package subComponent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.swing.JFormattedTextField;

import connection.Sever;

public class MoneyTextField extends JFormattedTextField implements KeyListener {

	private static final long serialVersionUID = 1L;

	public MoneyTextField() {
		
		super(Sever.numberFormat);
		this.addKeyListener(this);
	}
	
	@Override
	public void keyReleased(KeyEvent evt) {
		
		String text = this.getText();
		if (Character.isDigit(evt.getKeyChar()) || ( evt.getKeyCode() == 8 && text.length() >= 1 )) {
				
			try {
				Number number = Sever.numberFormat.parse(text);
				this.setText(Sever.numberFormat.format(number));
				this.setCaretPosition(this.getText().length());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (Character.isAlphabetic(evt.getKeyChar())) {
		
			int pos = this.getCaretPosition();
			
			if (text.length() >= 1)
				text = text.substring(0, text.length() - 1);
			else
				text = "";
			this.setText(text);
			this.setCaretPosition(pos - 1);
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
