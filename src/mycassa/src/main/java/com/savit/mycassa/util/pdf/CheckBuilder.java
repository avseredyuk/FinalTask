package com.savit.mycassa.util.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.savit.mycassa.entity.product.Product;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.util.exception.CantPrintCheckException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckBuilder {


//
//
//	public static ByteArrayInputStream buildSessionPDFCheck(Session session, List<Sale> sales) throws CantPrintCheckException{
//
//		Document document = new Document();
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//		User user = session.getUser();
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//
//			// welcome
//			Paragraph header = new Paragraph(PdfFragments.setCheckText("WELCOME", 20, Font.BOLD));
//			header.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(header);
//
//			// check
//			header = new Paragraph(PdfFragments.setCheckText("SALES CHECK №" + session.getId(), 13, Font.NORMAL));
//			header.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(header);
//
//			DottedLineSeparator dl = new DottedLineSeparator();
//			// cashier---------------
//			header = new Paragraph(new Chunk(dl));
//			header.add("\n");
//
//			header.add(PdfFragments.setCheckText("Cashier: " + user.getFirstName() + " " + user.getLastName(), 15, Font.ITALIC));
//			header.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(header);
//			header = new Paragraph(new Chunk(dl));
//			PdfFragments.addEmptyLine(header, 1);
//			document.add(header);
//			// ---------------
//
//			long totalPrice = 0L;
//			for (Sale sale : sales) {
//				header = new Paragraph(PdfFragments.setCheckText(sale.getProduct().getTitle(), 18, Font.NORMAL));
////			header.add(new Chunk(new DottedLineSeparator()));
//				document.add(header);
//
//				totalPrice += (sale.getQuantity() * sale.getFixedPrice());
//				header = new Paragraph(
//						PdfFragments.setCheckText(
//								String.format("%d x %d = %d A", 
//										sale.getQuantity(), 
//										sale.getFixedPrice(), 
//										sale.getQuantity() * sale.getFixedPrice()),
//								18, Font.NORMAL));
//				header.setAlignment(Paragraph.ALIGN_RIGHT);
//				PdfFragments.addEmptyLine(header, 2);
//				document.add(header);
//			}
//
//			header = new Paragraph(new Chunk(dl));
//			header.add("\n");
//			document.add(header);
//			header = new Paragraph(PdfFragments.setCheckText("TOTAL: " + totalPrice, 25, Font.BOLD));
//			header.setAlignment(Paragraph.ALIGN_RIGHT);
//			document.add(header);
//			header = new Paragraph(new Chunk(dl));
//			header.add("\n");
//			document.add(header);
//
//			header = new Paragraph(PdfFragments.setCheckText(
//					session.getEndedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), 15, Font.NORMAL));
//			header.setAlignment(Paragraph.ALIGN_RIGHT);
//			PdfFragments.addEmptyLine(header, 2);
//			document.add(header);
//
//			// qr code
//			BufferedImage bufIm = PdfFragments.generateQRCode(
//					String.format("time=%s&total=%d&check=%d",
//							session.getEndedAt().format(DateTimeFormatter.ISO_DATE_TIME), totalPrice, session.getId()),
//					"UTF-8", 200, 200);
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(bufIm, "png", baos);
//			Image qrCodeImage = Image.getInstance(baos.toByteArray());
//
//			qrCodeImage.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(qrCodeImage);
//
//			header = new Paragraph(PdfFragments.setCheckText("THANKS FOR SHOPPING WITH US!", 20, Font.BOLD));
//			header.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(header);
//
//			document.close();
//
//		} catch (Exception ex) {
//			throw new CantPrintCheckException("Error in print check");
//		}
//
//		return new ByteArrayInputStream(out.toByteArray());
//
//	}
//
//	public static ByteArrayInputStream buildSessionsWithTimeBordersPdfReport(User userAuth, List<Session> sessions) 
//			throws Exception {
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Document document = new Document();
//		document.setPageSize(PageSize.LEDGER);
//		
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//			
//			Paragraph paragraph = new Paragraph(PdfFragments
//					.setUbunuText("Statistics for sessions", 30, Font.NORMAL));
//			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(paragraph);
//			
//			
//
//			DottedLineSeparator dl = new DottedLineSeparator();
//			// cashier---------------
//			paragraph = new Paragraph(new Chunk(dl));
//			paragraph.add("\n");
//
//			paragraph.add(PdfFragments.setUbunuText("Senior cashier: " + userAuth.getFirstName() + " " + userAuth.getFirstName(), 15, Font.ITALIC));
//			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(paragraph);
//			paragraph = new Paragraph(new Chunk(dl));
//			PdfFragments.addEmptyLine(paragraph, 1);
//			document.add(paragraph);
//			// --------------
//			float [] pointColumnWidths = {15f, 50f, 40f, 150f,55f};
//			PdfPTable table = new PdfPTable(pointColumnWidths);
//			
//			
//			for(String h : PdfFragments.HEADERS_SESSION_TABLE) {
//		        PdfPCell c1 = new PdfPCell(new Phrase(PdfFragments.setUbunuText(h, 25, Font.BOLD)));
//		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		        c1.setPadding(10);
//		        table.addCell(c1);
//			}
//			table.setHeaderRows(1);
//			
//			Long totalGlobal = 0L;
//			
//			for(Session session : sessions) {
//				
//				Long totalLocal = 0L;
//				List<Sale> sales = session.getSales();
//				for(Sale sale : sales) {
//					totalLocal += (sale.getQuantity() * sale.getFixedPrice());
//				}
//				
//				table.addCell(PdfFragments.newCell(String.valueOf(session.getId())));
//				table.addCell(PdfFragments.newCell(session.getEndedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))));
//				table.addCell(PdfFragments.newCell(String.format("%s %s", session.getUser().getFirstName(), session.getUser().getLastName())));
//				table.addCell(PdfFragments.newCellList(sales));
//				table.addCell(PdfFragments.newCell(String.valueOf(totalLocal)));
//				
//				totalGlobal+=totalLocal;
//			}
//				
//				document.add(table);
//			
//
//				
//				paragraph = new Paragraph(PdfFragments.setUbunuText("PROCEEDS: " + String.valueOf(totalGlobal) + " UAH", 25, Font.BOLD));
//				paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
//				document.add(paragraph);	
//			
//			
//			document.close();
//			
//			
//		}catch (Exception ex) {
//			log.info("ERRRRRROR: {}", ex.getMessage());
//			throw new Exception();
////			throw new CantPrintCheckException("Error in print check");
//		}
//
//
//		return new ByteArrayInputStream(out.toByteArray());
//
//	}
//
//
//	public static ByteArrayInputStream buildProductSalesPdfReport(User userAuth, List<Sale> sales) throws CantPrintCheckException {
//		Product product = null;
//		if(sales.size() >= 1) {
//			product = sales.get(0).getProduct();
//		}else {
//			throw new CantPrintCheckException("Can't print doc");
//		}
//		
//		ByteArrayOutputStream out = new ByteArrayOutputStream();
//		Document document = new Document();
//		document.setPageSize(PageSize.LEDGER);
//		
//		try {
//			PdfWriter.getInstance(document, out);
//			document.open();
//			
//			Paragraph paragraph = new Paragraph(PdfFragments
//					.setUbunuText("Sales statistics for product:\n\"",  30, Font.NORMAL));
//			paragraph.add(PdfFragments.setUbunuText(product.getTitle()  + "\"", 
//					35, Font.BOLD));
//			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(paragraph);
//			
//			
//
//			DottedLineSeparator dl = new DottedLineSeparator();
//			// cashier---------------
//			paragraph = new Paragraph(new Chunk(dl));
//			paragraph.add("\n");
//
//			paragraph.add(PdfFragments.setUbunuText("Senior cashier: " + userAuth.getFirstName() + " " + userAuth.getFirstName(), 15, Font.ITALIC));
//			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
//			document.add(paragraph);
//			paragraph = new Paragraph(new Chunk(dl));
//			PdfFragments.addEmptyLine(paragraph, 1);
//			document.add(paragraph);
//			// --------------
//			float [] pointColumnWidths = {60f, 100f, 60f, 60f,60f};
//			PdfPTable table = new PdfPTable(pointColumnWidths);
//			
//			
//			for(String h : PdfFragments.HEADERS_SALES_TABLE) {
//		        PdfPCell c1 = new PdfPCell(new Phrase(PdfFragments.setUbunuText(h, 25, Font.BOLD)));
//		        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//		        c1.setPadding(10);
//		        table.addCell(c1);
//			}
//			table.setHeaderRows(1);
//			
//			Long totalPrice = 0L;
//			
//			for(Sale sale : sales) {
//				
//				table.addCell(PdfFragments.newCell(String.valueOf(sale.getSession().getId())));
//				table.addCell(PdfFragments.newCell(String.format("%s %s", 
//						sale.getSession().getUser().getFirstName(), 
//						sale.getSession().getUser().getLastName())));
//				table.addCell(PdfFragments.newCell(String.valueOf(sale.getQuantity())));
//				table.addCell(PdfFragments.newCell(String.valueOf(sale.getFixedPrice())));
//				table.addCell(PdfFragments.newCell(String.valueOf(sale.getQuantity() * sale.getFixedPrice())));
//				
//				totalPrice+=(sale.getQuantity() * sale.getFixedPrice());
//			}
//				
//				document.add(table);
//			
//
//				
//				paragraph = new Paragraph(PdfFragments.setUbunuText("PROCEEDS: " + String.valueOf(totalPrice) + " UAH", 25, Font.BOLD));
//				paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
//				document.add(paragraph);	
//			
//			
//			document.close();
//			
//			
//		}catch (Exception ex) {
//			log.info("ERRRRRROR: {}", ex.getMessage());
//			throw new CantPrintCheckException("Can't print doc");
////			throw new CantPrintCheckException("Error in print check");
//		}
//
//
//		return new ByteArrayInputStream(out.toByteArray());
//		
//		
//	}

}
