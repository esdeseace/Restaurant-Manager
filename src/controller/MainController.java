package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import connection.PanelListener;
import connection.Sever;
import dialog.PasswordDialog;
import model.BillModel;
import model.CouponModel;
import model.DinnerTableModel;
import model.FoodModel;
import model.StaffModel;
import panel.LoginPane;
import panel.MainPane;
import panel.MainReportPane;

public class MainController {
	
	private MainPane mainPane;
	private JFrame frame;
	private JPanel contentPane;
	
	public MainController(MainPane mainPane, JFrame frame, JPanel contentPane) {
		this.mainPane = mainPane;
		this.frame = frame;
		this.contentPane = contentPane;
	}
	
	public void setEvent() {
		
		FoodModel foodModel = new FoodModel();
		foodModel.connectDatabase();
		
		DinnerTableModel dinnerTableModel = new DinnerTableModel();
		dinnerTableModel.connectDatabase();
		
		StaffModel staffModel = new StaffModel();
		staffModel.connectDatabase();
		
		CouponModel couponModel = new CouponModel(foodModel);
		couponModel.connectDatabase();
		
		BillModel billModel = new BillModel(foodModel);
		billModel.connectDatabase();
		
		
		FoodController foodController = new FoodController(mainPane.getFoodPane(), foodModel);
		if (mainPane.getFoodPane() != null)
			foodController.setViewAndEvent();
		
		ResourceController resourceController = new ResourceController(mainPane.getResourcePane(), foodModel);
		if (mainPane.getResourcePane() != null)
			resourceController.setViewAndEvent();
		
		StaffController staffController = new StaffController(mainPane.getStaffPane(), staffModel);
		if (mainPane.getStaffPane() != null)
			staffController.setViewAndEvent();
		
		StorgeController storgeController = new StorgeController(mainPane.getStorgePane(), foodModel, couponModel);
		if (mainPane.getStorgePane() != null)
			storgeController.setViewAndEvent();
		
		BillController billController = new BillController(mainPane.getBillPane(), billModel, foodController);
		if (mainPane.getBillPane() != null)
			billController.setViewAndEvent();
		
		TableController tableController = new TableController(mainPane.getTablePane(), dinnerTableModel, billController);
		if (mainPane.getTablePane() != null)
			tableController.setViewAndEvent();
		
		StatisticController statisticController = new StatisticController(mainPane.getStatisticPane(), billModel, couponModel);
		if (mainPane.getStatisticPane() != null)
			statisticController.setViewAndEvent();
		
		MainReportPane mainReportPane = mainPane.getMainReportPane();
		if (mainReportPane != null) {
			BillReportController billReportController = new BillReportController(mainReportPane.getBillReportPane(), billModel);
			billReportController.setViewAndEvent();
			
			CouponReportController couponReportController = new CouponReportController(mainReportPane.getCouponReportPane(), couponModel);
			couponReportController.setViewAndEvent();
		
			ReportTableController reportTableController = new ReportTableController();
			reportTableController.updateTable(mainReportPane.getBonusPane(), "TIENTHUONG");
			reportTableController.updateTable(mainReportPane.getAdvanceMoneyPane(), "TIENUNG");
			
			PayWageController payWageController = new PayWageController(mainReportPane.getPayWagePane());
			payWageController.updateTable();
			
			TimeKeepingController timeKeepingController = new TimeKeepingController(mainReportPane.getTimeKeepingPane());
			timeKeepingController.updateTable();
			
			mainReportPane.setOnPanelOpenned(new PanelListener() {
				
				@Override
				public void onPanelOpenned() {
					billReportController.updateTable();
					couponReportController.updateTable();
					reportTableController.updateTable(mainReportPane.getBonusPane(), "TIENTHUONG");
					reportTableController.updateTable(mainReportPane.getAdvanceMoneyPane(), "TIENUNG");
					payWageController.updateTable();
					timeKeepingController.updateTable();
				}
			});
		}
		
		mainPane.getLogout().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				frame.setSize(600, 550);
				frame.setLocationRelativeTo(null);
				
				Sever.staff = null;
				contentPane.removeAll();
				
				LoginPane loginPane = new LoginPane();
				contentPane.add(loginPane, BorderLayout.CENTER);
				
				LoginController loginController = new LoginController(loginPane);
				loginController.setEvent(frame, contentPane);
				
				contentPane.revalidate();
				contentPane.repaint();
			}
		});
		
		mainPane.getChangePassword().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				PasswordDialog passwordDialog = new PasswordDialog(staffModel);
				passwordDialog.setModal(true);
				passwordDialog.setVisible(true);
			}
		});
		
	}
}
