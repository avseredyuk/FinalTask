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

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CheckBuilder{
	
	private static Document document = new Document();
	private static ByteArrayOutputStream out = new ByteArrayOutputStream();


	private static BaseFont bf;
	private static String FONT = "12540.ttf";

	private CheckBuilder() {
		try {

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private static Chunk setText(String content, float size, int style) {
		BaseFont bf;
		try {
			bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			return new Chunk(content, new Font(bf, size, style));
		} catch (Exception ex) {
			return null;
		}
	}

	public static ByteArrayInputStream buildSessionPDFCheck(Session session) {

		List<Sale> sales = session.getSales();

		User user = session.getUser();
		try {
			PdfWriter.getInstance(document, out);
			document.open();

			// welcome
			Paragraph header = new Paragraph(setText("WELCOME", 20, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);

			// check
			header = new Paragraph(setText("SALES CHECK №" + session.getId(), 13, Font.NORMAL));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);

			DottedLineSeparator dl = new DottedLineSeparator();
			// cashier---------------
			header = new Paragraph(new Chunk(dl));
			header.add("\n");

			header.add(setText("Cashier: " + user.getFirstName() + " " + user.getFirstName(), 15, Font.ITALIC));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);
			header = new Paragraph(new Chunk(dl));
			addEmptyLine(header, 1);
			document.add(header);
			// ---------------

			long totalPrice = 0L;
			for (Sale sale : sales) {
				header = new Paragraph(setText(sale.getProduct().getTitle(), 18, Font.NORMAL));
//			header.add(new Chunk(new DottedLineSeparator()));
				document.add(header);

				totalPrice += (sale.getQuantity() * sale.getProduct().getCost());
				header = new Paragraph(setText(
						String.valueOf(sale.getQuantity()) + " x " + String.valueOf(sale.getProduct().getCost()) + " = "
								+ String.valueOf(sale.getQuantity() * sale.getProduct().getCost() + " A"),
						18, Font.NORMAL));
				header.setAlignment(Paragraph.ALIGN_RIGHT);
				addEmptyLine(header, 2);
				document.add(header);
			}

			header = new Paragraph(new Chunk(dl));
			header.add("\n");
			document.add(header);
			header = new Paragraph(setText("TOTAL: " + String.valueOf(totalPrice), 25, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_RIGHT);
			document.add(header);
			header = new Paragraph(new Chunk(dl));
			header.add("\n");
			document.add(header);

			header = new Paragraph(setText(session.getEndedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
					15, Font.NORMAL));
			header.setAlignment(Paragraph.ALIGN_RIGHT);
			addEmptyLine(header, 2);
			document.add(header);

			// qr code
			BufferedImage bufIm = generateQRCode(String.format("time=%s&total=%d&check=%d",
					session.getEndedAt().format(DateTimeFormatter.ISO_DATE_TIME), totalPrice, session.getId()), "UTF-8",
					200, 200);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufIm, "png", baos);
			Image qrCodeImage = Image.getInstance(baos.toByteArray());

			qrCodeImage.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(qrCodeImage);
			
			header = new Paragraph(setText("THANKS FOR SHOPPING WITH US!", 20, Font.BOLD));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			document.add(header);
		
			
			document.close();

		} catch (Exception ex) {
			log.error("Error occurred: {0}", ex);
		}
		
		return new ByteArrayInputStream(out.toByteArray());

	}

	private static BufferedImage generateQRCode(String data, String charset, int height, int width){

		com.google.zxing.common.BitMatrix bitMatrix;
		try {
			bitMatrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
					BarcodeFormat.QR_CODE, width, height);
			return MatrixToImageWriter.toBufferedImage(bitMatrix);
		} catch (UnsupportedEncodingException | com.google.zxing.WriterException e) {
			return null; //FIXME
		}

	}

}
