package com.savit.mycassa.util.pdf;

import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.savit.mycassa.entity.product.Sale;
import com.savit.mycassa.util.exception.CantPrintCheckException;

public class PdfFragments {
	
	
	private static String FONT_CHECK = "12540.ttf";
	private static String FONT_UBUNTGU = "ubuntuFont.ttf";

	static final String HEADERS_SESSION_TABLE[] = new String[]{
			"№", 
			"Ended at", 
			"Cashier",
			"Products",
			"Total Price"};
	
	static final String HEADERS_SALES_TABLE[] = new String[]{
			"Session №", 
			"Cashier", 
			"Pieces",//FIXME
			"Unit price",
			"Total, UAH"};
	
	
	
	static BufferedImage generateQRCode(String data, String charset, int height, int width)
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

	
	
	static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	 static Chunk setCheckText(String content, float size, int style) {
		try {
			BaseFont bf = BaseFont.createFont(FONT_CHECK, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			return new Chunk(content, new Font(bf, size, style));
		} catch (Exception ex) {
			return new Chunk(content);
		}
	}
	
	static Chunk setUbunuText(String content, float size, int style) {
		try {
			BaseFont bf = BaseFont.createFont(FONT_UBUNTGU, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			return new Chunk(content, new Font(bf, size, style));
		} catch (Exception ex) {
			return new Chunk(content);
		}
	}
	
	
	static PdfPCell newCell(String phrase) {
		PdfPCell c1 = new PdfPCell(new Phrase(setUbunuText(phrase, 24, Font.NORMAL)));			
		c1.setPadding(10);
		return c1;
	}
	
	static PdfPCell newCellList(List<Sale> sales) {
		StringBuilder sb = new StringBuilder();
		
		for(Sale sale : sales) {
			sb.append("- " + sale.getProduct().getTitle() + "\n");
		}
		PdfPCell c1 = new PdfPCell(new Phrase(setUbunuText(sb.toString(), 24, Font.NORMAL)));
		c1.setPadding(10);
		return c1;
	}
	
	
	
}
