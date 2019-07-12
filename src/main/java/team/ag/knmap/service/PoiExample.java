package team.ag.knmap.service;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.xslf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @Author weitangzhao
 **/
@Component
public class PoiExample {
    static final String outPath = "D:\\PPT";

    public void exchange(String path) throws IOException {
        File file=new File(path);

        XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(file));

        Dimension pgsize = ppt.getPageSize();
        XSLFSlide[] slide = ppt.getSlides().toArray(new XSLFSlide[0]);

        FileOutputStream out = null;

        for (int i = 0; i < slide.length; i++) {
            BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = img.createGraphics();

            //clear the drawing area
            graphics.setPaint(Color.white);
            graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

            //render
            slide[i].draw(graphics);

            out = new FileOutputStream("ppt_image"+i+".png");
            javax.imageio.ImageIO.write(img, "png", out);
            ppt.write(out);
        }



        System.out.println("Image successfully created");
        out.close();

    }

    public static List<String> converPPTtoImage(String orignalPPTFileName, String targetImageFileDir, String imageFormatNameString, int times) {
        List<String> imgList = new ArrayList<>();
        List<String> imgNamesList = new ArrayList<String>();// PPT转成图片后所有名称集合
        FileInputStream orignalPPTFileInputStream = null;
        FileOutputStream orignalPPTFileOutStream = null;
        HSLFSlideShow oneHSLFSlideShow = null;
        //创建文件夹
        createDirIfNotExist(targetImageFileDir);
        try {
            try {
                orignalPPTFileInputStream = new FileInputStream(orignalPPTFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
            try {
                oneHSLFSlideShow = new HSLFSlideShow(orignalPPTFileInputStream);

            } catch (IOException e) {
                // e.printStackTrace();
               // return Collections.emptyList();
            }
            // 获取PPT每页的大小（宽和高度）
            Dimension onePPTPageSize = oneHSLFSlideShow.getPageSize();
            // 获得PPT文件中的所有的PPT页面（获得每一张幻灯片）,并转为一张张的播放片
            List<HSLFSlide> pptPageSlideList = oneHSLFSlideShow.getSlides();
            // 下面循环的主要功能是实现对PPT文件中的每一张幻灯片进行转换和操作
            for (int i = 0; i < pptPageSlideList.size(); i++) {
                // 这几个循环只要是设置字体为宋体，防止中文乱码，
                List<List<HSLFTextParagraph>> oneTextParagraphs = pptPageSlideList.get(i).getTextParagraphs();
                for (List<HSLFTextParagraph> list : oneTextParagraphs) {
                    for (HSLFTextParagraph hslfTextParagraph : list) {
                        List<HSLFTextRun> HSLFTextRunList = hslfTextParagraph.getTextRuns();
                        for (int j = 0; j < HSLFTextRunList.size(); j++) {
                            // 如果PPT在WPS中保存过，则
                            // HSLFTextRunList.get(j).getFontSize();的值为0或者26040，
                            // 因此首先识别当前文本框内的字体尺寸是否为0或者大于26040，则设置默认的字体尺寸。

                            // 设置字体大小
                            Double size = HSLFTextRunList.get(j).getFontSize();
                            if ((size <= 0) || (size >= 26040)) {
                                HSLFTextRunList.get(j).setFontSize(20.0);
                            }
                            // 设置字体样式为宋体
                            // String
                            // family=HSLFTextRunList.get(j).getFontFamily();
                            HSLFTextRunList.get(j).setFontFamily("宋体");

                        }
                    }

                }
                // 创建BufferedImage对象，图像的尺寸为原来的每页的尺寸*倍数times
                BufferedImage oneBufferedImage = new BufferedImage(onePPTPageSize.width * times,
                        onePPTPageSize.height * times, BufferedImage.TYPE_INT_RGB);
                Graphics2D oneGraphics2D = oneBufferedImage.createGraphics();
                // 设置转换后的图片背景色为白色
                oneGraphics2D.setPaint(Color.white);
                oneGraphics2D.scale(times, times);// 将图片放大times倍
                oneGraphics2D
                        .fill(new Rectangle2D.Float(0, 0, onePPTPageSize.width * times, onePPTPageSize.height * times));
                pptPageSlideList.get(i).draw(oneGraphics2D);
                // 设置图片的存放路径和图片格式，注意生成的图片路径为绝对路径，最终获得各个图像文件所对应的输出流对象
                try {
                    String imgName = (i + 1) + "_" + UUID.randomUUID().toString() + "." + imageFormatNameString;
                    imgNamesList.add(imgName);// 将图片名称添加的集合中
                    imgList.add(imgName);
                    orignalPPTFileOutStream = new FileOutputStream(targetImageFileDir + imgName);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }
                // 转换后的图片文件保存的指定的目录中
                try {
                    ImageIO.write(oneBufferedImage, imageFormatNameString, orignalPPTFileOutStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    return Collections.emptyList();
                }

            }

        } finally {
            try {
                if (orignalPPTFileInputStream != null) {
                    orignalPPTFileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (orignalPPTFileOutStream != null) {
                    orignalPPTFileOutStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imgList;
    }

    /**
     * 检查是否为ppt文件
     * @param file
     * @return
     */
    public static boolean checkIsPPTFile(File file) {
        boolean isppt = false;
        String filename = file.getName();
        String suffixname = null;
        if (filename != null && filename.indexOf(".") != -1) {
            suffixname = filename.substring(filename.lastIndexOf("."));
            if (suffixname.equals(".ppt") || suffixname.equals(".pptx")) {
                isppt = true;
            }
            return isppt;
        } else {
            return isppt;
        }
    }

    /**
     * 创建文件如果路径不存在则创建对应的文件夹
     * @param file
     * @return
     */
    public static File createDirIfNotExist(String file) {
        File fileDir = new File(file);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }

    public static boolean doPPT2007toImage(File pptFile,File imgFile,List<String> list) {


        FileInputStream is = null ;


        try {

            is = new FileInputStream(pptFile);

            XMLSlideShow xmlSlideShow = new XMLSlideShow(is);

            is.close();

            // 获取大小
            Dimension pgsize = xmlSlideShow.getPageSize();

            // 获取幻灯片
            List<XSLFSlide> slides = xmlSlideShow.getSlides();



            for (int i = 0 ; i < slides.size() ; i++) {


                // 解决乱码问题
                XSLFShape[] shapes = slides.get(i).getShapes().toArray(new XSLFShape[0]);
                for (XSLFShape shape : shapes) {

                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape sh = (XSLFTextShape) shape;
                        List<XSLFTextParagraph> textParagraphs = sh.getTextParagraphs();

                        for (XSLFTextParagraph xslfTextParagraph : textParagraphs) {
                            List<XSLFTextRun> textRuns = xslfTextParagraph.getTextRuns();
                            for (XSLFTextRun xslfTextRun : textRuns) {
                                xslfTextRun.setFontFamily("宋体");
                            }
                        }
                    }
                }


                //根据幻灯片大小生成图片
                BufferedImage img = new BufferedImage(pgsize.width,pgsize.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = img.createGraphics();

                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,pgsize.height));

                // 最核心的代码
                slides.get(i).draw(graphics);

                //图片将要存放的路径
                String absolutePath = imgFile.getAbsolutePath()+"/"+ (i + 1) + ".jpeg";
                File jpegFile = new File(absolutePath);
                // 图片路径存放
                list.add((i + 1) + ".jpeg");

                //如果图片存在，则不再生成
                if (jpegFile.exists()) {
                    continue;
                }
                // 这里设置图片的存放路径和图片的格式(jpeg,png,bmp等等),注意生成文件路径
                FileOutputStream out = new FileOutputStream(jpegFile);

                // 写入到图片中去
                ImageIO.write(img, "jpeg", out);

                out.close();

            }


            System.out.println("PPT转换成图片 成功！");

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("PPT转换成图片 发生异常！");
        }


        return false;

    }

}
