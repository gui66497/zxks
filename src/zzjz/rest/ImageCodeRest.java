package zzjz.rest;

import zzjz.bean.BaseResponse;
import zzjz.bean.ResultCode;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuxiaoxiao
 * @version 2016/6/2 14:03
 * @ClassName: ImageCodeRest
 * @Description: 验证码相关操作接口类
 */
@Component
@Path("/imageCode")
public class ImageCodeRest {
    @Context
    ServletContext context;

    @Context
    HttpServletRequest request;

    @Context
    HttpServletResponse httpServletResponse;

    private static final int NUM1 = 220;

    private static final int NUM2 = 250;

    private static final int NUM3 = 25;

    private static final int NUM4 = 160;

    private static final int NUM5 = 155;

    private static final int NUM6 = 40;

    private static final int NUM7 = 4;

    private static final int NUM8 = 50;

    private static final int NUM9 = 200;

    private static final int NUM10 = 20;

    private static final int NUM11 = 110;

    private static final int NUM12 = 23;

    private static final int NUM13 = 30;

    private static final int NUM14 = 10;

    private static final int NUM15 = 100;

    /**
     * 获取验证码.
     *
     * @param headers headers对象
     * @return 验证码列表实体
     * @throws IOException IO异常
     */
    @GET
    @Path("/execute")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse<String> imageExecute(@Context HttpHeaders headers) throws IOException {
        BaseResponse<String> response = new BaseResponse<String>();
        //设置页面不缓存
        httpServletResponse.setHeader("Pragma", "No-cache");
        httpServletResponse.setHeader("Cache-control", "no-cache");
        httpServletResponse.setHeader("Expires", "0");
        //在内存中创建图像
        final int width = 145;
        final int height = 38;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取图形上下文
        Graphics g = image.getGraphics();
        //生成随机类
        Random random = new Random();
        //设定背景色
        g.setColor(getRandColor(NUM1, NUM2));
        g.fillRect(0, 0, width, height);
        //设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, NUM3));
        //画边框
        g.draw3DRect(0, 0, width - 1, height - 1, true);
        //随机产生155条干扰线，使图像中的认证码不易被其他程序探测到
        g.setColor(getRandColor(NUM4, NUM9));
        for (int i = 0; i < NUM5; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(NUM6);
            int y1 = random.nextInt(NUM6);
            g.drawLine(x, y, x + x1, y + y1);
        }
        //取随机产生的认证码（6位数字）
        StringBuffer sRand = new StringBuffer();
        String s = "012345678901234567890123456789ABCDEFGHIJKLMNOPQRSTU" +
                "VWXYZ012345678901234567890123456789ABCDEFGHIGKLMNOPQRSTUVWXYZ";
        for (int i = 0; i < NUM7; i++) {
            char rand = s.charAt(random.nextInt(s.length()));
            sRand.append(rand);
            //将认证码显示到图像中
            g.setColor(new Color(NUM8 + random.nextInt(NUM9),
                    NUM10 + random.nextInt(NUM11), NUM10 + random.nextInt(NUM11)));
            g.drawString(String.valueOf(rand), NUM12 * i + NUM12, NUM13);
        }
        g.drawOval(NUM14, NUM14, NUM15, NUM10);
        System.out.print("<<<<" + sRand.toString());
        //将验证码存入session中
        //request.getSession().setAttribute("rand",sRand);
        //图像生成
        g.dispose();
        long currentTime = System.currentTimeMillis();
        String realPath = request.getSession().getServletContext().getRealPath("/");
        String randomCodeImgPath = realPath + "images/" + currentTime + "" + ".jpeg";
        //File tmpFile = new File(randomCodeImgPath);
        try {
            //输出图像到页面
            //ImageIO.write(image, "JPEG", output);
            ImageIO.write(image, "JPEG", new File(randomCodeImgPath));

        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setResultCode(ResultCode.SUCCESS);
        response.setMessage(sRand.toString());
        List<String> imageData = new ArrayList<String>();
        imageData.add("/zxks/images/" + currentTime + ".jpeg");
        imageData.add(sRand.toString());
        response.setData(imageData);
        return response;
    }

    //生成随机颜色
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        final int num = 255;
        if (fc > num) {
            fc = num;
        }
        if (bc > num) {
            bc = num;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);

    }

}
