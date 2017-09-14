package com.hisun.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.FOSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.model.fields.FieldUpdater;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.services.client.ConversionException;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhouying on 2017/9/8.
 */
public class Docx2PdfUtil {

    public static void main(String[] args) throws Exception {

//        try {
//            int maxNum = 1;
//            InputStream is1 = new FileInputStream(new File("/Users/zhouying/Desktop/zzb-app-android/cc.xls"));
//            POIFSFileSystem fs1 = new POIFSFileSystem(is1);
//            HSSFWorkbook book1 = new HSSFWorkbook(fs1);
//            HSSFSheet sheet1;
//            for (int i = 0; i < book1.getNumberOfSheets(); i++) {
//                sheet1 = book1.getSheetAt(i);
//                for (int j = 0; j <= sheet1.getPhysicalNumberOfRows(); j++) {
//                    HSSFRow row = sheet1.getRow(j);
//                    if (null != row) {
//                        if (maxNum < row.getPhysicalNumberOfCells()) {
//                            maxNum = row.getPhysicalNumberOfCells();
//                        }
//                    }
//                }
//            }
//            maxNum += 1;
//            InputStream is = new FileInputStream(new File("/Users/zhouying/Desktop/zzb-app-android/cc.xls"));
//            Document doc = new Document();
//            doc.setPageSize(PageSize.A4);
//            PdfWriter.getInstance(doc, new FileOutputStream(new File("/Users/zhouying/Desktop/zzb-app-android/cc.pdf")));
//
//            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//            Font headFont = new Font(bf, 12, Font.BOLD);
//            Font font = new Font(bf, 10, Font.NORMAL);
//            doc.open();
//
//            POIFSFileSystem fs = new POIFSFileSystem(is);
//            HSSFWorkbook book = new HSSFWorkbook(fs);
//            HSSFSheet sheet;
//            for (int i = 0; i < book.getNumberOfSheets(); i++) {
//                sheet = book.getSheetAt(i);
//                doc.add(new Paragraph(sheet.getSheetName(), headFont));
//                PdfPTable table = new PdfPTable(maxNum);
//                table.setWidthPercentage(100);
//                table.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
//                for (int j = 0; j <= sheet.getPhysicalNumberOfRows(); j++) {
//                    PdfPCell cell = new PdfPCell();
//                    cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
//                    HSSFRow row = sheet.getRow(j);
//                    if (null != row) {
//                        int currentNum = row.getPhysicalNumberOfCells();
//                        for (int k = 0; k < maxNum; k++) {
//                            if (currentNum < maxNum) {
//                                String value = getCellFormatValue(row.getCell(k));
//                                cell.setPhrase(new Paragraph(value, font));
//                            } else {
//                                cell.setPhrase(new Paragraph(" ", font));
//                            }
//                            table.addCell(cell);
//                        }
//                    }
//                }
//                doc.add(table);
//            }
//            doc.close();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//    private static String getCellFormatValue(HSSFCell cell) {
//        String cellValue = "";
//        if (null != cell) {
//            switch(cell.getCellType()) {
//                case HSSFCell.CELL_TYPE_NUMERIC:
//                case HSSFCell.CELL_TYPE_FORMULA:
//                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                        Date date = cell.getDateCellValue();
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        cellValue = sdf.format(date);
//                    } else {
//                        DecimalFormat df = new DecimalFormat("########");
//                        cellValue = df.format(cell.getNumericCellValue());
//                    }
//                    break;
//                case HSSFCell.CELL_TYPE_STRING:
//                    cellValue = cell.getRichStringCellValue().getString();
//                    break;
//                default:
//                    cellValue = " ";
//            }
//        } else {
//            cellValue = "";
//        }
//        return cellValue;


        // Font regex (optional)
        // Set regex if you want to restrict to some defined subset of fonts
        // Here we have to do this before calling createContent,
        // since that discovers fonts
        String regex = null;
        // Windows:
        // String
        // regex=".*(calibri|camb|cour|arial|symb|times|Times|zapf).*";
        //regex=".*(calibri|camb|cour|arial|times|comic|georgia|impact|LSANS|pala|tahoma|trebuc|verdana|symbol|webdings|wingding).*";
        // Mac
         // regex=".*(Courier New|Arial|Times New Roman|Comic Sans|Georgia|Impact|Lucida Console|Lucida Sans Unicode|Palatino Linotype|Tahoma|Trebuchet|Verdana|Symbol|Webdings|Wingdings|MS Sans Serif|MS Serif).*";
        PhysicalFonts.setRegex(regex);

        String inputfilepath = "/Users/zhouying/Desktop/zzb-app-android/ee.docx";
        // Document loading (required)
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(new java.io.File(inputfilepath));



        // Refresh the values of DOCPROPERTY fields
        FieldUpdater updater = new FieldUpdater(wordMLPackage);
        updater.update(true);

        String outputfilepath = "/Users/zhouying/Desktop/zzb-app-android/ee.pdf";

        // All methods write to an output stream
        OutputStream os = new java.io.FileOutputStream(outputfilepath);


        if (!Docx4J.pdfViaFO()) {

            // Since 3.3.0, Plutext's PDF Converter is used by default

            System.out.println("Using Plutext's PDF Converter; add docx4j-export-fo if you don't want that");

            try {
                Docx4J.toPDF(wordMLPackage, os);
            } catch (Docx4JException e) {
                e.printStackTrace();
                // What did we write?
                IOUtils.closeQuietly(os);
                System.out.println(
                        FileUtils.readFileToString(new File(outputfilepath)));
                if (e.getCause() != null
                        && e.getCause() instanceof ConversionException) {

                    ConversionException ce = (ConversionException) e.getCause();
                    ce.printStackTrace();
                }
            }
            System.out.println("Saved: " + outputfilepath);

            return;
        }

        System.out.println("Attempting to use XSL FO");

        /**
         * Demo of PDF output.
         *
         * PDF output is via XSL FO.
         * First XSL FO is created, then FOP
         * is used to convert that to PDF.
         *
         * Don't worry if you get a class not
         * found warning relating to batik. It
         * doesn't matter.
         *
         * If you don't have logging configured,
         * your PDF will say "TO HIDE THESE MESSAGES,
         * TURN OFF debug level logging for
         * org.docx4j.convert.out.pdf.viaXSLFO".  The thinking is
         * that you need to be able to be warned if there
         * are things in your docx which the PDF output
         * doesn't support...
         *
         * docx4j used to also support creating
         * PDF via iText and via HTML. As of docx4j 2.5.0,
         * only viaXSLFO is supported.  The viaIText and
         * viaHTML source code can be found in src/docx4j-extras directory
         *
         */


		/*
		 * NOT WORKING?
		 *
		 * If you are getting:
		 *
		 *   "fo:layout-master-set" must be declared before "fo:page-sequence"
		 *
		 * please check:
		 *
		 * 1.  the jaxb-xslfo jar is on your classpath
		 *
		 * 2.  that there is no stack trace earlier in the logs
		 *
		 * 3.  your JVM has adequate memory, eg
		 *
		 *           -Xmx1G -XX:MaxPermSize=128m
		 *
		 */


        // Set up font mapper (optional)
        Mapper fontMapper = new IdentityPlusMapper();
        wordMLPackage.setFontMapper(fontMapper);
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体",PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑",PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体",PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体",PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体",PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("宋体扩展",PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋",PhysicalFonts.get("FangSong"));
        fontMapper.put("仿宋_GB2312",PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("幼圆",PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体",PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋",PhysicalFonts.get("STZhongsong"));


        // .. example of mapping font Times New Roman which doesn't have certain Arabic glyphs
        // eg Glyph "ي" (0x64a, afii57450) not available in font "TimesNewRomanPS-ItalicMT".
        // eg Glyph "ج" (0x62c, afii57420) not available in font "TimesNewRomanPS-ItalicMT".
        // to a font which does
//        PhysicalFont font
//                = PhysicalFonts.get("Arial Unicode MS");
        // make sure this is in your regex (if any)!!!
//		if (font!=null) {
//			fontMapper.put("Times New Roman", font);
//			fontMapper.put("Arial", font);
//		}
//		fontMapper.put("Libian SC Regular", PhysicalFonts.get("SimSun"));

        // FO exporter setup (required)
        // .. the FOSettings object


        FOSettings foSettings = Docx4J.createFOSettings();
        //foSettings.setf
        boolean saveFO = true;
        if (saveFO) {
            foSettings.setFoDumpFile(new java.io.File(inputfilepath + ".fo"));
            //foSettings.setFoDumpFile(new java.io.File("/Users/zhouying/Desktop/zzb-app-android/bb.fo"));
        }

        foSettings.setWmlPackage(wordMLPackage);

        // Document format:
        // The default implementation of the FORenderer that uses Apache Fop will output
        // a PDF document if nothing is passed via
        // foSettings.setApacheFopMime(apacheFopMime)
        // apacheFopMime can be any of the output formats defined in org.apache.fop.apps.MimeConstants eg org.apache.fop.apps.MimeConstants.MIME_FOP_IF or
        // FOSettings.INTERNAL_FO_MIME if you want the fo document as the result.
        //foSettings.setApacheFopMime(FOSettings.INTERNAL_FO_MIME);

        // Specify whether PDF export uses XSLT or not to create the FO
        // (XSLT takes longer, but is more complete).

        // Don't care what type of exporter you use
        Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

        // Prefer the exporter, that uses a xsl transformation
        // Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_XSL);

        // Prefer the exporter, that doesn't use a xsl transformation (= uses a visitor)
        // .. faster, but not yet at feature parity
        // Docx4J.toFO(foSettings, os, Docx4J.FLAG_EXPORT_PREFER_NONXSL);

        System.out.println("Saved: " + outputfilepath);

        Pdf2ImgUtil pdf2img = new Pdf2ImgUtil();
        pdf2img.toImg(outputfilepath,"/Users/zhouying/Desktop/zzb-app-android/ee.jpg");

        // Clean up, so any ObfuscatedFontPart temp files can be deleted
        if (wordMLPackage.getMainDocumentPart().getFontTablePart() != null) {
            wordMLPackage.getMainDocumentPart().getFontTablePart().deleteEmbeddedFontTempFiles();
        }
        // This would also do it, via finalize() methods
        updater = null;
        foSettings = null;
        wordMLPackage = null;

    }



}
