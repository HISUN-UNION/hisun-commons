package com.hisun.util;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Created by zhouying on 2017/9/9.
 */
public class WordConvertUtil {


    private static WordConvertUtil util = null;


    private WordConvertUtil(){

    }

    public static WordConvertUtil newInstance(){

        WordConvertUtil util = new WordConvertUtil();
        try{
            util.init();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return util;
    }



    private final static Logger logger = Logger.getLogger(WordConvertUtil.class);

    private static void init() throws Exception{
        InputStream is = WordConvertUtil.class.getClassLoader().getResourceAsStream("aspose-license.xml");
        if (is==null){
            throw new Exception("aspose-license.xml is not found.");
        }
        License aposeLic = new License();
        aposeLic.setLicense(is);
    }

    public  void convert(String sourcepath,String targetpath,int fortmat) throws Exception{
        // Open the stream. Read only access is enough for Aspose.Words to load a document.
        InputStream stream = new FileInputStream(new File(sourcepath));
        // Load the entire document into memory.
        Document doc = new Document(stream);
        // You can close the stream now, it is no longer needed because the document is in memory.
        stream.close();
        //ExEnd:OpenFromStream
        // Convert the document to a different format and save to stream.
        ByteArrayOutputStream dstStream = new ByteArrayOutputStream();
        doc.save(dstStream,fortmat);
        FileOutputStream output = new FileOutputStream(new File(targetpath));
        output.write(dstStream.toByteArray());
        output.close();
    }

    public  void toPdf(String sourcepath,String targetpath) throws Exception{
        this.convert(sourcepath,targetpath, SaveFormat.PDF);
    }

    public  void toJpeg(String sourcepath,String targetpath) throws Exception{
        this.convert(sourcepath,targetpath, SaveFormat.JPEG);
    }

    public  void toHtml(String sourcepath,String targetpath) throws Exception{
        this.convert(sourcepath,targetpath, SaveFormat.HTML);
    }

    public static void main(String[] args) throws Exception {
//        String ext = FilenameUtils.getExtension("/Users/zhouying/Desktop/zzb-app-android/ee.docx");
//        String output = "";
//        if ("docx".equalsIgnoreCase(ext)) {
//            output = readDocxFile("/Users/zhouying/Desktop/zzb-app-android/ee.docx");
//        } else if ("doc".equalsIgnoreCase(ext)) {
//            output = readDocFile("/Users/zhouying/Desktop/zzb-app-android/ee.doc");
//        } else {
//            System.out.println("INVALID FILE TYPE. ONLY .doc and .docx are permitted.");
//        }
//        writePdfFile(output);

//          InputStream in = new FileInputStream(new File("/Users/zhouying/Desktop/zzb-app-android/ee.docx"));
//        OutputStream out = new FileOutputStream(new File("/Users/zhouying/Desktop/zzb-app-android/ee.pdf"));
//
//        WordConvertUtil util = new WordConvertUtil();
//        util.convert(in,out);

//
//        POIFSFileSystem fs = null;
//        Document document = new Document();
//
//        try {
//            System.out.println("Starting the test");
//            fs = new POIFSFileSystem(new FileInputStream("/Users/zhouying/Desktop/zzb-app-android/bb.doc"));
//
//            HWPFDocument doc = new HWPFDocument(fs);
//            WordExtractor we = new WordExtractor(doc);
//
//            OutputStream file = new FileOutputStream(new File("/Users/zhouying/Desktop/zzb-app-android/bb.pdf"));
//
//            PdfWriter writer = PdfWriter.getInstance(document, file);
//
//            Range range = doc.getRange();
//            document.open();
//            writer.setPageEmpty(true);
//            document.newPage();
//            writer.setPageEmpty(true);
//
//            String[] paragraphs = we.getParagraphText();
//            for (int i = 0; i < paragraphs.length; i++) {
//
//                org.apache.poi.hwpf.usermodel.Paragraph pr = range.getParagraph(i);
//                // CharacterRun run = pr.getCharacterRun(i);
//                // run.setBold(true);
//                // run.setCapitalized(true);
//                // run.setItalic(true);
//                paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
//                System.out.println("Length:" + paragraphs[i].length());
//                System.out.println("Paragraph" + i + ": " + paragraphs[i].toString());
//
//                // add the paragraph to the document
//                document.add(new Paragraph(paragraphs[i]));
//            }
//
//            System.out.println("Document testing completed");
//        } catch (Exception e) {
//            System.out.println("Exception during test");
//            e.printStackTrace();
//        } finally {
//            // close the document
//            document.close();
//        }


//        HWPFDocumentCore hwpfDocument = WordToFoUtils.loadDoc(new File("/Users/zhouying/Desktop/zzb-app-android/bb.doc"));
//        WordToFoConverter wordToFoConverter = new WordToFoConverter(XMLHelper.getDocumentBuilderFactory().newDocumentBuilder().newDocument());
//        wordToFoConverter.processDocument(hwpfDocument);
//
//
//
//        Document doc = wordToFoConverter.getDocument();
//        DOMSource domSource = new DOMSource(doc);
//        StreamResult streamResult = new StreamResult(new File("/Users/zhouying/Desktop/zzb-app-android/bb.fo"));
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer serializer = tf.newTransformer();
//        serializer.setOutputProperty("encoding", "UTF-8");
//        serializer.setOutputProperty("indent", "yes");
//        serializer.transform(domSource, streamResult);

//        HWPFDocumentCore wordDocument = AbstractWordUtils.loadDoc(new File("/Users/zhouying/Desktop/zzb-app-android/ee.doc"));
//        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(XMLHelper.getDocumentBuilderFactory().newDocumentBuilder().newDocument());
//        wordToHtmlConverter.processDocument(wordDocument);
//
//
//        Document doc = wordToHtmlConverter.getDocument();
//        DOMSource domSource = new DOMSource(doc);
//        StreamResult streamResult = new StreamResult(new File("/Users/zhouying/Desktop/zzb-app-android/ee.html"));
//        TransformerFactory tf = TransformerFactory.newInstance();
//        Transformer serializer = tf.newTransformer();
//        serializer.setOutputProperty("encoding", "UTF-8");
//        serializer.setOutputProperty("indent", "yes");
//        serializer.setOutputProperty("method", "html");
//        serializer.transform(domSource, streamResult);
////
//        WordConvertUtil.newInstance().toPdf("/Users/zhouying/Desktop/zzb-app-android/dd.docx",
//                "/Users/zhouying/Desktop/zzb-app-android/ee.pdf");
  //      WordConvertUtil.newInstance().toPdf("/Users/zhouying/Desktop/zzb-app-android/湘西州干部调整配备建议方案.docx",
//                "/Users/zhouying/Desktop/zzb-app-android/dd.pdf");

    //    System.out.println("Document loaded from stream and then saved successfully.");

       // Pdf2ImgUtil pdf2img = new Pdf2ImgUtil();
       // pdf2img.toImg("/Users/zhouying/Desktop/zzb-app-android/ee.pdf","/Users/zhouying/Desktop/zzb-app-android/ee.jpg");


}


}
