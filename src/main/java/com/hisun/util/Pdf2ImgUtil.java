package com.hisun.util;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouying on 2017/9/8.
 */
public class Pdf2ImgUtil {

    private final static Logger logger = Logger.getLogger(Pdf2ImgUtil.class);

    public static void toImg(String pdfPath, String imgPath) throws IOException {

        long start = System.currentTimeMillis();
        File pdfFile = new File(pdfPath);
        PDDocument document = PDDocument.load(pdfFile, (String) null);
        int size = document.getNumberOfPages();
        List<BufferedImage> piclist = new ArrayList<BufferedImage>();
        for (int i = 0; i < size; i++) {
            BufferedImage image = new PDFRenderer(document).renderImageWithDPI(i, 130, ImageType.RGB);
            piclist.add(image);
        }
        document.close();
        yPic(piclist, imgPath);
        long end = System.currentTimeMillis();
        logger.info("Pdf converted times :"+(end - start));
    }


    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     *
     * @param piclist 文件流数组
     * @param outPath 输出路径
     */
    public static void yPic(List<BufferedImage> piclist, String outPath) throws IOException{// 纵向处理图片
        if (piclist == null || piclist.size() <= 0) {
            System.out.println("图片数组为空!");
            return;
        }
        int height = 0, // 总高度
                width = 0, // 总宽度
                _height = 0, // 临时的高度 , 或保存偏移高度
                __height = 0, // 临时的高度，主要保存每个高度
                picNum = piclist.size();// 图片的数量
        File fileImg = null; // 保存读取出的图片
        int[] heightArray = new int[picNum]; // 保存每个文件的高度
        BufferedImage buffer = null; // 保存图片流
        List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
        int[] _imgRGB; // 保存一张图片中的RGB数据
        for (int i = 0; i < picNum; i++) {
            buffer = piclist.get(i);
            heightArray[i] = _height = buffer.getHeight();// 图片高度
            if (i == 0) {
                width = buffer.getWidth();// 图片宽度
            }
            height += _height; // 获取总高度
            _imgRGB = new int[width * _height];// 从图片中读取RGB
            _imgRGB = buffer
                    .getRGB(0, 0, width, _height, _imgRGB, 0, width);
            imgRGB.add(_imgRGB);
        }
        _height = 0; // 设置偏移高度为0
        // 生成新图片
        BufferedImage imageResult = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_BGR);
        for (int i = 0; i < picNum; i++) {
            __height = heightArray[i];
            if (i != 0)
                _height += __height; // 计算偏移高度
            imageResult.setRGB(0, _height, width, __height, imgRGB.get(i),
                    0, width); // 写入流中
        }
        File outFile = new File(outPath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(imageResult, "jpg", out);// 写图片
        byte[] b = out.toByteArray();
        FileOutputStream output = new FileOutputStream(outFile);
        output.write(b);
        out.close();
        output.close();
    }


    public static void main(String[] args) throws Exception {

        Pdf2ImgUtil p2i = new Pdf2ImgUtil();
        p2i.toImg("/Users/zhouying/Desktop/zzb-app-android/aa.pdf", "/Users/zhouying/Desktop/zzb-app-android/aa.png");

    }
}
