package com.hisun.util;

import com.aspose.words.*;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created by zhouying on 2017/9/9.
 */
public class WordUtil {

    private final static Logger logger = Logger.getLogger(WordUtil.class);

    private static WordUtil util = null;
    public static String dataPrefix = "[";
    public static String datasuffix = "]";
    public static String dot=".";
    public static String rangeRowColLink="*";
    public static String equals="=";
    public static String specialDataPrefix = "#";
    public static String imageSign = "#image";
    public static String listSign = "#list";
    public static String rangeSign = "#range";


    private WordUtil() {

    }

    public static WordUtil newInstance() {

        if(util==null) {
            synchronized (WordUtil.class){
                if(util==null){
                    WordUtil util = new WordUtil();
                    try {
                        util.init();
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        return util;
    }

    private static void init() throws Exception {
        InputStream is = WordUtil.class.getClassLoader().getResourceAsStream("aspose-license.xml");
        if (is == null) {
            throw new Exception("aspose-license.xml is not found.");
        }
        License aposeLic = new License();
        aposeLic.setLicense(is);
    }

    public java.util.List<byte[]> extractImages(String wordPath) throws Exception {
        java.util.List<byte[]> images = new ArrayList<byte[]>();
        // Open the stream. Read only access is enough for Aspose.Words to load a document.
        InputStream stream = new FileInputStream(new File(wordPath));
        // Load the entire document into memory.
        Document doc = new Document(stream);
        // You can close the stream now, it is no longer needed because the document is in memory.
        stream.close();
        return this.extractImages(doc);
    }

    public java.util.List<byte[]> extractImages(Document doc) throws Exception {
        java.util.List<byte[]> images = new ArrayList<byte[]>();
        NodeCollection<Shape> shapes = (NodeCollection<Shape>) doc.getChildNodes(NodeType.SHAPE, true);
        for (Shape shape : shapes) {
            if (shape.hasImage()) {
                images.add(shape.getImageData().getImageBytes());
            }
        }
        return images;
    }


    public java.util.List<String> extractImages(String wordPath, String saveDir) throws Exception {
        InputStream stream = new FileInputStream(new File(wordPath));
        Document doc = new Document(stream);
        stream.close();
        return this.extractImages(doc, saveDir);
    }

    public java.util.List<String> extractImages(Document doc, String saveDir) throws Exception {
        java.util.List<String> images = new ArrayList<String>();
        NodeCollection<Shape> shapes = (NodeCollection<Shape>) doc.getChildNodes(NodeType.SHAPE, true);
        int imageIndex = 0;
        for (Shape shape : shapes) {
            if (shape.hasImage()) {
                String imageFileName = UUIDUtil.getUUID() + dot+ imageIndex + FileFormatUtil.imageTypeToExtension(shape.getImageData().getImageType());
                shape.getImageData().save(saveDir + imageFileName);
                images.add(saveDir + imageFileName);
                imageIndex++;
            }
        }
        return images;
    }


    public Map<String, String> convertMapByTemplate(String sourceWordPath, String tmplateWordPath, String imageSaveDir) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        InputStream sourceStream = new FileInputStream(new File(sourceWordPath));
        Document sourceDoc = new Document(sourceStream);
        InputStream templateStream = new FileInputStream(new File(tmplateWordPath));
        Document templateDoc = new Document(templateStream);
        Map<String, Integer> templateMap = this.generateTemplateMap(templateDoc);
        //解析Word,找出对应cell的值,形成数据字段与实际数据的映射
        NodeCollection cells = sourceDoc.getChildNodes(NodeType.CELL, true);
        for (Iterator<String> it = templateMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            Integer value = templateMap.get(key);
            if (key.startsWith(imageSign)) {
                result.put(key, this.dealImageCell(sourceDoc, imageSaveDir));
            } else if (key.startsWith(rangeSign)) {
                this.dealRangeCell(cells,key,value.intValue(),result);

            } else if (key.startsWith(listSign)) {
                    result.put(key, trim(cells.get(value.intValue()).getText()));
            } else {
                result.put(key, trim(cells.get(value.intValue()).getText()));
            }

        }

        sourceStream.close();
        templateStream.close();
        return result;
    }



    public String trim(String str) {
        if (str == null) {
            return "";
        } else {
            return StringUtils.trim(str.replace((char) 12288, ' '));
        }
    }


    private Map<String, Integer> generateTemplateMap(Document templateDoc) {
        //模板Word数据字段与位置映射
        Map<String, Integer> templateMap = new HashMap<String, Integer>();
        //获取模板文档的所有数据表格
        NodeCollection templateCells = templateDoc.getChildNodes(NodeType.CELL, true);
        Cell cell = null;
        //建立模板Word数据字段与cell位置的映射
        for (int index = 0; index < templateCells.getCount(); index++) {
            cell = (Cell) templateCells.get(index);
            String trimText = trim(cell.getText());
            if (trimText.startsWith(dataPrefix) || trimText.startsWith(specialDataPrefix)) {
                templateMap.put(trimText, index);
            }
        }
        return templateMap;
    }


    private String dealImageCell(Document doc, String saveDir) throws Exception {
        List<String> list= this.extractImages(doc,saveDir);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return "";
    }


    private void dealRangeCell(NodeCollection cells, String key, int rangeIndex, Map<String, String> result) {
        result.put(key + dot+"0", cells.get(rangeIndex).getText());
        int row = this.getRowCount(key);
        int col = this.getColCount(key);
        for (int i = 1; i <= row; i++) {
            Cell rangeCell = (Cell) cells.get(rangeIndex + i * col);
            if (rangeCell != null) {
                String rangeValue = cells.get(rangeIndex + i * col).getText();
                if (rangeValue != null && trim(rangeValue).equals("") == false) {
                    result.put(key + dot + i, rangeValue);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }


    public static int getRowCount(String key){
        int beginIndex = key.indexOf(dot)+1;
        int endIndex = key.indexOf(rangeRowColLink);
        return Integer.valueOf(key.substring(beginIndex,endIndex)).intValue();
    }

    public static int getColCount(String key){
        int beginIndex = key.indexOf(rangeRowColLink)+1;
        int endIndex = key.indexOf(dataPrefix);
        return Integer.valueOf(key.substring(beginIndex,endIndex)).intValue();
    }



    public static String getSqlField(String str){
        if(str!=null && str.length()>0){
           return str.substring(str.indexOf(dataPrefix)+1,str.indexOf(datasuffix));
        }
        return "";
    }

    public static String getSqlMainTable(Map<String,String> dataMap){
        String str=null;
        for (Iterator<String> it = dataMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            if(key.startsWith(dataPrefix)){
                str = key;
                break;
            }
        }
        //如果都没有以dataPrefix开头的值,则此模板可能是多行表
        if(str==null){
            for (Iterator<String> it = dataMap.keySet().iterator(); it.hasNext(); ) {
                String key = it.next();
                if(key.startsWith(rangeSign)){
                    str = key.substring(key.indexOf(dataPrefix));
                    break;
                }
            }
        }
        if(str!=null && str.length()>0){
            return str.substring(str.indexOf(dataPrefix)+1,str.lastIndexOf(dot));
        }
        return str;
    }




    public static void main(String[] args) throws Exception {

        String wordPath = "/Users/zhouying/Desktop/zzb-app-android/dd.docx";
        String wordPathTemplate = "/Users/zhouying/Desktop/zzb-app-android/template.docx";
////        java.util.List<byte[]> images = WordUtil.newInstance().extractImages("/Users/zhouying/Desktop/zzb-app-android/dd.docx");
////        String imagePath = "/Users/zhouying/Desktop/zzb-app-android/wordutil.jpg";
////        if(images.size()>0){
////            File file = new File(imagePath);
////            FileOutputStream outputStream = new FileOutputStream(file);
////            outputStream.write(images.get(0));
////            outputStream.close();
////        }
//
//
////        System.out.println(WordUtil.newInstance()
////                        .extractImages("/Users/zhouying/Desktop/zzb-app-android/dd.docx",
////                                "/Users/zhouying/Desktop/zzb-app-android/"));
//
//        WordUtil.newInstance();
//        InputStream stream = new FileInputStream(new File(wordPath));
//        Document doc = new Document(stream);
//
//        InputStream templateStream = new FileInputStream(new File(wordPathTemplate));
//        Document templateDoc = new Document(templateStream);
//
//        //System.out.println(StringUtils.trim(doc.toString(SaveFormat.TEXT)));
////        System.out.println(doc.getText());
//        // System.out.println(doc.getChildNodes(NodeType.CELL,true).get(0).getText());
//
////        for (Paragraph para : (Iterable<Paragraph>) doc.getChildNodes(NodeType.PARAGRAPH, true)) {
////            // Check if this paragraph is formatted using the TOC result based styles. This is any style between TOC and TOC9.
////            System.out.println(para.getText());
////        }
//        Map<String, Integer> templateMap = new HashMap<String, Integer>();
//        Map<String, String> dataMap = new HashMap<String, String>();
//
//        NodeCollection templateCells = templateDoc.getChildNodes(NodeType.CELL, true);
//        Cell cell = null;
//        // for (Cell cell : (Iterable<Cell>) doc.getChildNodes(NodeType.CELL, true)) {
//        for (int index = 0; index < templateCells.getCount(); index++) {
//            // Check if this paragraph is formatted using the TOC result based styles. This is any style between TOC and TOC9.
//
//            // Node node = cells.get(index);
//
//
//            cell = (Cell) templateCells.get(index);
//
//            // System.out.println("=="+cell.getText());
//            String trimText = StringUtils.trim(cell.getText());
//            if (trimText.startsWith("[") || trimText.startsWith("#")) {
//                templateMap.put(trimText, index);
//            }
//
//
////            if (para.getParagraphFormat().getStyle().getStyleIdentifier() >= StyleIdentifier.TOC_1 && para.getParagraphFormat().getStyle().getStyleIdentifier() <= StyleIdentifier.TOC_9) {
////                // Get the first tab used in this paragraph, this should be the tab used to align the page numbers.
////                TabStop tab = para.getParagraphFormat().getTabStops().get(0);
////                // Remove the old tab from the collection.
////                para.getParagraphFormat().getTabStops().removeByPosition(tab.getPosition());
////                // Insert a new tab using the same properties but at a modified position.
////                // We could also change the separators used (dots) by passing a different Leader type
////                para.getParagraphFormat().getTabStops().add(tab.getPosition() - 50, tab.getAlignment(), tab.getLeader());
////            }
//        }
//
//        // System.out.println(templateMap.keySet());
//        NodeCollection cells = doc.getChildNodes(NodeType.CELL, true);
//
//        if (templateMap.size() > 0)
//
//
//            for (Iterator<String> it = dataMap.keySet().iterator(); it.hasNext(); ) {
//                String key = it.next();
//                System.out.println(key + "=" + dataMap.get(key));
//            }
//
//
//        stream.close();
//        templateStream.close();

//       // System.out.println(WordUtil.newInstance().getColCount("#range.10*5["));
//        WordUtil wordUtil  = WordUtil.newInstance();
//        Map<String,String> dataMap =wordUtil.convertMapByTemplate(wordPath,wordPathTemplate,"/Users/zhouying/Desktop/zzb-app-android/");
//
//        for (Iterator<String> it = dataMap.keySet().iterator(); it.hasNext(); ) {
//                String key = it.next();
//                System.out.println(key + "=" + dataMap.get(key));
//                System.out.println(wordUtil.getSqlField(key));
//        }
//
//        System.out.println("==="+wordUtil.genInsertSql(dataMap,"1"));
    }

}
