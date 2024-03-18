package zzjz.util;


import java.util.Random;

/**
 * @author 房桂堂
 * @version 2016/6/14 14:26
 * @ClassName: NumberUtil
 * @Description: 数值常用方法
 */
public class NumberUtil {

    /**
     * 生成指定范围N个不重复的数.
     *
     * @param max 指定范围最大值
     * @param n   随机数个数
     * @Author 房桂堂
     * @Date 2016/6/14 14:33
     * @return Integer[]
     */
    public static Integer[] randomCommon(int max, int n) {
        if (n < 1) {
            return null;
        }
        Integer[] result = new Integer[n];
        Random random = new Random();
        int count = 0;
        max += 1;
        while (count < n) {
            int num = random.nextInt(max); //nextInt(m)范围是[0,m) 要+1
            boolean flag = true;
            for (int j = 0; j < n; j++) {
                if (result[j] == null) {
                    break;
                } else if (num == result[j]) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                result[count] = num;
                count++;
            }
        }
        return result;
    }


}
