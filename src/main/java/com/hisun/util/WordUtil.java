package com.hisun.util;

import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;
import com.aspose.words.*;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


    private WordUtil() {}
    public static WordUtil newInstance() {
        if(util==null) {
            synchronized (WordUtil.class){
                if(util==null){
                     util = new WordUtil();
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

    public Document read(String file) throws Exception{
        InputStream stream = new FileInputStream(new File(file));
        Document doc = new Document(stream);
        stream.close();
        return doc;
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

        Map<String, String> result = new LinkedMap();
        InputStream sourceStream = new FileInputStream(new File(sourceWordPath));
        Document sourceDoc = new Document(sourceStream);

        InputStream templateStream = new FileInputStream(new File(tmplateWordPath));
        Document templateDoc = new Document(templateStream);
        Map<String, Integer> templateMap = this.generateTemplateMap(templateDoc);
        //解析Word,找出对应cell的值,形成数据字段与实际数据的映射
        NodeCollection cells = sourceDoc.getChildNodes(NodeType.CELL, true);
        //获取到空白行
        int blankRowSign = this.getBlankRowOverRangeSign(cells,templateMap);
        for (Iterator<String> it = templateMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            Integer value = templateMap.get(key);
            if (key.startsWith(imageSign)) {
                if(imageSaveDir!=null && imageSaveDir.length()>0) {
                    result.put(key, this.dealImageCell(sourceDoc, imageSaveDir));
                }
            } else if (key.startsWith(rangeSign)) {
                    this.dealRangeCell(cells,blankRowSign,key,value.intValue(),result);
            } else if (key.startsWith(listSign)) {
                    result.put(key, trim(cells.get(value.intValue()).getText()));
            } else {
                if(key.toLowerCase().contains("gzjlstr")) {
                    result.put(key, cells.get(value.intValue()).getText());
                }else {
                    result.put(key, trim(cells.get(value.intValue()).getText()));
                }
            }
        }
        sourceStream.close();
        templateStream.close();
        return result;
    }


    public Map<String, String> convertMapByTemplate(NodeCollection cells,Map<String, Integer> templateMap){
        Map<String, String> result = new LinkedMap();
        //获取到空白行
        int blankRowSign = this.getBlankRowOverRangeSign(cells,templateMap);
        for (Iterator<String> it = templateMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            Integer value = templateMap.get(key);
            if (key.startsWith(imageSign)) {

            } else if (key.startsWith(rangeSign)) {
                //如果是第一列,则标识为rowkey,如果rowkey对应的值为空,则当前行及以下的行都不在计算
                this.dealRangeCell(cells,blankRowSign,key,value.intValue(),result);
            } else if (key.startsWith(listSign)) {
                result.put(key, trim(cells.get(value.intValue()).getText()));
            } else {
                result.put(key, trim(cells.get(value.intValue()).getText()));
            }
        }
        return result;
    }

    public Map<String, String> convertMapByTemplate(String sourceWordPath, String tmplateWordPath) throws Exception {
        return this.convertMapByTemplate(sourceWordPath,tmplateWordPath,null);
    }


    private int getBlankRowOverRangeSign(NodeCollection cells, Map<String,Integer> templateMap){
        //通过第一列来判断当前行数据是否有效
        for (Iterator<String> it = templateMap.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            if(key.startsWith(rangeSign)) {
                Integer value = templateMap.get(key);
                int row = this.getRowCount(key);
                int col = this.getColCount(key);
                for (int i = 0; i < row; i++) {
                    Cell rangeCell = (Cell) cells.get(value.intValue() + i * col);
                    if (rangeCell != null) {
                        String rangeValue = cells.get(value.intValue() + i * col).getText();
                        rangeValue = trim(rangeValue);
                        if (rangeValue == null || rangeValue.equals("") == true) {
                            return i;
                        }
                    } else {
                        break;
                    }
                }
                break;
            }
        }
        return -1;
    }


    public String trim(String str) {
        if (str == null) {
            return "";
        } else {
            //去掉换行符
            str = str.replaceAll("[\\s\b\r)]*", "");
            str = str.replaceAll("[\u0007]*","");
            //去掉全角空格
            str = StringUtils.trim(str.replace((char) 12288, ' '));
            return str;
        }
    }


    public Map<String, Integer> generateTemplateMap(Document templateDoc) {
        //模板Word数据字段与位置映射
        Map<String, Integer> templateMap = new LinkedMap();
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


    private void dealRangeCell(NodeCollection cells, int blankRowSign,String key, int rangeIndex, Map<String, String> result) {
        if(blankRowSign!=0){//等于0代表第一行为空白,不作处理
            result.put(key + dot+"0", trim(cells.get(rangeIndex).getText()));
            int row = this.getRowCount(key);
            int col = this.getColCount(key);
            for (int i = 1; i <= row; i++) {
                //去掉空行
                if(blankRowSign>0 && i>=blankRowSign){
                    break;
                }
                Cell rangeCell = (Cell) cells.get(rangeIndex + i * col);
                if (rangeCell != null) {
                    String rangeValue = cells.get(rangeIndex + i * col).getText();
                    rangeValue = trim(rangeValue);
                    if (rangeValue != null && rangeValue.equals("") == false) {
                        result.put(key + dot + i, rangeValue);
                    } else {
                        result.put(key + dot + i, "");
                    }
                } else {
                    break;
                }
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
            String field = str.substring(str.indexOf(dataPrefix)+1,str.indexOf(datasuffix));
            field = field.substring(field.indexOf(dot)+1);
           return field;
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

//            WordUtil wordUtil = WordUtil.newInstance();
//            String wordPath = "/Users/zhouying/Desktop/湘西州/测试数据/5名册列表/2/2州直单位领导干部名册.docx";
//            String wordPathTemplate = "/Users/zhouying/Desktop/湘西州/测试数据/5名册列表/2/gbmca01.docx";
//
//            Document templateDoc = wordUtil.read(wordPathTemplate);
//            Map<String, Integer> templateMap = wordUtil.generateTemplateMap(templateDoc);
//
//            Document document = wordUtil.read(wordPath);
//            NodeCollection collection = document.getChildNodes(NodeType.TABLE, true);
//            for(Iterator<Table> tables = collection.iterator(); tables.hasNext();){
//                Table table = tables.next();
//                NodeCollection cells = table.getChildNodes(NodeType.CELL,true);
//               System.out.println(wordUtil.convertMapByTemplate(cells,templateMap));
//            }

        WordUtil wordUtil = WordUtil.newInstance();
        String wordPathTemplate = "/Users/zhouying/Documents/workspace/store/mca01/gbrmspb/gbrmspb.docx";
        String outWord =  "/Users/zhouying/Documents/workspace/store/mca01/gbrmspb/out.docx";
        Document templateDoc = wordUtil.read(wordPathTemplate);
        DocumentBuilder builder = new DocumentBuilder(templateDoc);

//        NodeCollection paragraphs = templateDoc.getChildNodes(NodeType.PARAGRAPH,true);
//        int index=0;
//        for(Iterator<Paragraph> iterator= paragraphs.iterator();iterator.hasNext();){
//            Paragraph paragraph = iterator.next();
//            String trimText = wordUtil.trim(paragraph.getText());
//            if (trimText.startsWith(dataPrefix) || trimText.startsWith(specialDataPrefix)) {
//               // System.out.println(trimText);
//                System.out.println("==="+index);
//                builder.moveToParagraph(index,0);
//                builder.;
//            }
//            index++;
//        }


        NodeCollection tables = templateDoc.getChildNodes(NodeType.TABLE,true);
        int tableIndex =0;
        for(Iterator<Table> iterator= tables.iterator();iterator.hasNext();){
            Table table = iterator.next();
            NodeCollection rows =  table.getChildNodes(NodeType.ROW,true);
            int rowIndex=0;
            for(Iterator<Row> rowIterator= rows.iterator();rowIterator.hasNext();){
                Row row = rowIterator.next();
                NodeCollection cells =  row.getChildNodes(NodeType.CELL,true);
                int colIndex =0;
                for(Iterator<Cell> cellIterator= cells.iterator();cellIterator.hasNext();){
                    Cell cell = cellIterator.next();
                    String trimText = wordUtil.trim(cell.getText());
                    if (trimText.startsWith(dataPrefix)) {
                        System.out.println(tableIndex+"-"+rowIndex+"-"+colIndex);
                        builder.moveToCell(tableIndex, rowIndex, colIndex, 0);
                        builder.write("");
                        templateDoc.getRange().replace(trimText,"",
                                new FindReplaceOptions(FindReplaceDirection.FORWARD));
                    }else if(trimText.startsWith(WordUtil.imageSign)){
                        System.out.println(tableIndex+"-"+rowIndex+"-"+colIndex);
                        builder.moveToCell(tableIndex, rowIndex, colIndex, 0);
                        builder.insertImage("/Users/zhouying/Documents/workspace/store/mca01/ffcb864cf4ff41e4b91249b67e59ee58.jpg",94,122);
                        templateDoc.getRange().replace(trimText,"",
                                new FindReplaceOptions(FindReplaceDirection.FORWARD));
                        System.out.println(trimText);
                    }
                    colIndex++;
                }
                rowIndex++;
            }
            tableIndex++;
        }


//        NodeCollection templateCells = templateDoc.getChildNodes(NodeType.CELL, true);
//        Cell cell = null;
//        //建立模板Word数据字段与cell位置的映射
//        for (int index = 0; index < templateCells.getCount(); index++) {
//            cell = (Cell) templateCells.get(index);
//
//            String trimText = wordUtil.trim(cell.getText());
//            if (trimText.startsWith(dataPrefix) || trimText.startsWith(specialDataPrefix)) {
//                //builder.moveto
//
//               // builder.moveToCell(int tableIndex, int rowIndex, int columnIndex, int characterIndex);
//               // builder.write("dsfsfsfdsfdsfsf测试");
//
//            }
//        }

        templateDoc.save(outWord);

    }

}
