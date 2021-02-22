package com.savit.mycassa.util.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.savit.mycassa.entity.sale.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.shift.Shift;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.util.exception.CantPrintCheckException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckBuilder {



	public static ByteArrayInputStream buildSessionPDFCheck(Session session, List<Sale> sales, Long totalSessionAmount)
			throws CantPrintCheckException {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		User user = session.getUser();
		try {
			PdfWriter.getInstance(document, out);
			document.open();

			document.add(PdfElements.getTextLine("W E L C O M E", 20, Font.BOLD, Paragraph.ALIGN_CENTER));
			document.add(PdfElements.getTextLine(String.format("SALES CHECK №%d", session.getId()), 13, Font.NORMAL,
					Paragraph.ALIGN_CENTER));

			document.add(PdfElements.getSeparatedTextLine(
					String.format("Cashier: %s %s", user.getFirstName(), user.getLastName()), 15, Font.ITALIC,
					Paragraph.ALIGN_CENTER));

			for (Sale sale : sales) {
				document.add(PdfElements.getEntryLine(sale.getProduct().getTitle(), String.format("%d x %d = %d",
						sale.getQuantity(), sale.getFixedPrice(), sale.getQuantity() * sale.getFixedPrice())));
				document.add(PdfElements.addEmptyLine(2));
			}

			document.add(PdfElements.getSeparatedTextLine(String.format("TOTAL: %d", totalSessionAmount), 25, Font.BOLD, Paragraph.ALIGN_RIGHT));
			document.add(PdfElements.getTextLine(PdfElements.formatDate(session.getEndedAt()), 15, Font.NORMAL, Paragraph.ALIGN_RIGHT));
			document.add(PdfElements.addEmptyLine(2));
			document.add(PdfElements.generateQRCode(String.format("time=%s&total=%d&check=%d",
					session.getEndedAt().format(DateTimeFormatter.ISO_DATE_TIME), totalSessionAmount,
					session.getId())));
			document.add(
					PdfElements.getTextLine("THANKS FOR SHOPPING WITH US!", 20, Font.BOLD, Paragraph.ALIGN_CENTER));

			document.close();

		} catch (Exception ex) {
			throw new CantPrintCheckException();
		}

		return new ByteArrayInputStream(out.toByteArray());

	}
	
	
	public static ByteArrayInputStream buildXReport(Shift shift, List<Session> sessions, Long totalShiftAmount)
			throws CantPrintCheckException {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, out);
			document.open();
			document.add(PdfElements.getTextLine(String.format("Report-X for shift №%d", shift.getId()), 20, Font.BOLD,Paragraph.ALIGN_CENTER));
			document.add(PdfElements.getSeparatedTextLine(PdfElements.formatDate(LocalDateTime.now()), 15, Font.ITALIC, Paragraph.ALIGN_CENTER));
			document.add(PdfElements.getEntryLine("Shift opening time", PdfElements.formatDate(shift.getStartedAt())));
			document.add(PdfElements.getEntryLine("Printed checks", String.valueOf(sessions.size())));
			document.add(PdfElements.getEntryLine("Zero checks",
					String.valueOf(sessions.stream().filter(a -> a.getSales().isEmpty()).count())));
			document.add(PdfElements.getEntryLine("Sales", String.valueOf(totalShiftAmount)));

			document.close();
		} catch (Exception ex) {
			throw new CantPrintCheckException();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
	
	
	public static ByteArrayInputStream buildZReport(Shift shift, List<Session> sessions, Long totalShiftAmount, Long notZeroAmount)
			throws CantPrintCheckException {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, out);
			document.open();
			document.add(PdfElements.getTextLine(String.format("Report-Z for shift №%d", shift.getId()), 20, Font.BOLD,Paragraph.ALIGN_CENTER));
			document.add(PdfElements.getSeparatedTextLine(PdfElements.formatDate(LocalDateTime.now()), 15, Font.ITALIC, Paragraph.ALIGN_CENTER));
			document.add(PdfElements.getEntryLine("Shift opening time", PdfElements.formatDate(shift.getStartedAt())));
			document.add(PdfElements.getEntryLine("Shift closing time", PdfElements.formatDate(shift.getEndedAt())));
			document.add(PdfElements.getEntryLine("Printed checks", String.valueOf(sessions.size())));
			document.add(PdfElements.getEntryLine("Zero checks",
					String.valueOf(sessions.stream().filter(a -> a.getSales().isEmpty()).count())));
			document.add(PdfElements.getEntryLine("Sales", String.valueOf(totalShiftAmount)));

			document.add(PdfElements.getSeparatedTextLine(String.format("Nonzero amount: %d", notZeroAmount), 18, Font.NORMAL, Paragraph.ALIGN_CENTER));
			document.add(PdfElements.getTextLine("ENCASHMENT CARRIED OUT!", 20, Font.BOLD, Paragraph.ALIGN_CENTER));
			document.close();
		} catch (Exception ex) {
			throw new CantPrintCheckException();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
	

}
