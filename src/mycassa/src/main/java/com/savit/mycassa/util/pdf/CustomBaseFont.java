package com.savit.mycassa.util.pdf;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

public enum CustomBaseFont {
	
	UBUNTU_FONT("ubuntuFont.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED),
	CHECK_FONT("12540.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
	
	
	
	private BaseFont baseFont;

	
	private CustomBaseFont(String FontPath, String enc, boolean isEmbedded) {
		try {
			baseFont = BaseFont.createFont(FontPath, enc, isEmbedded);
		} catch (DocumentException | IOException e) {
			baseFont = null;
		}
	}
	
	public BaseFont getBaseFont() {
		return baseFont;
	}
}
