package com.hisun.util;

import com.aspose.cells.*;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * Created by zhouying on 2017/11/18.
 */
public class ExcellUtil {


    private final static Logger logger = Logger.getLogger(ExcellUtil.class);

    private static ExcellUtil util = null;



    public static ExcellUtil newInstance() {

        if(util==null) {
            synchronized (ExcellUtil.class){
                if(util==null){
                    util = new ExcellUtil();
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
        InputStream is = ExcellUtil.class.getClassLoader().getResourceAsStream("aspose-license.xml");
        if (is == null) {
            throw new Exception("aspose-license.xml is not found.");
        }
        License aposeLic = new License();
        aposeLic.setLicense(is);
    }


    public static void main(String[] args) throws Exception {


        ExcellUtil util = ExcellUtil.newInstance();

        Workbook workbook = new Workbook("/Users/zhouying/Desktop/1.xls");

        WorksheetCollection worksheets = workbook.getWorksheets();

        Worksheet sheet = worksheets.get(0);

        Cells cells = sheet.getCells();

        //Cell cell = cells.get("M_D_C03");


       // System.out.println("Cell value : " + cell.getStringValue());


       // cell.getValue();

        Range[] namedRanges = worksheets.getNamedRanges();

        for(Range range:namedRanges){
            System.out.println("Number of Named Ranges : " + range.getName());

            Object[] valueObjs = (Object[])range.getValue();

            for(Object obj: valueObjs){
                Object[] innerObjs = (Object[])obj;
                for( Object innerObj : innerObjs){
                    System.out.println(innerObj+";");
                }

            }



        }



    }


}
