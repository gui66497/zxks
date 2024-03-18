package zzjz.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;

/**
 * @ClassName: NumberUtilTest
 * @Description: 常量类 测试
 * @Author 房桂堂
 * @Date: 2016/7/15 8:44
 */
public class NumberUtilTest {

    private NumberUtil numberUtil;
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testRandomCommon() throws Exception {
        Assert.assertEquals("参数错误", null, NumberUtil.randomCommon(0,0));

        Assert.assertEquals("参数错误", 3, NumberUtil.randomCommon(4,3).length);

        Assert.assertEquals("参数错误", 50, NumberUtil.randomCommon(100,50).length);
    }
}