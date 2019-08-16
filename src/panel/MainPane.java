package panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import connection.Sever;
import subComponent.MenuItemPane;

public class MainPane extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<MenuItem> menuItems;
	private JPanel cards;
	private JPanel menuPane;
	
	private FoodPane foodPane;
	private ResourcePane resourcePane;
	private StorgePane storgePane;
	private StaffPane staffPane;
	private TablePane tablePane;
	private BillPane billPane;
	private MainReportPane mainReportPane;
	private StatisticPane statisticPane;

	private JMenuItem logout;
	private JMenuItem changePassword;
	
	public JMenuItem getLogout() {
		return this.logout;
	}
	
	public JMenuItem getChangePassword() {
		return this.changePassword;
	}
	
	public StatisticPane getStatisticPane() {
		return this.statisticPane;
	}
	
	public MainReportPane getMainReportPane() {
		return this.mainReportPane;
	}
	
	public BillPane getBillPane() {
		return this.billPane;
	}
	
	public TablePane getTablePane() {
		return this.tablePane;
	}
	
	public StorgePane getStorgePane() {
		return storgePane;
	}
	
	public StaffPane getStaffPane() {
		return this.staffPane;
	}
	
	public ResourcePane getResourcePane() {
		return this.resourcePane;
	}
	
	public FoodPane getFoodPane() {
		return this.foodPane;
	}

	public MainPane() {
		
		this.setLayout(new BorderLayout(10, 10));
		menuItems = new ArrayList<>();
		
		final ImageIcon tableIcon = new ImageIcon(MainPane.class.getResource("/image/table.png"));
		final ImageIcon foodIcon = new ImageIcon(MainPane.class.getResource("/image/food.png"));
		final ImageIcon resourceIcon = new ImageIcon(MainPane.class.getResource("/image/resource.png"));
		final ImageIcon storgeIcon = new ImageIcon(MainPane.class.getResource("/image/storge.png"));
		final ImageIcon staffIcon = new ImageIcon(MainPane.class.getResource("/image/staff.png"));
		final ImageIcon reportIcon = new ImageIcon(MainPane.class.getResource("/image/report.png"));
		final ImageIcon statisticIcon = new ImageIcon(MainPane.class.getResource("/image/statistics.png"));
		final ImageIcon detailIcon = new ImageIcon(MainPane.class.getResource("/image/down.png"));
		final ImageIcon passwordIcon = new ImageIcon(MainPane.class.getResource("/image/password.png"));
		final ImageIcon logoutIcon = new ImageIcon(MainPane.class.getResource("/image/logout.png"));
		
		cards = new JPanel();
		cards.setLayout(new CardLayout());
		cards.setBorder(new EmptyBorder(20, 0, 10, 20));
		this.add(cards, BorderLayout.CENTER);
		
		menuPane = new JPanel();
		menuPane.setBorder(new EmptyBorder(20, 0, 0, 0));
		menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.Y_AXIS));
		menuPane.setBackground(new Color(35, 46, 51));
		this.add(menuPane, BorderLayout.WEST);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBorder(new LineBorder(new Color(114, 137, 218), 2));
		panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.setLayout(new BorderLayout());
		menuPane.add(panel);

		panel.setPreferredSize(new Dimension(200, 200));
		panel.setMaximumSize(new Dimension(200, 200));
		panel.setMinimumSize(new Dimension(200, 200));
		
		JLabel label = new JLabel();
		label.setIcon(Sever.icon);
		label.setHorizontalAlignment(JLabel.CENTER);
		panel.add(label, BorderLayout.CENTER);
		
		JPanel userPanel = new JPanel();
		userPanel.setOpaque(false);
		userPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		panel.add(userPanel, BorderLayout.SOUTH);
		
		JLabel usernameLabel = new JLabel(Sever.staff.getUsername());
		usernameLabel.setForeground(Color.RED);
		usernameLabel.setVerticalAlignment(JLabel.CENTER);
		usernameLabel.setFont(new Font("Calibri", Font. BOLD, 25));
		userPanel.add(usernameLabel);
		
		JPopupMenu popup = new JPopupMenu();
		popup.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		changePassword = new JMenuItem("Đổi mật khẩu", passwordIcon);
		changePassword.setOpaque(true);
		changePassword.setBackground(Color.BLACK);
		changePassword.setForeground(Color.WHITE);
		changePassword.setVerticalAlignment(JMenuItem.CENTER);
		changePassword.setFont(new Font("Calibri", Font.PLAIN, 15));
		popup.add(changePassword);
		
		logout = new JMenuItem("Đăng xuất", logoutIcon);
		logout.setOpaque(true);
		logout.setBackground(Color.BLACK);
		logout.setForeground(Color.WHITE);
		logout.setVerticalAlignment(JMenuItem.CENTER);
		logout.setFont(new Font("Calibri", Font.PLAIN, 15));
		popup.add(logout);
		
		JLabel detail = new JLabel();
		detail.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				popup.show(detail, 0, detail.getHeight());
			}
		});
		detail.setIcon(detailIcon);
		userPanel.add(detail);
		
		menuPane.add(Box.createRigidArea(new Dimension(0, 20)));
		
		if (Sever.staff.isTable) {
			MenuItemPane payPane = new MenuItemPane();
			payPane.setLayout(new GridLayout());
			cards.add(payPane, "Pay");
			
			tablePane = new TablePane();
			payPane.add(tablePane);
			
			billPane = new BillPane();
			billPane.createSouthPanel();
			payPane.add(billPane);
		
			new MenuItem("Bàn ăn", "Pay", tableIcon, payPane);
		}
		

		if (Sever.staff.isStorge) {
			storgePane = new StorgePane();
			cards.add(storgePane, "Storge");
			new MenuItem("Kho", "Storge", storgeIcon, storgePane);
		}
		
		if (Sever.staff.isFood) {
			foodPane = new FoodPane();
			foodPane.createMenu();
			cards.add(foodPane, "Food");
			new MenuItem("Món ăn", "Food", foodIcon, foodPane);
		}
		
		if (Sever.staff.isResource) {
			resourcePane = new ResourcePane();
			cards.add(resourcePane, "Resource");
			new MenuItem("Thực phẩm", "Resource", resourceIcon, resourcePane);
		}
		
		if (Sever.staff.isStaff) {
			staffPane = new StaffPane();
			cards.add(staffPane, "Staff");
			new MenuItem("Nhân viên", "Staff", staffIcon, staffPane);
		}
		
		if (Sever.staff.isReport) {
			mainReportPane = new MainReportPane();
			cards.add(mainReportPane, "Report");
			new MenuItem("Báo cáo", "Report", reportIcon, mainReportPane);
		}
		
		if (Sever.staff.isStatistic) {
			statisticPane = new StatisticPane();
			cards.add(statisticPane, "Statistic");
			new MenuItem("Thống kê", "Statistic", statisticIcon, statisticPane);
		}
		
	}
	
	private class MenuItem extends JPanel {
		
		private static final long serialVersionUID = 1L;
		
		private boolean isOpen;
		private String key;
		private MenuItemPane panel;
		
		public MenuItem(String text, String key, ImageIcon icon, MenuItemPane panel) {
			
			this.panel = panel;
			this.isOpen = false;
			this.key = key;
			
			setPreferredSize(new Dimension(250, 70));
			setMaximumSize(new Dimension(250, 70));
			setMinimumSize(new Dimension(250, 70));
			
			setBackground(new Color(35, 46, 51));
			setLayout(new BorderLayout());
			
			JLabel label = new JLabel(text);
			label.setIcon(icon);
			label.setVerticalAlignment(JLabel.CENTER);
			label.setBorder(new EmptyBorder(0, 10, 0, 0));
			label.setFont(new Font("Calibri", Font.PLAIN, 25));
			label.setForeground(new Color(195, 210, 221));
			this.add(label, BorderLayout.CENTER);
			
			menuPane.add(this);
			menuItems.add(this);
			
			this.addMouseListener(new MouseAdapter() {
			
				@Override
				public void mouseReleased(MouseEvent e) {
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
			panel.setOpenned();
		}
		
		public void setUnOpenned() {
			this.setBorder(null);
			this.setBackground(new Color(35, 46, 51));
			this.isOpen = false;
		}
	}
}
