package controller;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import connection.Sever;
import dialog.DayDialog;
import dialog.ReportDialog;
import model.BillModel;
import model.CouponModel;
import object.Bill;
import object.Coupon;
import object.DetailBill;
import object.DetailCoupon;
import object.Food;
import object.FullStatistic;
import panel.StatisticPane;

public class StatisticController {
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
	private final SimpleDateFormat fullTimeFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/YYYY");
	private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

	private final ImageIcon barChartIcon = new ImageIcon(StatisticController.class.getResource("/image/bar_chart_30px.png"));
	private final ImageIcon lineChartIcon = new ImageIcon(StatisticController.class.getResource("/image/Line Chart_30px.png"));
	private final ImageIcon pieChartIcon = new ImageIcon(StatisticController.class.getResource("/image/chart_30px.png"));
	
	private final Font TABLE_FONT;
	private final Font TITLE_FONT;
	private final Font BANNER_FONT;
	private final Font HEADER_FONT;
	
	private StatisticPane statisticPane;
	private BillModel billModel;
	private CouponModel couponModel;
	private JPanel center;
	private Date fromDate;
	
	private String key;
	private String banner;
	
	private Calendar toCalendar;
	private Connection connection;
	
	private ArrayList<FullStatistic> fullStatistics;
	private JTabbedPane leftCenter;
	private JTabbedPane rightCenter;
	private FullStatistic full;
	
	public StatisticController(StatisticPane statisticPane, BillModel billModel, CouponModel couponModel) {
		
		this.statisticPane = statisticPane;
		this.billModel = billModel;
		this.couponModel = couponModel;
		this.connection = Sever.connection;

		BaseFont baseFont = null;
		try {
			baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\Calibri.ttf", BaseFont.IDENTITY_H, true);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Có lỗi khi load font Calibri! Hãy tải font để sử dụng phần mềm!", 
					"Lỗi", JOptionPane.WARNING_MESSAGE);
			System.exit(0);
		}
		
		TABLE_FONT = new Font(baseFont, 11f);
		HEADER_FONT = new Font(baseFont, 11f, Font.BOLD, BaseColor.DARK_GRAY);
		TITLE_FONT = new Font(baseFont, 28f,  Font.BOLD, BaseColor.RED);
		BANNER_FONT = new Font(baseFont, 18f,  Font.NORMAL, BaseColor.RED);
		
	}

	public void setView(Date fromDate, Date toDate, String key) {

		leftCenter.removeAll();
		rightCenter.removeAll();
		
		this.fromDate = fromDate;
		this.key = key;
		
		toCalendar = Calendar.getInstance();
		toCalendar.setTime(toDate);
		
		switch (key) {
		case "Day":
			banner = "BÁO CÁO THEO NGÀY";
			break;
			
		case "Month":
			banner = "BÁO CÁO THEO THÁNG";
			break;
			
		case "Year":
			banner = "BÁO CÁO THEO NĂM";
			break;
		}
		
		try {
			drawBill();
			drawCoupon();
			drawMoney(true);
			drawMoney(false);
			drawPageWage();
			drawTimeKeeping();
			drawAll();
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Thư pục /pdf không tồn tại!", 
					"Lỗi", JOptionPane.WARNING_MESSAGE);
		}
		
		leftCenter.revalidate();
		leftCenter.repaint();
	
		rightCenter.revalidate();
		rightCenter.repaint();
	}

	public void drawAll() throws FileNotFoundException, DocumentException {
		
		Document document = new Document(PageSize.A4.rotate());
		
		PdfWriter.getInstance(document, new FileOutputStream("pdf/Tonghop.pdf"));
		document.open();
		
		Paragraph paragraph_1 = new Paragraph(banner, TITLE_FONT);
		paragraph_1.setAlignment(Element.ALIGN_CENTER);
		paragraph_1.setSpacingAfter(30f);
		document.add(paragraph_1);

		String detail = "Từ " + fullTimeFormat.format( fromDate ) + 
				" đến " + fullTimeFormat.format( toCalendar.getTime() );
		Paragraph paragraph_2 = new Paragraph(detail, BANNER_FONT);
		paragraph_2.setAlignment(Element.ALIGN_CENTER);
		paragraph_2.setSpacingAfter(20f);
		document.add(paragraph_2);
		
		final String[] TITLES = {"STT", "Từ ngày", "Đến ngày", "Tổng tiền thực thu", 
				"Tổng tiền nhập hàng", "Tổng tiền xuất hàng", "Tổng tiền thưởng", "Tổng tiền ứng", 
				"Tổng lương", "Tổng doanh thu", "Tổng chi phí"};
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		PdfPTable table = createTable(TITLES);
		table.setWidths(new float[] {0.7f, 2f, 2f, 2, 2, 1.8f, 1.8f, 1.8f, 2, 2, 2});
		int pos = 0;
		
		for (FullStatistic fullStatistic : fullStatistics) {
			
			String stt = String.valueOf(++pos);
			String from = fullTimeFormat.format( fullStatistic.getFromCalendar().getTime() );
			String to = fullTimeFormat.format( fullStatistic.getToCalendar().getTime() );
			String realTotal = numberFormat.format( fullStatistic.getRealTotal() );
			String importString = numberFormat.format( fullStatistic.getImportTotal() );
			String exportString = numberFormat.format( fullStatistic.getExportTotal() );
			String bonus = numberFormat.format( fullStatistic.getBonus() );
			String advanced = numberFormat.format( fullStatistic.getAdvancedMoney() );
			String salary = numberFormat.format( fullStatistic.getSalary() );
			String revenue = numberFormat.format( fullStatistic.getRevenue() );
			String cost = numberFormat.format( fullStatistic.getCost() );

			dataset.addValue(fullStatistic.getCost(), "Chi phí", 
					dateFormat.format( fullStatistic.getFromCalendar().getTime() ));
			dataset.addValue(fullStatistic.getRevenue(), "Doanh thu", 
					dateFormat.format( fullStatistic.getFromCalendar().getTime() ));
			
			String[] row = {stt, from, to, realTotal, importString, exportString, bonus, advanced, salary, revenue, cost};
			addCellToTable(table, row, -1);
		}
		
		String realTotal = numberFormat.format( full.getRealTotal() );
		String importString = numberFormat.format( full.getImportTotal() );
		String exportString = numberFormat.format( full.getExportTotal() );
		String bonus = numberFormat.format( full.getBonus() );
		String advanced = numberFormat.format( full.getAdvancedMoney() );
		String salary = numberFormat.format( full.getSalary() );
		String revenue = numberFormat.format( full.getRevenue() );
		String cost = numberFormat.format( full.getCost() );
		
		String[] row = {"Tổng cộng:", realTotal, importString, exportString, bonus, advanced, salary, revenue, cost};
		PdfPTable sum = createTable(row);
		sum.setWidths(new float[] {4.7f, 2, 2, 1.8f, 1.8f, 1.8f, 2, 2, 2});

		String banner = "Biểu đồ tổng doanh thu và chi phí từ " + 
				dateFormat.format(fromDate) + " tới " + dateFormat.format(toCalendar.getTime());
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Chi phí", new Double( full.getCost() ));
		pieDataset.setValue("Doanh thu", new Double( full.getRevenue() ));
		
		JFreeChart pieChart = createPieChart(pieDataset, banner, "Chi phí", "Doanh thu");
		ChartPanel pieChartPanel = new ChartPanel(pieChart);
		rightCenter.addTab("Biểu đồ quạt", pieChartIcon, pieChartPanel);
	
		JFreeChart barChart = createBarChart(dataset, banner);
		ChartPanel barChartPanel = new ChartPanel(barChart);
		rightCenter.addTab("Biểu đồ cột", barChartIcon, barChartPanel);
	
		JFreeChart linehart = createLineChart(dataset, banner);
		ChartPanel lineChartPanel = new ChartPanel(linehart);
		rightCenter.addTab("Biểu đồ đường", lineChartIcon, lineChartPanel);
	
		Paragraph paragraph_3 = new Paragraph("Tổng toàn bộ doanh thu:" + revenue, BANNER_FONT);
		paragraph_3.setAlignment(Element.ALIGN_LEFT);
		paragraph_3.setIndentationLeft(80f);
		paragraph_3.setSpacingBefore(20f);
		document.add(paragraph_3);
		
		Paragraph paragraph_4 = new Paragraph("Tổng toàn bộ chi phí: " + cost, BANNER_FONT);
		paragraph_4.setAlignment(Element.ALIGN_LEFT);
		paragraph_4.setIndentationLeft(80f);
		paragraph_3.setSpacingAfter(20f);
		document.add(paragraph_4);
		
		Paragraph paragraph_5 = new Paragraph("Lưu ý: Tổng doanh thu = tổng tiền thực thu + tổng tiền xuất hàng", TABLE_FONT);
		paragraph_5.setAlignment(Element.ALIGN_LEFT);
		paragraph_5.setIndentationLeft(80f);
		document.add(paragraph_5);
		
		Paragraph paragraph_6 = new Paragraph("Lưu ý: Tổng chi phí = tổng tiền nhập hàng + "
				+ "tổng tiền thưởng + tổng tiền ứng + lương (lương cơ bản chưa tính tiền thưởng và tiền ứng).", TABLE_FONT);
		paragraph_6.setAlignment(Element.ALIGN_LEFT);
		paragraph_6.setIndentationLeft(80f);
		paragraph_6.setSpacingAfter(20f);
		document.add(paragraph_6);
		
		document.add(table);
		document.add(sum);
		document.close();
	}
	
	public void drawTimeKeeping() throws FileNotFoundException, DocumentException {

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		
		Document document = new Document(PageSize.A4.rotate());
		
		PdfWriter.getInstance(document, new FileOutputStream("pdf/Chamcong.pdf"));
		document.open();
		
		final String[] TITLES = {"STT", "Họ tên", "Ngày chấm công"};
		
		while (fromCalendar.compareTo(toCalendar) <= 0) {
		
			Calendar compareCalendar = createCompareDate(fromCalendar);
			int count = 0;
			
			PdfPTable table = createTable(TITLES);
			table.setWidths(new float[] {1, 5, 3});
			
			try {
				
				Timestamp timestamp_from = new Timestamp(fromCalendar.getTimeInMillis());
				Timestamp timestamp_to = new Timestamp(compareCalendar.getTimeInMillis());
				
				String sqlQuery = "SELECT * FROM CHAMCONG WHERE NGAY >= ? AND NGAY <= ?";
				PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
				preStatement.setTimestamp(1, timestamp_from);
				preStatement.setTimestamp(2, timestamp_to);
				
				ResultSet resultSet = preStatement.executeQuery();
				while(resultSet.next()) {
					String stt = String.valueOf( ++ count );
					String name = resultSet.getString(1);
					Timestamp timestamp = resultSet.getTimestamp(2);
					Date day = new Date(timestamp.getTime());
					String date = fullTimeFormat.format(day);
					
					String[] row = {stt, name, date};
					addCellToTable(table, row, 1);
				}
				
				resultSet.close();
				preStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Paragraph paragraph_1 = new Paragraph(banner, TITLE_FONT);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_1);
			
			String detail = "Từ " + fullTimeFormat.format( fromCalendar.getTime() ) + 
					" đến " + fullTimeFormat.format( compareCalendar.getTime() );
			Paragraph paragraph_2 = new Paragraph(detail, BANNER_FONT);
			paragraph_2.setAlignment(Element.ALIGN_CENTER);
			paragraph_2.setSpacingAfter(20f);
			document.add(paragraph_2);
			document.add(table);
			
			fromCalendar = addDate(compareCalendar);
			document.newPage();
		}
		
		document.close();
	}

	public void drawPageWage() throws FileNotFoundException, DocumentException {

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		
		Document document = new Document(PageSize.A4.rotate());
		
		PdfWriter.getInstance(document, new FileOutputStream("pdf/Luong.pdf"));
		document.open();
		
		final String[] TITLES = {"STT", "Họ tên", "Lương", "Tiền ứng", "Tiền thưởng", "Tổng tiền", "Ngày"};
		
		int pos = 0;
		while (fromCalendar.compareTo(toCalendar) <= 0) {
		
			Calendar compareCalendar = createCompareDate(fromCalendar);
			int count = 0;
			int allMoney = 0;
			int all = 0;
			
			PdfPTable table = createTable(TITLES);
			table.setWidths(new float[] {1, 3, 2, 2, 2, 2, 3});
			
			try {
				
				Timestamp timestamp_from = new Timestamp(fromCalendar.getTimeInMillis());
				Timestamp timestamp_to = new Timestamp(compareCalendar.getTimeInMillis());
				
				String sqlQuery = "SELECT * FROM TRALUONG WHERE NGAY >= ? AND NGAY <= ?";
				PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
				preStatement.setTimestamp(1, timestamp_from);
				preStatement.setTimestamp(2, timestamp_to);
				
				ResultSet resultSet = preStatement.executeQuery();
				while(resultSet.next()) {
					
					String stt = String.valueOf( ++ count );
					String name = resultSet.getString(1);
					
					int salary = resultSet.getInt(2);
					int bonus = resultSet.getInt(3);
					int advanceMoney = resultSet.getInt(4);
					int total = salary + bonus + advanceMoney;
					allMoney += total;
					all += salary - advanceMoney;
					
					Timestamp timestamp = resultSet.getTimestamp(5);
					Date day = new Date(timestamp.getTime());
					String date = Sever.fullTimeFormat.format(day);
					
					String money_1 = numberFormat.format( salary );
					String money_2 = numberFormat.format( bonus );
					String money_3 = numberFormat.format( advanceMoney );
					String money_4 = numberFormat.format( total );
					
					String[] row = {stt, name, money_1, money_2, money_3, money_4, date};
					addCellToTable(table, row, 1);
				}
				
				resultSet.close();
				preStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Paragraph paragraph_1 = new Paragraph(banner, TITLE_FONT);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_1);
			
			String detail = "Từ " + fullTimeFormat.format( fromCalendar.getTime() ) + 
					" đến " + fullTimeFormat.format( compareCalendar.getTime() );
			Paragraph paragraph_2 = new Paragraph(detail, BANNER_FONT);
			paragraph_2.setAlignment(Element.ALIGN_CENTER);
			paragraph_2.setSpacingAfter(20f);
			document.add(paragraph_2);
			
			String totalString = numberFormat.format(allMoney);
			
			FullStatistic fullStatistic = fullStatistics.get(pos++);
			fullStatistic.setSalary(allMoney);
			fullStatistic.addCost(all);
			
			full.setSalary( full.getSalary() + allMoney );
			full.addCost( all );
			
			String[] row = {"Tổng tiền:", totalString, ""};
			PdfPTable sum = createTable(row);
			sum.setWidths(new float[] {4, 8, 3});
			document.add(table);
			document.add(sum);
			
			fromCalendar = addDate(compareCalendar);
			document.newPage();
		}
		
		document.close();
	}
	
	public void drawMoney(boolean isBonus) throws FileNotFoundException, DocumentException {

		String tableName = (isBonus) ? "TIENTHUONG" : "TIENUNG";
		String fileName = (isBonus) ? "Tienthuong" : "Tienung";
		
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		
		Document document = new Document(PageSize.A4);
		
		PdfWriter.getInstance(document, new FileOutputStream("pdf/" + fileName + ".pdf"));
		document.open();
		
		final String[] TITLES = {"STT", "Họ tên", "Số tiền", "Ngày", "Nhân viên thực hiện"};
		
		int pos = 0;
		while (fromCalendar.compareTo(toCalendar) <= 0) {
		
			Calendar compareCalendar = createCompareDate(fromCalendar);
			int count = 0;
			int total = 0;
			
			PdfPTable table = createTable(TITLES);
			table.setWidths(new float[] {1, 3, 2, 3, 3});
			
			try {
				
				Timestamp timestamp_from = new Timestamp(fromCalendar.getTimeInMillis());
				Timestamp timestamp_to = new Timestamp(compareCalendar.getTimeInMillis());
				
				String sqlQuery = "SELECT * FROM " + tableName + " WHERE NGAY >= ? AND NGAY <= ?";
				PreparedStatement preStatement = connection.prepareStatement(sqlQuery);
				preStatement.setTimestamp(1, timestamp_from);
				preStatement.setTimestamp(2, timestamp_to);
				
				ResultSet resultSet = preStatement.executeQuery();
				while(resultSet.next()) {
					
					String stt = String.valueOf( ++ count );
					String name = resultSet.getString(1);
					int money = resultSet.getInt(2);
					total += money;
					String money_text = numberFormat.format( money );
					Timestamp timestamp = resultSet.getTimestamp(3);
					Date day = new Date(timestamp.getTime());
					String date = fullTimeFormat.format(day);
					String staff = resultSet.getString(4);
					
					String[] row = {stt, name, money_text, date, staff};
					addCellToTable(table, row, 1);
				}
				
				resultSet.close();
				preStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			Paragraph paragraph_1 = new Paragraph(banner, TITLE_FONT);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_1);
			
			String detail = "Từ " + fullTimeFormat.format( fromCalendar.getTime() ) + 
					" đến " + fullTimeFormat.format( compareCalendar.getTime() );
			Paragraph paragraph_2 = new Paragraph(detail, BANNER_FONT);
			paragraph_2.setAlignment(Element.ALIGN_CENTER);
			paragraph_2.setSpacingAfter(20f);
			document.add(paragraph_2);
			
			FullStatistic fullStatistic = fullStatistics.get(pos++);
			if (isBonus) {
				fullStatistic.setBonus(total);
				full.setBonus( full.getBonus() + total );
			} else {
				fullStatistic.setAdvancedMoney(total);
				full.setAdvancedMoney( full.getAdvancedMoney() + total );
			}
			fullStatistic.addCost(total);
			full.addCost( total );
			
			String totalString = numberFormat.format(total);
			String[] row = {"Tổng tiền: ", totalString, ""};
			PdfPTable sum = createTable(row);
			sum.setWidths(new float[] {4, 2, 6});
			document.add(table);
			document.add(sum);
			
			fromCalendar = addDate(compareCalendar);
			document.newPage();
		}
		
		document.close();
	}
	
	
	private void drawBill() throws FileNotFoundException, DocumentException {
		
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		
		Document document = new Document(PageSize.A4);
		Document detailDocument = new Document(PageSize.A4.rotate());
		
		PdfWriter.getInstance(document, new FileOutputStream("pdf/Hoadon.pdf"));
		document.open();
		
		PdfWriter.getInstance(detailDocument, new FileOutputStream("pdf/Chitiethoadon.pdf"));
		detailDocument.open();
		
		ArrayList<Bill> bills = billModel.getBills();
		int index = bills.size() - 1;
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		final String[] TITLES = {"STT", "Tên bàn", "Thời gian bắt đầu", "Thời gian kết thúc", "Ngày",
				"Số tiền thực tế", "Số tiền thực thu", "Nhân viên"};
		
		final String[] DETAIL_TITLES = {"STT", "Tên bàn", "Thời gian bắt đầu", "Ngày", "Nhân viên"};
		
		final String[] FOOD_TITLES = {"Tên món ăn", "Số lượng", "Đơn giá", "Thành tiền"};
		
		int xyzTotal = 0;
		full = new FullStatistic();
		
		while (fromCalendar.compareTo(toCalendar) <= 0) {
		
			Calendar compareCalendar = createCompareDate(fromCalendar);
			int count = 0;
			int totalValue = 0;
			int realTotalValue = 0;
			
			PdfPTable table = createTable(TITLES);
			table.setWidths(new float[]{0.5f, 0.8f, 1f, 1f, 1.2f, 1.2f, 1.2f, 1.2f});
			table.setSpacingBefore(20f);
			
			PdfPTable detailTable = new PdfPTable(6);
			addCellToTable(detailTable, DETAIL_TITLES, -1);
			detailTable.setWidths(new float[]{0.5f, 0.8f, 1f, 1.2f, 1.2f, 5f});
			detailTable.setSpacingBefore(20f);
			
			PdfPTable topFoodHeader = createTable(new String[] {"Chi tiết món ăn"});
			PdfPTable foodHeader = createTable(FOOD_TITLES);
			foodHeader.setWidths(new float[]{2f, 1f, 1f, 1f});
			topFoodHeader.addCell(new PdfPCell( foodHeader ));
			detailTable.addCell(new PdfPCell(topFoodHeader));
			
			HashMap<Food, Integer> foodMap = new HashMap<>();
			
			for (;index >= 0; index--) {
				Bill bill = bills.get(index);
				if (bill.getEnd() != null) {
					
					Calendar billCalendar = Calendar.getInstance();
					billCalendar.setTime(bill.getDay());
					
					if (billCalendar.compareTo(fromCalendar) >= 0) {
						if (billCalendar.compareTo(compareCalendar) <= 0) {
							
							totalValue += bill.getTotal();
							realTotalValue += bill.getRealTotal();
							
							String stt = String.valueOf(++count);
							String nameOfTable = bill.getNameOfTable();
							String start = bill.getStart().toString();
							String end = bill.getEnd().toString();
							String date = dateFormat.format(bill.getDay());
							String total =  numberFormat.format(bill.getTotal());
							String realTotal = numberFormat.format(bill.getRealTotal());
							String name = bill.getName();
							
							String[] row = {stt, nameOfTable, start, end, date, total, realTotal, name};
							addCellToTable(table, row, -1);
							
							String[] detailRow = {stt, nameOfTable, start, date, name};
							addCellToTable(detailTable, detailRow, -1);
							
							PdfPTable foodTable = new PdfPTable(4);
							foodTable.setWidths(new float[]{2f, 1f, 1f, 1f});
							
							HashMap<Food, DetailBill> foods = bill.getFoods();
							for (Food food : foods.keySet()) {
								DetailBill info = foods.get(food);
								
								String nameFood = food.getName();
								String amount = String.valueOf( info.getAmount() ) + " " + food.getSmallUnit();
								String price = numberFormat.format( info.getPrice() );
								String totalFood = numberFormat.format( info.getTotal() );
								
								String[] foodRow = {nameFood, amount, price, totalFood};
								addCellToTable(foodTable, foodRow, 0);
							
								if (foodMap.get(food) == null)
									foodMap.put(food, info.getAmount());
								else
									foodMap.put(food, foodMap.get(food) + info.getAmount());
							}
							
							PdfPTable foodTableWithTotal = new PdfPTable(1);
							foodTableWithTotal.addCell(new PdfPCell(foodTable));
							
							PdfPTable totalInFoodTAble = new PdfPTable(2);
							totalInFoodTAble.setWidths(new float[]{4f, 1f});
							
							String[] result = {"Tổng tiền thực tế:", 
									numberFormat.format(bill.getTotal())};
							addCellToTable(totalInFoodTAble, result, -1);
							
							String[] realResult = {"Tổng tiền thực thu:", 
									numberFormat.format(bill.getRealTotal())};
							addCellToTable(totalInFoodTAble, realResult, -1);
							foodTableWithTotal.addCell(new PdfPCell(totalInFoodTAble));
							
							detailTable.addCell( foodTableWithTotal );
						} else break;					
					}
				}
			}
			
			Paragraph paragraph_1 = new Paragraph(banner, TITLE_FONT);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_1);
			detailDocument.add(paragraph_1);
			
			String detail = "Từ " + fullTimeFormat.format( fromCalendar.getTime() ) + 
					" đến " + fullTimeFormat.format( compareCalendar.getTime() );
			Paragraph paragraph_2 = new Paragraph(detail, BANNER_FONT);
			paragraph_2.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_2);
			detailDocument.add(paragraph_2);

			dataset.addValue(totalValue, "Thực tế", 
					dateFormat.format( fromCalendar.getTime() ));
			dataset.addValue(realTotalValue, "Thực thu", 
					dateFormat.format( fromCalendar.getTime() ));
			
			FullStatistic fullStatistic = new FullStatistic(fromCalendar, compareCalendar);
			fullStatistic.setRealTotal(realTotalValue);
			fullStatistic.addRevenue(realTotalValue);
			xyzTotal += totalValue;
			
			full.setRealTotal( realTotalValue + full.getRealTotal() );
			full.addRevenue( realTotalValue );
			fullStatistics.add(fullStatistic);

			String totalString = numberFormat.format(totalValue);
			String realTotalString = numberFormat.format(realTotalValue);
			String[] row = {"Tổng tiền: ", totalString, realTotalString, ""};
			
			Paragraph paragraph_3 = new Paragraph("Tổng tiền thực tế: " + totalString, BANNER_FONT);
			paragraph_3.setAlignment(Element.ALIGN_LEFT);
			paragraph_3.setIndentationLeft(80f);
			paragraph_3.setSpacingBefore(20f);
			detailDocument.add(paragraph_3);
			
			Paragraph paragraph_4 = new Paragraph("Tổng tiền thực thu: " + realTotalString, BANNER_FONT);
			paragraph_4.setAlignment(Element.ALIGN_LEFT);
			paragraph_4.setIndentationLeft(80f);
			paragraph_3.setSpacingAfter(20f);
			detailDocument.add(paragraph_4);
			
			PdfPTable sumTable = createTable(row);
			sumTable.setWidths(new float[]{0.5f + 0.8f + 1f + 1f + 1.2f, 1.2f, 1.2f, 1.2f});
			document.add(table);
			document.add(sumTable);
			detailDocument.add(detailTable);
			
			Paragraph paragraph_5 = new Paragraph("SỐ MÓN ĂN ĐÃ BÁN ĐƯỢC", BANNER_FONT);
			paragraph_5.setAlignment(Element.ALIGN_CENTER);
			paragraph_5.setSpacingBefore(20f);
			paragraph_5.setSpacingAfter(10f);
			detailDocument.add(paragraph_5);
			
			PdfPTable table_2 = createTable(new String[] {"STT", "Tên món ăn", "Số lượng đã bán"});
			table_2.setWidths(new float[] {1, 5, 3});
			
			int count_2 = 0;
			for (Food food : foodMap.keySet())
				addCellToTable(table_2, new String[] {String.valueOf(++count_2),
						food.getName(), foodMap.get(food) + " " + food.getSmallUnit()}, 1);
			detailDocument.add(table_2);
			
			document.newPage();
			detailDocument.newPage();
			fromCalendar = addDate(compareCalendar);
			
		}
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new java.awt.Font("Calibri", Font.NORMAL, 15));
		leftCenter.addTab("Hóa đơn", tabbedPane);
		String banner = "Biểu đồ tổng tiền thực tế và thực thu từ " + 
					dateFormat.format(fromDate) + " tới " + dateFormat.format(toCalendar.getTime());
		
		JFreeChart barChart = createBarChart(dataset, banner);
		ChartPanel barChartPanel = new ChartPanel(barChart);
		tabbedPane.addTab("Biểu đồ cột", barChartIcon, barChartPanel);
		
		JFreeChart linehart = createLineChart(dataset, banner);
		ChartPanel lineChartPanel = new ChartPanel(linehart);
		tabbedPane.addTab("Biểu đồ đường", lineChartIcon, lineChartPanel);
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Thực tế", new Double(xyzTotal));
		pieDataset.setValue("Thực thu", new Double(full.getRealTotal()));
		
		JFreeChart pieChart = createPieChart(pieDataset, banner, "Thực tế", "Thực thu");
		ChartPanel pieChartPanel = new ChartPanel(pieChart);
		tabbedPane.addTab("Biểu đồ quạt", pieChartIcon, pieChartPanel);
		
		detailDocument.close();
		document.close();
	}
	
	
	private void drawCoupon() throws FileNotFoundException, DocumentException {

		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(fromDate);
		
		Document importDocument = new Document(PageSize.A4.rotate());
		Document exportDocument = new Document(PageSize.A4.rotate());
		
		PdfWriter.getInstance(importDocument, new FileOutputStream("pdf/Nhap.pdf"));
		importDocument.open();
		
		PdfWriter.getInstance(exportDocument, new FileOutputStream("pdf/Xuat.pdf"));
		exportDocument.open();
		
		ArrayList<Coupon> coupons = couponModel.getCoupons();
		int index = coupons.size() - 1;
		
		final String[] TITLES = {"STT", "Thời gian", "Tổng tiền", "Nhân viên", "Nhà cung cấp"};
		
		final String[] FOOD_TITLES = {"Tên món ăn", "Số lượng (lớn)", "Số lượng (nhỏ)", "Đơn giá", "Thành tiền", "Ghi chú"};
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		int pos = 0;
		while (fromCalendar.compareTo(toCalendar) <= 0) {
		
			Calendar compareCalendar = createCompareDate(fromCalendar);
			int count = 0;
			int importValue = 0;
			int exportValue = 0;
			
			PdfPTable importTable = new PdfPTable(6);
			addCellToTable(importTable, TITLES, -1);
			importTable.setWidths(new float[]{0.5f, 1.3f, 1f, 1.2f, 1.2f, 6f});
			importTable.setSpacingBefore(20f);
			
			PdfPTable exportTable = new PdfPTable(6);
			addCellToTable(exportTable, TITLES, -1);
			exportTable.setWidths(new float[]{0.5f, 1.5f, 1f, 1.2f, 1.2f, 6f});
			exportTable.setSpacingBefore(20f);
			
			PdfPTable topHeader = createTable(new String[] {"Chi tiết hàng nhập/xuất"});
			PdfPTable foodHeader = createTable(FOOD_TITLES);
			foodHeader.setWidths(new float[]{2f, 1f, 1f, 1f, 1.4f, 1.6f});
			topHeader.addCell(new PdfPCell( foodHeader ));
			
			importTable.addCell(new PdfPCell(topHeader));
			exportTable.addCell(new PdfPCell(topHeader));
			
			HashMap<Food, DetailCoupon> importFoodMap = new HashMap<>();
			HashMap<Food, DetailCoupon> exportFoodMap = new HashMap<>();
			
			for (;index >= 0; index--) {
				
				Coupon coupon = coupons.get(index);
				
				Calendar couponCalendar = Calendar.getInstance();
				couponCalendar.setTime(coupon.getDay());
				
				if (couponCalendar.compareTo(fromCalendar) >= 0) {
					if (couponCalendar.compareTo(compareCalendar) <= 0) {
						
						String stt = String.valueOf(++count);
						String date = fullTimeFormat.format( coupon.getDay() );
						String total = numberFormat.format( coupon.getTotal() );
						String name = coupon.getName();
						String supplier = coupon.getSupplier();
						
						String[] row = {stt, date, total, name, supplier};
						
						PdfPTable foodTable = new PdfPTable(6);
						foodTable.setWidths(new float[]{2f, 1f, 1f, 1f, 1.4f, 1.6f});
						
						ArrayList<DetailCoupon> foods = coupon.getFoods();
						
						for (DetailCoupon detailCoupon : foods) {
							
							Food food = detailCoupon.getFood();
							
							String nameFood = food.getName();
							String bigAmount = String.valueOf( detailCoupon.getBigNumber() ) + " " + food.getBigUnit();
							String smallAmount = String.valueOf( detailCoupon.getSmallNumber() ) + " " + food.getSmallUnit();
							String price = numberFormat.format( detailCoupon.getPrice() );
							String totalFood = numberFormat.format( detailCoupon.getTotal() );
							String note = detailCoupon.getNote();

							String[] foodRow = {nameFood, bigAmount, smallAmount, price, totalFood, note};
							addCellToTable(foodTable, foodRow, 0);
							
							if (coupon.isImport()) {
								if (importFoodMap.get(food) == null) {
									DetailCoupon info = new DetailCoupon(detailCoupon);
									importFoodMap.put(food, info);
								} else {
									DetailCoupon info = importFoodMap.get(food);
									info.addDetailCoupon(detailCoupon);
								}
							} else {
								if (exportFoodMap.get(food) == null) {
									DetailCoupon info = new DetailCoupon(detailCoupon);
									exportFoodMap.put(food, info);
								} else {
									DetailCoupon info = exportFoodMap.get(food);
									info.addDetailCoupon(detailCoupon);
								}
							}
						}
						
						PdfPTable foodTableWithTotal = new PdfPTable(1);
						foodTableWithTotal.addCell(new PdfPCell(foodTable));
						
						PdfPTable totalInFoodTAble = new PdfPTable(3);
						totalInFoodTAble.setWidths(new float[]{5f, 1.4f, 1.6f});

						String[] realResult = {"Tổng tiền:", total, ""};
						addCellToTable(totalInFoodTAble, realResult, -1);
						foodTableWithTotal.addCell(new PdfPCell(totalInFoodTAble));
						
						if (coupon.isImport()) {
							addCellToTable(importTable, row, -1);
							importTable.addCell( foodTableWithTotal );
							importValue += coupon.getTotal();
						} else {
							addCellToTable(exportTable, row, -1);
							exportTable.addCell( foodTableWithTotal );
							exportValue += coupon.getTotal();
						}
						
					} else break;					
				}
				
			}
		
			Paragraph paragraph_1 = new Paragraph(banner, TITLE_FONT);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			importDocument.add(paragraph_1);
			exportDocument.add(paragraph_1);
			
			String detail = "Từ " + fullTimeFormat.format( fromCalendar.getTime() ) + 
					" đến " + fullTimeFormat.format( compareCalendar.getTime() );
			Paragraph paragraph_2 = new Paragraph(detail, BANNER_FONT);
			paragraph_2.setAlignment(Element.ALIGN_CENTER);
			importDocument.add(paragraph_2);
			exportDocument.add(paragraph_2);
			
			dataset.addValue(importValue, "Nhập hàng", dateFormat.format( fromCalendar.getTime() ));
			dataset.addValue(exportValue, "Xuất hàng", dateFormat.format( fromCalendar.getTime() ));
			
			String importString = numberFormat.format(importValue);
			String exportString = numberFormat.format(exportValue);
			
			FullStatistic fullStatistic = fullStatistics.get(pos++);
			fullStatistic.setImportTotal(importValue);
			fullStatistic.setExportTotal(exportValue);
			fullStatistic.addCost(importValue);
			fullStatistic.addRevenue(exportValue);
			
			full.setImportTotal( full.getImportTotal() + importValue );
			full.setExportTotal( full.getExportTotal() + exportValue );
			full.addCost( importValue );
			full.addRevenue( exportValue );
			
			Paragraph paragraph_3 = new Paragraph("Tổng tiền nhập hàng: " + importString, BANNER_FONT);
			paragraph_3.setAlignment(Element.ALIGN_LEFT);
			paragraph_3.setIndentationLeft(80f);
			paragraph_3.setSpacingBefore(20f);
			paragraph_3.setSpacingBefore(20f);
			importDocument.add(paragraph_3);
			
			Paragraph paragraph_4 = new Paragraph("Tổng tiền xuất hàng: " + exportString, BANNER_FONT);
			paragraph_4.setAlignment(Element.ALIGN_LEFT);
			paragraph_4.setIndentationLeft(80f);
			paragraph_4.setSpacingBefore(20f);
			paragraph_4.setSpacingBefore(20f);
			exportDocument.add(paragraph_4);
			
			importDocument.add(importTable);
			exportDocument.add(exportTable);
			
			Paragraph paragraph_5 = new Paragraph("SỐ HÀNG ĐÃ NHẬP", BANNER_FONT);
			paragraph_5.setAlignment(Element.ALIGN_CENTER);
			paragraph_5.setSpacingBefore(20f);
			paragraph_5.setSpacingAfter(10f);
			importDocument.add(paragraph_5);
			
			PdfPTable table_2 = createTable(new String[] {"STT", "Tên món ăn", "Số lượng (lớn)", "Số lương (nhỏ)"});
			table_2.setWidths(new float[] {1, 5, 2, 2});
			
			int count_2 = 0;
			for (Food food : importFoodMap.keySet()) {
				DetailCoupon info = importFoodMap.get(food);
				addCellToTable(table_2, new String[] {String.valueOf(++count_2),
						food.getName(), info.getBigNumber() + " " + food.getBigUnit(),
						info.getSmallNumber() + " " + food.getSmallUnit()}, 1);
			}
			importDocument.add(table_2);
			
			Paragraph paragraph_6 = new Paragraph("SỐ HÀNG ĐÃ Xuất", BANNER_FONT);
			paragraph_6.setAlignment(Element.ALIGN_CENTER);
			paragraph_6.setSpacingBefore(20f);
			paragraph_6.setSpacingAfter(10f);
			exportDocument.add(paragraph_6);
			
			PdfPTable table_3 = createTable(new String[] {"STT", "Tên món ăn", "Số lượng (lớn)", "Số lương (nhỏ)"});
			table_3.setWidths(new float[] {1, 5, 2, 2});
			
			int count_3 = 0;
			for (Food food : exportFoodMap.keySet()) {
				DetailCoupon info = exportFoodMap.get(food);
				addCellToTable(table_3, new String[] {String.valueOf(++count_3),
						food.getName(), info.getBigNumber() + " " + food.getBigUnit(),
						info.getSmallNumber() + " " + food.getSmallUnit()}, 1);
			}
			exportDocument.add(table_3);
			
			importDocument.newPage();
			exportDocument.newPage();
			fromCalendar = addDate(compareCalendar);
		}
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new  java.awt.Font("Calibri", Font.NORMAL, 15));
		leftCenter.addTab("Nhập/Xuất", tabbedPane);
		
		String banner = "Biểu đồ nhập hàng và xuất hàng từ " + dateFormat.format(fromDate) 
				+ " tới " + dateFormat.format(toCalendar.getTime());
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		pieDataset.setValue("Nhập hàng", new Double( full.getImportTotal() ));
		pieDataset.setValue("Xuất hàng", new Double( full.getExportTotal() ));
		
		JFreeChart pieChart = createPieChart(pieDataset, banner, "Nhập hàng", "Xuất hàng");
		ChartPanel pieChartPanel = new ChartPanel(pieChart);
		tabbedPane.addTab("Biểu đồ quạt", pieChartIcon, pieChartPanel);
		
		JFreeChart barChart = createBarChart(dataset, banner);
		ChartPanel barChartPanel = new ChartPanel(barChart);
		tabbedPane.addTab("Biểu đồ cột", barChartIcon, barChartPanel);
		
		JFreeChart linehart = createLineChart(dataset, banner);
		ChartPanel lineChartPanel = new ChartPanel(linehart);
		tabbedPane.addTab("Biểu đồ đường", lineChartIcon, lineChartPanel);
		
		importDocument.close();
		exportDocument.close();
		
	}

	private JFreeChart createLineChart(CategoryDataset dataset, String banner_0) {
		
		JFreeChart linehart = ChartFactory.createLineChart(banner_0.toUpperCase(), "Ngày", "Số tiền", dataset);
		CategoryPlot plot = linehart.getCategoryPlot();
		plot.getRenderer().setBaseStroke( new BasicStroke( 3 ) );
		return linehart;
	}
	
	private JFreeChart createBarChart(CategoryDataset dataset, String banner_0) {
		
		JFreeChart barChart = ChartFactory.createBarChart(banner_0.toUpperCase(), "Ngày", "Số tiền", dataset);
		CategoryItemRenderer barRenderer = ((CategoryPlot)barChart.getPlot()).getRenderer();
		barRenderer.setBaseItemLabelGenerator( new StandardCategoryItemLabelGenerator(
				"{2}", NumberFormat.getIntegerInstance()));
		barRenderer.setBaseItemLabelsVisible(true);
		barRenderer.setSeriesPaint(0, Color.BLUE);
		barRenderer.setSeriesPaint(1, Color.RED);
		return barChart;
	}

	private JFreeChart createPieChart(PieDataset pieDataset, String banner_0, String banner_1, String banner_2) {
		
		JFreeChart pieChart = ChartFactory.createPieChart(banner_0.toUpperCase(), pieDataset);
		PiePlot plot = (PiePlot) pieChart.getPlot();
		plot.setSectionPaint(banner_1, Color.GREEN);
		plot.setSectionPaint(banner_2, Color.RED);
		plot.setExplodePercent(banner_1, 0.10);
		plot.setSimpleLabels(true);
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}: {1} ({2})", numberFormat, new DecimalFormat("0%")));
		return pieChart;
	}
	
	public void addCellToTable(PdfPTable table, String[] row, int left) {
		
		for (int i = 0; i < row.length; i++) {
			PdfPCell cell;
			
			if (i != left)
				cell = createCell(row[i], true);
			else
				cell = createCell(row[i], false);
			
			table.addCell(cell);	
		}
	}
	
	public PdfPTable createTable(String[] titles) {
		
		PdfPTable table = new PdfPTable(titles.length);
		
		for (String string : titles) {
			PdfPCell cell = new PdfPCell(new Phrase(string, HEADER_FONT));
			cell.setMinimumHeight(20f);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell);
		}
		
		return table;
	}
	
	public PdfPCell createCell(String string, boolean isCenter) {
		
		PdfPCell cell = new PdfPCell(new Phrase(string, TABLE_FONT));
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setMinimumHeight(20f);
		
		if ( !isCenter ) {
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPaddingLeft(15f);
		} else {
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		}
		
		return cell;
	}
	
	public Calendar addDate(Calendar calendar) {
		calendar.add(Calendar.MILLISECOND, 1);
		return calendar;
	}
	
	public Calendar createCompareDate(Calendar calendar) {
		
		Calendar compareCalendar = Calendar.getInstance();
		compareCalendar.setTime(calendar.getTime());
		
		switch (key) {
		case "Day":
			compareCalendar.add(Calendar.DATE, 1);
			break;
			
		case "Month":
			compareCalendar.add(Calendar.MONTH, 1);
			break;
			
		case "Year":
			compareCalendar.add(Calendar.YEAR, 1);
			break;
		}
		
		compareCalendar.add(Calendar.MILLISECOND, -1);
		
		if (compareCalendar.compareTo(toCalendar) > 0)
			compareCalendar.setTime(toCalendar.getTime());
		return compareCalendar;
	}
	
	public void setViewAndEvent() {

		this.fullStatistics = new ArrayList<>();
		
		this.center = statisticPane.getCenter();
		this.center.setLayout(new GridLayout(0, 2, 10, 10));
		
		this.leftCenter = new JTabbedPane();
		leftCenter.setFont(new java.awt.Font("Calibri", Font.NORMAL, 20));
		this.center.add(leftCenter);
		
		this.rightCenter = new JTabbedPane();
		rightCenter.setFont(new java.awt.Font("Calibri", Font.NORMAL, 15));
		this.center.add(rightCenter);
		
		statisticPane.getSelectButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				createDayDialog();
			}
		});	
		
		statisticPane.getReportButton().addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createReportDialog();
			}
		});
	}
	
	private void createReportDialog() {
		ReportDialog reportDialog = new ReportDialog();
		reportDialog.setModal(true);
		reportDialog.setVisible(true);
	}
	
	private void createDayDialog() {
		DayDialog daydialog = new DayDialog(this);
		daydialog.setModal(true);
		daydialog.setVisible(true);
	}
	
}
