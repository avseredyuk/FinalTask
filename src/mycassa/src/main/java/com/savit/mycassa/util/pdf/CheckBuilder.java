package com.savit.mycassa.util.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.qrcode.BitMatrix;
import com.itextpdf.text.pdf.qrcode.WriterException;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.entity.user.User;
import com.savit.mycassa.util.exception.CantPrintCheckException;
import com.savit.mycassa.util.exception.EmptySalesListException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckBuilder {

	private static String FONT = "12540.ttf";

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private static Chunk setCheckText(String content, float size, int style) {
		try {
			BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			return new Chunk(content, new Font(bf, size, style));
		} catch (Exception ex) {
			return new Chunk(content);
		}
	}

	public static ByteArrayInputStream buildSessionPDFCheck(Session session)
			throws CantPrintCheckException, EmptySalesListException {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		List<Sale> sales = session.getSales();

		if (sales.size() == 0) {
			throw new EmptySalesListException("Check is empty", session.getId()); // FIXME: move to service
		}

		User user = session.getUser();
		try {
			PdfWriter.getInstance(document, out);
			document.open();

			// welcome
			Paragraph header = new Paragraph(setCheckText("WELCOME", 20, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);

			// check
			header = new Paragraph(setCheckText("SALES CHECK №" + session.getId(), 13, Font.NORMAL));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);

			DottedLineSeparator dl = new DottedLineSeparator();
			// cashier---------------
			header = new Paragraph(new Chunk(dl));
			header.add("\n");

			header.add(setCheckText("Cashier: " + user.getFirstName() + " " + user.getFirstName(), 15, Font.ITALIC));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);
			header = new Paragraph(new Chunk(dl));
			addEmptyLine(header, 1);
			document.add(header);
			// ---------------

			long totalPrice = 0L;
			for (Sale sale : sales) {
				header = new Paragraph(setCheckText(sale.getProduct().getTitle(), 18, Font.NORMAL));
//			header.add(new Chunk(new DottedLineSeparator()));
				document.add(header);

				totalPrice += (sale.getQuantity() * sale.getFixedPrice());
				header = new Paragraph(
						setCheckText(
								String.valueOf(sale.getQuantity()) + " x " + String.valueOf(sale.getFixedPrice())
										+ " = " + String.valueOf(sale.getQuantity() * sale.getFixedPrice() + " A"),
								18, Font.NORMAL));
				header.setAlignment(Paragraph.ALIGN_RIGHT);
				addEmptyLine(header, 2);
				document.add(header);
			}

			header = new Paragraph(new Chunk(dl));
			header.add("\n");
			document.add(header);
			header = new Paragraph(setCheckText("TOTAL: " + String.valueOf(totalPrice), 25, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(header);
			header = new Paragraph(new Chunk(dl));
			header.add("\n");
			document.add(header);

			header = new Paragraph(setCheckText(
					session.getEndedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")), 15, Font.NORMAL));
			header.setAlignment(Paragraph.ALIGN_RIGHT);
			addEmptyLine(header, 2);
			document.add(header);

			// qr code
			BufferedImage bufIm = generateQRCode(
					String.format("time=%s&total=%d&check=%d",
							session.getEndedAt().format(DateTimeFormatter.ISO_DATE_TIME), totalPrice, session.getId()),
					"UTF-8", 200, 200);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufIm, "png", baos);
			Image qrCodeImage = Image.getInstance(baos.toByteArray());

			qrCodeImage.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(qrCodeImage);

			header = new Paragraph(setCheckText("THANKS FOR SHOPPING WITH US!", 20, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);

			document.close();

		} catch (Exception ex) {
			throw new CantPrintCheckException("Error in print check");
		}

		return new ByteArrayInputStream(out.toByteArray());

	}

	public static ByteArrayInputStream buildSessionsWithTimeBordersPdfReport(List<Session> sessions) {

		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, out);
			document.open();
			// welcome
			Paragraph header = new Paragraph(setCheckText("WELCOME", 20, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);
			for (Session session : sessions) {
				header = new Paragraph(setCheckText(session.getStartedAt() + " " + session.getEndedAt(), 18, Font.NORMAL));

				header.setAlignment(Paragraph.ALIGN_RIGHT);
				addEmptyLine(header, 2);
				document.add(header);
			}
			document.close();

		} catch (Exception ex) {
			log.error("EXCEPTION : {}", ex);
		}

		return new ByteArrayInputStream(out.toByteArray());

	}

	private static BufferedImage generateQRCode(String data, String charset, int height, int width)
			throws CantPrintCheckException {

		com.google.zxing.common.BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
					BarcodeFormat.QR_CODE, width, height);
			return MatrixToImageWriter.toBufferedImage(bitMatrix);
		} catch (UnsupportedEncodingException | com.google.zxing.WriterException e) {
			throw new CantPrintCheckException("Error in print check");
		}

	}

	public static ByteArrayInputStream buildProductSalesPdfReport(List<Sale> sales) {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			PdfWriter.getInstance(document, out);
			document.open();
			// welcome
			Paragraph header = new Paragraph(setCheckText("WELCOME", 20, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);
			for (Sale sale : sales) {
				header = new Paragraph(setCheckText(sale.getProduct().getTitle() + " " + sale.getQuantity(), 18, Font.NORMAL));

				header.setAlignment(Paragraph.ALIGN_RIGHT);
				addEmptyLine(header, 2);
				document.add(header);
			}
			document.close();

		} catch (Exception ex) {
			log.error("EXCEPTION : {}", ex);
		}

		return new ByteArrayInputStream(out.toByteArray());
	}

}
