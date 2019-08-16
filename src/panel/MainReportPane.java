package panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import subComponent.MenuItemPane;

public class MainReportPane extends MenuItemPane {

	private static final long serialVersionUID = 1L;

	private JButton button;

	private JPanel cards;

	private JPanel menuPane;
	private ArrayList<MenuItem> menuItems;

	private ReportPane billReportPane;
	private ReportPane couponReportPane;
	private ReportTablePane bonusPane;
	private ReportTablePane advanceMoneyPane;
	private ReportTablePane payWagePane;
	private ReportTablePane timeKeepingPane;

	public ReportTablePane getTimeKeepingPane() {
		return this.timeKeepingPane;
	}
	
	public ReportTablePane getPayWagePane() {
		return this.payWagePane;
	}

	public ReportTablePane getAdvanceMoneyPane() {
		return this.advanceMoneyPane;
	}
	
	public ReportTablePane getBonusPane() {
		return this.bonusPane;
	}
	
	public ReportPane getBillReportPane() {
		return this.billReportPane;
	}
	
	public ReportPane getCouponReportPane() {
		return this.couponReportPane;
	}
	
	public JButton getButton() {
		return this.button;
	}
	
	public MainReportPane() {
		
		setLayout(new BorderLayout(10, 10));
		menuItems = new ArrayList<>();
		
		cards = new JPanel();
		cards.setLayout(new CardLayout());
		this.add(cards, BorderLayout.CENTER);
		
		billReportPane = new ReportPane();
		billReportPane.setBillReport();
		cards.add(billReportPane, "Bill");
		
		couponReportPane = new ReportPane();
		couponReportPane.setSlipReport();
		cards.add(couponReportPane, "Coupon");
		
		bonusPane = new ReportTablePane("LỊCH SỬ THƯỞNG TIỀN");
		bonusPane.createLabel();
		cards.add(bonusPane, "Bonus");
		
		advanceMoneyPane = new ReportTablePane("LỊCH SỬ ỨNG TIỀN");
		advanceMoneyPane.createLabel();
		cards.add(advanceMoneyPane, "AdvanceMoney");
		
		payWagePane = new ReportTablePane("LỊCH SỬ ỨNG TIỀN");
		payWagePane.createLabel();
		cards.add(payWagePane, "PayWage");
		
		timeKeepingPane = new ReportTablePane("LỊCH SỬ CHẤM CÔNG");
		cards.add(timeKeepingPane, "TimeKeeping");
		
		menuPane = new JPanel();
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.Y_AXIS));
		menuPane.add(Box.createRigidArea(new Dimension(0, 350)));
		this.add(menuPane, BorderLayout.WEST);
		
		MenuItem firstItem = new MenuItem("Lịch sử hóa đơn", "Bill");
		firstItem.setOpenned();
		
		new MenuItem("Lịch sử nhập/xuất", "Coupon");
		new MenuItem("Lịch sử thưởng tiền", "Bonus");
		new MenuItem("Lịch sử ứng tiền", "AdvanceMoney");
		new MenuItem("Lịch sử trả lương", "PayWage");
		new MenuItem("Lịch sử chấm công", "TimeKeeping");
		
	}

	private class MenuItem extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		private boolean isOpen;
		private String key;
		
		public MenuItem(String text, String key) {
			
			this.isOpen = false;
			this.key = key;
			
			setPreferredSize(new Dimension(250, 70));
			setMaximumSize(new Dimension(250, 70));
			setMinimumSize(new Dimension(250, 70));
			setBackground(new Color(35, 46, 51));
			setLayout(new BorderLayout());
			
			JLabel label = new JLabel(text);
			label.setBorder(new EmptyBorder(0, 10, 0, 0));
			label.setFont(new Font("Calibri", Font.PLAIN, 20));
			label.setForeground(new Color(195, 210, 221));
			this.add(label, BorderLayout.CENTER);
			
			menuPane.add(this);
			menuItems.add(this);
			
			this.addMouseListener(new MouseAdapter() {
			
				@Override
				public void mouseClicked(MouseEvent e) {
					setOpenned();
				}
			});
		}
		
		public void setOpenned() {
			
			for (MenuItem menuItem : menuItems) {
				if (menuItem.isOpen) {
					menuItem.setUnOpenned();
					break;
				}
			}
			
			this.setBorder(new MatteBorder(0, 5, 0, 0, (Color) new Color(114, 137, 218)));
			this.setBackground(Color.black);
			this.isOpen = true;
			
			CardLayout cardLayout = (CardLayout) cards.getLayout();
			cardLayout.show(cards, key);
		}
		
		public void setUnOpenned() {
			this.setBorder(null);
			this.setBackground(new Color(35, 46, 51));
			this.isOpen = false;
		}
	}
	
	
}
