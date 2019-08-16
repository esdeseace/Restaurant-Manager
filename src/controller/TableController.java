package controller;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import dialog.TableDialog;
import model.DinnerTableModel;
import object.DinnerTable;
import panel.TablePane;

public class TableController {

	private final ImageIcon icon = new ImageIcon(TableController.class.getResource("/image/floor.png"));
	private final ImageIcon tableIcon = new ImageIcon(TableController.class.getResource("/image/aTable.png"));
	
	private HashMap<Integer, JPanel> floors;
	private ArrayList<TableButton> tableButtons;
	
	private JTabbedPane tabbedPane;
	private DinnerTable selectedTable;
	
	private TablePane tablePane;
	private DinnerTableModel dinnerTableModel;
	private TableButton selectedButton;
	private BillController billController;
	
	private int index = 0;
	
	public TableController(TablePane tablePane, DinnerTableModel dinnerTableModel, BillController billController) {
		this.dinnerTableModel = dinnerTableModel;
		this.tablePane = tablePane;
		this.billController = billController;
	}
	
	public DinnerTableModel getDinnerTableModel() {
		return this.dinnerTableModel;
	}
	
	public void createDialog() {
		TableDialog tableDialog = new TableDialog(this);
		tableDialog.setModal(true);
		tableDialog.setVisible(true);
	}
	
	public void addFloorPane(DinnerTable table) {
		
		Integer key = new Integer(table.getFloor());
		JPanel floor = floors.get(key);
		
		if (floor == null) {
			
			floor = new JPanel();
			floor.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
			floors.put(key, floor);
			
			JLabel label = new JLabel("Tầng " + key);
			label.setFont(new Font("Calibri", Font.PLAIN, 20));
			label.setVerticalAlignment(JLabel.CENTER);
			label.setIcon(icon);

			tabbedPane.addTab(null, floor);
			tabbedPane.setTabComponentAt(index++, label);
		}
		
		TableButton button = new TableButton(table);
		tableButtons.add(button);
		floor.add(button);
	}
	
	public void setViewAndEvent() {
		
		ArrayList<DinnerTable> tables = dinnerTableModel.getDinnerTables();
		this.tabbedPane = tablePane.getTabbedPane();
		tableButtons = new ArrayList<>();
		floors = new HashMap<>();
		
		for (DinnerTable table : tables)
			this.addFloorPane(table);
		
		tablePane.getAddButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				createDialog();
				tablePane.revalidate();
				tablePane.repaint();
			}
		});
		
		tablePane.getDeleteButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				if (selectedTable != null) {
					
					if (selectedTable.getIdOfBill() == 0) {
						
						int isDelete = JOptionPane.showConfirmDialog(null, 
								"Việc này không thể hoàn tác. Bạn có chắc muốn xóa không?!", 
								"Xóa bàn", JOptionPane.YES_NO_OPTION);
						
						if (isDelete == 0) {
						
							JPanel floor = floors.get(selectedTable.getFloor());
							floor.remove(selectedButton);
							tableButtons.remove(selectedButton);
							floor.revalidate();
							floor.repaint();
							
							int floorNum = selectedTable.getFloor();
							dinnerTableModel.deleteDinnerTable(selectedTable.getId());
						
							if (!dinnerTableModel.isHaveFloor(floorNum)) {
								tabbedPane.remove(floor);
								tabbedPane.repaint();
							}
							selectedTable = null;
						}
					} else {
						JOptionPane.showMessageDialog(null, "Bàn chưa thanh toán, không thể xóa!", 
								"Lỗi", JOptionPane.WARNING_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Chưa chọn bàn để xóa!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}
	
	public class TableButton extends JLabel {

		private static final long serialVersionUID = 1L;
		
		private boolean isOpened = false;
	
		public TableButton(DinnerTable dinnerTable) {
			
			setText(dinnerTable.getName());
			setFont(new Font("Calibri", Font.PLAIN, 25));
			setIcon(tableIcon);
			setHorizontalTextPosition(JLabel.CENTER);
			setVerticalTextPosition(JLabel.BOTTOM);
			
			this.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent arg0) {
					
					for (TableButton tableButton : tableButtons) {
						if (tableButton.isOpened == true) {
							tableButton.setUnOpenned();
							break;
						}
					}
					
					selectedTable = dinnerTable;
					billController.setBillPane(dinnerTable);
					setOpenned();
				}
			});
		}
		
		public void setOpenned() {
			
			selectedButton = this;
			isOpened = true;
			setBorder(new LineBorder(Color.BLACK, 2));
		}
		
		public void setUnOpenned() {
			isOpened = false;
			setBorder(null);
		}
	}
}