package subComponent;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import connection.Sever;
import object.Bill;
import object.DetailBill;
import object.Food;

public class BillPdf {
	
	private Bill bill;
	private BaseFont baseFont;
	
	public BillPdf(Bill bill) {
		this.bill = bill;
	}
	
	public void createPdf() {
		
		baseFont = null;
		try {
			baseFont = BaseFont.createFont("C:\\Windows\\Fonts\\Calibri.ttf", BaseFont.IDENTITY_H, true);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

		try {
			
			Document document = new Document();
			
			Calendar calendar = Calendar.getInstance();
			String fileName = Sever.fileFormat.format(calendar.getTime());
			String filePath = "pdf/Hoadon/" + fileName + ".pdf";

			final Font font_1 = new Font(baseFont, 13f, Font.BOLD);
			final Font font_2 = new Font(baseFont, 10f, Font.NORMAL);
			final Font font_3 = new Font(baseFont, 10f, Font.NORMAL);
			final Font font_4 = new Font(baseFont, 10f, Font.NORMAL);
			final Font font_5 = new Font(baseFont, 10f, Font.NORMAL);
			final Font fontCell = new Font(baseFont, 8f, Font.NORMAL);
			final Font font_7 = new Font(baseFont, 8f, Font.ITALIC);
			final Font fontHeader = new Font(baseFont, 10f, Font.NORMAL);
			
			PdfPTable table = new PdfPTable(4);
			table.setWidths( new float[]{3f, 1f, 2.5f, 3f} );
			addToTable(table, new String[] {"MÓN ĂN", "SL", "ĐG", "THÀNH TIỀN"}, fontCell);
			
			HashMap<Food, DetailBill> foods = bill.getFoods();
			
			for (Food food : foods.keySet()) {
				
				DetailBill info = foods.get(food);
				
				String name = food.getName();
				String amount = String.valueOf( info.getAmount() );
				String price = Sever.numberFormat.format( info.getPrice() );
				String total = Sever.numberFormat.format( info.getTotal() );
				
				PdfPCell cell_2 = new PdfPCell(new Phrase(name, fontHeader));
				cell_2.setBorder(Rectangle.NO_BORDER);
				cell_2.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell_2);
				
				PdfPCell cell_3 = new PdfPCell(new Phrase(amount, fontCell));
				cell_3.setBorder(Rectangle.NO_BORDER);
				cell_3.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell_3.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell_3);
				
				PdfPCell cell_4 = new PdfPCell(new Phrase(price, fontCell));
				cell_4.setBorder(Rectangle.NO_BORDER);
				cell_4.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell_4.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell_4);
				
				PdfPCell cell_5 = new PdfPCell(new Phrase(total, fontCell));
				cell_5.setBorder(Rectangle.NO_BORDER);
				cell_5.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell_5.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell_5);
			}

			Rectangle rectangle = new Rectangle(132, 1500);
			document.setPageSize(rectangle);
			document.setMargins(0, 0, 0, 0);
			
			PdfWriter.getInstance(document, new FileOutputStream( filePath ));
			document.open();
			
			Paragraph paragraph_1 = new Paragraph("QUÁN: BÊ THUI 286", font_1);
			paragraph_1.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_1);
			
			Paragraph paragraph_2 = new Paragraph("CHUYÊN: CÁC MÓN NHẬU VÀ ĂN SÁNG BÌNH DÂN", font_2);
			paragraph_2.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_2);
			
			Paragraph paragraph_3 = new Paragraph("Đỉa chỉ: Ngã Ba Long Châu (Đường Lên Cao Tốc) Chi Long, Yên Phong, Bắc Ninh", font_3);
			paragraph_3.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_3);
			
			Paragraph paragraph_4 = new Paragraph("Số điện thoại: 0912.549.676 * 0913.889.178 * 0336.889.268", font_4);
			paragraph_4.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_4);
			
			Paragraph paragraph_5 = new Paragraph("HÓA ĐƠN BÁN HÀNG", font_5);
			paragraph_5.setAlignment(Element.ALIGN_CENTER);
			paragraph_5.setSpacingBefore(10f);
			document.add(paragraph_5);

			Chunk glue = new Chunk(new VerticalPositionMark());
			Paragraph paragraph_10 = new Paragraph(bill.getNameOfTable().toUpperCase(), font_5);
			paragraph_10.add(new Chunk(glue));
			paragraph_10.setIndentationRight(10f);
			paragraph_10.add("Giờ vào: " + Sever.timeFormat.format(bill.getStart()));
			document.add(paragraph_10);
			
			Paragraph paragraph_12 = new Paragraph("Giờ ra: " + Sever.timeFormat.format(bill.getEnd()), font_5);
			paragraph_12.setAlignment(Element.ALIGN_RIGHT);
			paragraph_12.setSpacingAfter(5f);
			paragraph_12.setIndentationRight(10f);
			document.add(paragraph_12);
			
			PdfPTable totalTable = new PdfPTable(2);
			totalTable.setWidths( new float[]{4f, 5.5f} );
		
			PdfPCell cell_1 = new PdfPCell(new Phrase("TỔNG CỘNG:", fontHeader));
			cell_1.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell_1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell_1.setBorder(Rectangle.TOP);
			totalTable.addCell(cell_1);
			
			String total = Sever.numberFormat.format( bill.getTotal() );
			PdfPCell cell_2 = new PdfPCell(new Phrase(total, fontHeader));
			cell_2.setBorder(Rectangle.TOP);
			cell_2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell_2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			totalTable.addCell(cell_2);
			
			table.setWidthPercentage(100);
			totalTable.setWidthPercentage(100);
			
			table.completeRow();
			totalTable.completeRow();
			
			document.add(table);
			document.add(totalTable);
			
			Paragraph paragraph_6 = new Paragraph("Bằng chữ: " + Sever.getStringFromNumber( bill.getTotal() ), fontHeader);
			paragraph_6.setSpacingAfter(10f);
			paragraph_6.setSpacingBefore(3f);
			paragraph_6.setAlignment(Element.ALIGN_CENTER);
			document.add(paragraph_6);
			
			SimpleDateFormat format = new SimpleDateFormat("dd");
			String string = "Ngày " + format.format(bill.getDay());
			format = new SimpleDateFormat("MM");
			string += " tháng " + format.format(bill.getDay());
			format = new SimpleDateFormat("YYYY");
			string += " năm " + format.format(bill.getDay());
			
			Paragraph paragraph_7 = new Paragraph(string, font_7);
			paragraph_7.setAlignment(Element.ALIGN_RIGHT);
			paragraph_7.setIndentationRight(20f);
			document.add(paragraph_7);
			
			Paragraph paragraph_8 = new Paragraph("Người bán hàng", fontCell);
			paragraph_8.setAlignment(Element.ALIGN_RIGHT);
			paragraph_8.setIndentationRight(20f);
			document.add(paragraph_8);

			Paragraph paragraph_9 = new Paragraph(bill.getName(), fontCell);
			paragraph_9.setAlignment(Element.ALIGN_RIGHT);
			paragraph_9.setIndentationRight(20f);
			document.add(paragraph_9);
			
			document.close();
			
			File file = new File( filePath );
			Desktop.getDesktop().open(file);
			
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}
	}

	private void addToTable(PdfPTable pdfPTable, String[] strings, Font font) {
		
		for (String string : strings) {
			PdfPCell pdfPCell = new PdfPCell(new Phrase(string, font));
			pdfPCell.setBorder(Rectangle.BOTTOM);
			pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			pdfPTable.addCell(pdfPCell);
		}
		
	}

}

