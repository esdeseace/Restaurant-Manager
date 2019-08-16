package subComponent;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class ButtonTable {
	
	private List<String> buttonNames;
	private List<Action> actions;
	
	public ButtonTable(List<String> buttonNames, List<Action> actions) {
		this.buttonNames = buttonNames;
		this.actions = actions;
	}
	
	public ButtonsRenderer getButtonsRenderer() {
		return new ButtonsRenderer();
	}
	
	public ButtonEditor getButtonEditor(JTable table) {
		return new ButtonEditor(table);
	}
	
	private class ButtonsRenderer implements TableCellRenderer {

		private final ButtonPane panel = new ButtonPane();
	
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
			return panel;
		}
	}
	
	private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
		
		private static final long serialVersionUID = 1L;
		
		protected final ButtonPane panel = new ButtonPane();
		protected final JTable table;

		private class EditingStopHandler extends MouseAdapter implements ActionListener {
			
			@Override 
			public void mousePressed(MouseEvent e) {
				Object o = e.getSource();
				if (o instanceof TableCellEditor) {
					actionPerformed(null);
				} else if (o instanceof JButton) {
					ButtonModel m = ((JButton) e.getComponent()).getModel();
					if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
						panel.setBackground(table.getBackground());
					}
				}
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(() -> fireEditingStopped());
			}
		}
		
		protected ButtonEditor(JTable table) {
		
			this.table = table;
			
			EditingStopHandler handler = new EditingStopHandler();
			for (int index = 0; index < panel.buttons.size(); index++) {
				JButton button = panel.buttons.get(index);
				button.addMouseListener(handler);
				button.addActionListener(handler);
				button.setAction(actions.get(index));
			}
			panel.addMouseListener(handler);
		}

		@Override
		public Component getTableCellEditorComponent(JTable tbl, Object value, boolean isSelected, int row, int column) {
			panel.setBackground(tbl.getSelectionBackground());
			return panel;
		}
		
		@Override
		public Object getCellEditorValue() {
			return "";
		}

	}
	
	private class ButtonPane extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public final List<JButton> buttons = new ArrayList<>();
		
		public ButtonPane() {
			
			setOpaque(true);
			setLayout(new GridLayout(0, buttonNames.size(), 5, 0));
			setBorder(new EmptyBorder(5, 5, 5, 5));
			
			for (String name : buttonNames) {
				JButton button = new JButton(name);
				button.setFont(new Font("Calibri", Font.BOLD, 17));
				button.setFocusable(false);
				button.setRolloverEnabled(false);
				buttons.add(button);
				add(button);
			}
		}
	}
}
