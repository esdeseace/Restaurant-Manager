package controller;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import connection.PanelListener;
import connection.Sever;
import dialog.MoneyDialog;
import dialog.PayWageDialog;
import dialog.StaffDialog;
import model.StaffModel;
import object.Staff;
import panel.StaffPane;
import subComponent.ButtonTable;

public class StaffController {

	private StaffPane staffPane;
	private StaffModel staffModel;
	private StaffTable staffTable;
	
	private Staff selectedStaff;

	public StaffController(StaffPane staffPane, StaffModel staffModel) {
		this.staffModel = staffModel;
		this.staffPane = staffPane;
	}

	public StaffModel getStaffModel() {
		return this.staffModel;
	}
	
	public Staff getSelectedStaff() {
		return this.selectedStaff;
	}
	
	public void setViewAndEvent() {

		this.staffTable = new StaffTable();
		staffPane.getMainPane().add(new JScrollPane(staffTable), BorderLayout.CENTER);
		
		updateMainPane();
		setAction();
	}
	
	public void updateMainPane() {
		
		staffTable.deleteAllRow();
		ArrayList<Staff> staffs = staffModel.getStaffs();
		for (Staff staff : staffs) {
			
			String name = staff.getName();
			String phoneNumber = staff.getPhoneNumber();
			String position = staff.getPosition();
			String bonus = Sever.numberFormat.format( staff.getBonus() );
			String advanceMoney = Sever.numberFormat.format( staff.getAdvanceMoney() );
			int timekeeping = staff.getTimekeeping();
			
			Object[] row = {staffTable.getRowCount() + 1, staff, name, phoneNumber, 
					position, bonus, advanceMoney, timekeeping};
			staffTable.addRow(row);
		}
	}
	
	private void setAction() {
		
		staffPane.getDeleteButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int[] selectedRows = staffTable.getSelectedRows();
				
				if (selectedRows.length > 0) {
					
					int isDelete = JOptionPane.showConfirmDialog(null, 
							"Việc này không thể hoàn tác. Bạn có chắc muốn xóa không?!", 
							"Xóa tài khoản", JOptionPane.YES_NO_OPTION);
					
					if (isDelete == 0) {
					
						for (int index = selectedRows.length - 1; index >= 0; index--) {
						
							DefaultTableModel tableModel = (DefaultTableModel) staffTable.getModel();
							Staff staff = (Staff) tableModel.getValueAt(selectedRows[index], 1);
						
							if (staff.getUsername().equals(Sever.staff.getUsername())) {
								JOptionPane.showMessageDialog(null, "Bạn không thể xóa tài khoản của chính bạn!", 
										"Lỗi", JOptionPane.WARNING_MESSAGE);
							} else {
								staffModel.deleteStaff(staff);
								tableModel.removeRow(selectedRows[index]);
							}
						}
					}
				} else 
					JOptionPane.showMessageDialog(null, "Bạn chưa chọn tài khoản để xóa!", 
							"Lỗi", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		staffPane.setOnPanelOpenned(new PanelListener() {
			
			@Override
			public void onPanelOpenned() {
				updateMainPane();
			}
		});

		staffPane.getAddButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Sever.staff.isAddStaff)
					createStaffDialog(false);
				else
					JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền thêm tài khoản! Liên hệ administrator để cấp quyền.", 
							"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	private void createStaffDialog(boolean isEdit) {
		StaffDialog staffDialog = new StaffDialog(this, isEdit);
		staffDialog.setModal(true);
		staffDialog.setVisible(true);
	}
	
	private void createMoneyDialog(boolean isBonus) {
		MoneyDialog moneyDialog = new MoneyDialog(this, isBonus);
		moneyDialog.setModal(true);
		moneyDialog.setVisible(true);
	}
	
	private void createPayWageDialog() {
		PayWageDialog payWageDialog = new PayWageDialog(this);
		payWageDialog.setModal(true);
		payWageDialog.setVisible(true);
	}
	
	private class EditEvent extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final JTable table;

		private EditEvent(JTable table) {
			this.table = table;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (Sever.staff.isEditStaff) {
				int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
				selectedStaff = (Staff) table.getModel().getValueAt(selectedRow, 1);
				createStaffDialog(true);
			} else
				JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền sửa tài khoản nhân viên! Liên hệ administrator để cấp quyền.", 
						"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private class PayWageEvent extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final JTable table;

		private PayWageEvent(JTable table) {
			this.table = table;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (Sever.staff.isPay) {
				int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
				selectedStaff = (Staff) table.getModel().getValueAt(selectedRow, 1);
				createPayWageDialog();
			} else 
				JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền trả lương cho nhân viên! Liên hệ administrator để cấp quyền.", 
						"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private class BonusEvent extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final JTable table;

		private BonusEvent(JTable table) {
			this.table = table;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if (Sever.staff.isBonus) {
				int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
				selectedStaff = (Staff) table.getModel().getValueAt(selectedRow, 1);
				createMoneyDialog(true);
			} else 
				JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền thưởng tiền cho nhân viên! Liên hệ administrator để cấp quyền.", 
						"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private class AdvanceMoneyEvent extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final JTable table;

		private AdvanceMoneyEvent(JTable table) {
			this.table = table;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (Sever.staff.isAdvanceMoney) {
				int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
				selectedStaff = (Staff) table.getModel().getValueAt(selectedRow, 1);
				createMoneyDialog(false);
			} else 
				JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền ứng lương cho nhân viên! Liên hệ administrator để cấp quyền.", 
						"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private class TimekeepingEvent extends AbstractAction {

		private static final long serialVersionUID = 1L;

		private final JTable table;

		private TimekeepingEvent(JTable table) {
			this.table = table;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (Sever.staff.isTime) {
				int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
				selectedStaff = (Staff) table.getModel().getValueAt(selectedRow, 1);
			
				if (!selectedStaff.isTimekeeping()) {
					staffModel.timekeeping(selectedStaff);
					JOptionPane.showMessageDialog(null, "Đã chấm công xong!",
							"Thành công", JOptionPane.INFORMATION_MESSAGE);
					updateMainPane();
				} else  {
					JOptionPane.showMessageDialog(null, "Nhân viên này đã chấm công trong hôm nay!",
							"Lỗi", JOptionPane.WARNING_MESSAGE);
				}
			} else
				JOptionPane.showMessageDialog(null, "Có vẻ như bạn không có quyền chấm công cho nhân viên! Liên hệ administrator để cấp quyền.", 
						"Thông cáo", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private class StaffTable extends JTable {
		
		private static final long serialVersionUID = 1L;
		
		private DefaultTableModel defaultTableModel;
		
		public StaffTable() {
			
			final String[] titles = {"STT", "Tài khoản", "Họ tên", "Số điện thoại", "Chức vụ",
					"Tiền thưởng", "Tiền ứng", "Số công", "", ""};
			
			defaultTableModel = new DefaultTableModel(null, titles) {
				
				private static final long serialVersionUID = 1L;

				@Override
				public Class<?> getColumnClass(int column) {
					for (int row = 0; row < getRowCount(); row++)
						if (getValueAt(row, column) != null)
							return getValueAt(row, column).getClass();
					return String.class;
				}
			};
			
			this.setModel(defaultTableModel);
			this.setFont(new Font("Calibri", Font.PLAIN, 20));
			this.setRowHeight(40);
			this.setAutoCreateRowSorter(true);
			
			JTableHeader header = this.getTableHeader();
			header.setReorderingAllowed(false);
			TableCellRenderer rendererFromHeader = header.getDefaultRenderer();
			
			JLabel headerLabel = (JLabel) rendererFromHeader;
			headerLabel.setHorizontalAlignment(JLabel.CENTER);
			header.setFont(new Font("Calibri", Font.BOLD, 20));
			
			this.addButtonToTable(Arrays.asList("Thưởng", "Ứng", "Trả lương", "Chấm công"), 
					Arrays.asList(new BonusEvent(this), new AdvanceMoneyEvent(this), 
							new PayWageEvent(this), new TimekeepingEvent(this)), 8);
			this.addButtonToTable(Arrays.asList("Sửa"), Arrays.asList(new EditEvent(this)), 9);
			
			int[] positions = {0, 1, 3, 4, 5, 6, 7, 8, 9};
			int[] widths = {50, 150, 130, 120, 120, 120, 80, 470, 80};
			setWidth(positions, widths);
		}
		
		public void deleteAllRow() {
			for (int index = defaultTableModel.getRowCount() - 1; index >= 0; index--) {
			    defaultTableModel.removeRow(index);
			}
		}
		
		private void addButtonToTable(List<String> strings, List<Action> actions, int position) {
			
			ButtonTable buttonTable = new ButtonTable(strings, actions);
			TableColumn column = columnModel.getColumn(position);
			column.setCellRenderer(buttonTable.getButtonsRenderer());
			column.setCellEditor(buttonTable.getButtonEditor(this));
		}
		
		private void setWidth(int[] positions, int[] widths) {
			
			TableColumn column;
			for (int index = 0; index < positions.length; index++) {
				column = columnModel.getColumn(positions[index]);
				column.setMinWidth(widths[index]);
				column.setMaxWidth(widths[index]);
				column.setPreferredWidth(widths[index]);
			}
		}
		
		public void addRow(Object[] rowData) {
			defaultTableModel.addRow(rowData);
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {                
			if (column == 8 || column == 9)
				return true;
			return false;
		}
		
		@Override
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int col) {
			Component comp = super.prepareRenderer(renderer, row, col);
			if (col != 8 && col != 9) {
				if (col != 1 && col != 2 && col != 3 && col != 4) {
					((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
				} else {
					((JLabel) comp).setHorizontalAlignment(JLabel.LEFT);
					((JLabel) comp).setBorder(new EmptyBorder(0, 10, 0, 0));
				}
			}
			return comp;
		}
	}
	
}
