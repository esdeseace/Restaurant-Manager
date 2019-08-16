package connection;

import java.awt.Font;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import object.Staff;

public class Sever {

	public static Connection connection = null;
	public static Staff staff;
	
	public static final NumberFormat numberFormat = NumberFormat.getNumberInstance();
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
	public static final SimpleDateFormat fullTimeFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/YYYY");
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	public static final SimpleDateFormat fileFormat = new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss");
	
	public static final ImageIcon icon = new ImageIcon(Sever.class.getResource("/image/Restaurant.png"));
	
	public Sever() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String dbURL = "jdbc:ucanaccess://QLNH.accde";
			connection = DriverManager.getConnection(dbURL);
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Gặp vấn đề khi kết nối dữ liệu!", 
					"Lỗi", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		UIManager.put("OptionPane.messageFont", new Font("Calibri", Font.PLAIN, 20));
		UIManager.put("OptionPane.buttonFont", new Font("Calibri", Font.BOLD, 20));
	}
	
	public void checkTime() {
		
		try {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM NGAYTIEPTHEO");
			
			if (resultSet.next()) {
				
				Date date = resultSet.getDate(2);
				Calendar nextCalendar = Calendar.getInstance();
				nextCalendar.setTime(date);
				
				Calendar calendar = Calendar.getInstance();
				
				if (calendar.get(Calendar.YEAR) > nextCalendar.get(Calendar.YEAR)
						|| calendar.get(Calendar.DAY_OF_YEAR) >= nextCalendar.get(Calendar.DAY_OF_YEAR)) {
					
					PreparedStatement preStatement = connection.prepareStatement("UPDATE NGAYTIEPTHEO SET NGAY = ? WHERE IDNGAY = 1");
					calendar.add(Calendar.DATE, 1);
					date = new Date(calendar.getTimeInMillis());
					preStatement.setDate(1, date);
					preStatement.executeUpdate();
				
					preStatement = connection.prepareStatement("UPDATE NHANVIEN SET CHAMCONG = ?");
					preStatement.setBoolean(1, false);
					preStatement.executeUpdate();
					preStatement.close();
				}
			}
			resultSet.close();
			statement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String createMD5Password(String password) {
		
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] hashInBytes = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder stringBuilder = new StringBuilder();
			for (byte b : hashInBytes) {
				stringBuilder.append(String.format("%02x", b));
			}
			return stringBuilder.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static final String[] number = {"", "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"};
	
	public static String getStringFromNumber(long changeNumber) {
		
		if (changeNumber == 0)
			return "Không đồng.";
		
		String result = "";
		int b = 1000000000;
		int m = 1000000;
		int t = 1000;
		
		do {
			
			int value = (int) (changeNumber % b);
			String string = "";
			
			if (value != changeNumber)
				string += " tỷ ";
			changeNumber = changeNumber / b;
			
			if (value >= m) {
				int milion = value / m;
				value = value % m;
				string += readThreeDigit(milion) + " triệu ";
			}
			
			if (value >= t) {
				int thousand = value / 1000;
				string += readThreeDigit(thousand) + " nghìn ";
			}
			
			int unit = value % 1000;
			string += readThreeDigit(unit);
			result = string + result;
			
		} while (changeNumber > 0);
		
		result = result + " đồng.";
		result = result.replaceAll("  ", " ");
		return result.substring(0, 1).toUpperCase() + result.substring(1);
	}

	private static String readThreeDigit(int value) {
		
		String result = "";
		int hunderd = value / 100;
		result += (hunderd  != 0)? number[hunderd] + " trăm " : "";
		
		value = value % 100;
		int dozen = value / 10;
		int unit = value % 10;
		
		if (dozen  != 0) {
			if (dozen == 1)
				result += " mười ";		
			else
				result += number[dozen] + " mươi ";
			
			if (unit == 1 && dozen != 1)
				result += "mốt";
			else if (unit == 5) 
				result += "lăm";
			else
				result += number[unit];
		} else if (unit != 0 && hunderd != 0) 
			result += " linh " + number[unit];
		else
			result += number[unit];
		return result;
	}
	
}
