package com.savit.mycassa.util.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.savit.mycassa.entity.session.Session;
import com.savit.mycassa.util.exception.CantPrintCheckException;

public class PdfElements {

	private static final DottedLineSeparator LINE_SEPARATOR = new DottedLineSeparator();

	static Paragraph getTextLine(String content, int size, int style, int alignment) {
		Paragraph p = new Paragraph(new Chunk(content, new Font(CustomBaseFont.CHECK_FONT.getBaseFont(), size, style)));
		p.setAlignment(alignment);
		return p;

	}

	static Paragraph getSeparatedTextLine(String content, int size, int style, int alignment) {
		Paragraph p = new Paragraph(new Chunk(LINE_SEPARATOR));
		p.add("\n");
		p.add(getTextLine(content, size, style, alignment));
		p.add("\n");
		p.add(new Chunk(LINE_SEPARATOR));
		p.add(addEmptyLine(1));
		return p;
	}

	
	
	
	static Paragraph addEmptyLine(int number) {
		Paragraph paragraph = new Paragraph();
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(""));
		}
		return paragraph;
	}

	static Element getEntryLine(String key, String value) {
		Paragraph p = PdfElements.getTextLine(key, 18, Font.NORMAL, Paragraph.ALIGN_LEFT);
		p.add("\n");
		p.add(PdfElements.getTextLine(value, 18, Font.NORMAL, Paragraph.ALIGN_RIGHT));
		p.add(addEmptyLine(1));
		return p;
	}
	
	static Image generateQRCode(String data)
			throws CantPrintCheckException, IOException, BadElementException {

		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(new String(data.getBytes("UTF-8"), "UTF-8"),
					BarcodeFormat.QR_CODE, 200, 200);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "png", baos);
			Image qrCodeImage = Image.getInstance(baos.toByteArray());
			qrCodeImage.setAlignment(Paragraph.ALIGN_CENTER);
			return qrCodeImage;
		} catch (UnsupportedEncodingException | com.google.zxing.WriterException e) {
			throw new CantPrintCheckException("Error in print check");
		}

	}

	public static String formatDate(LocalDateTime startedAt) {
		return startedAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
	}

}
