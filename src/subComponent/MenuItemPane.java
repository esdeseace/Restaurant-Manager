package subComponent;

import javax.swing.JPanel;

import connection.PanelListener;

public class MenuItemPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private PanelListener listener;

	public void setOnPanelOpenned(PanelListener listener) {
		this.listener = listener;
	}
	
	public void setOpenned() {
		if (this.listener != null)
			listener.onPanelOpenned();
	}
}
