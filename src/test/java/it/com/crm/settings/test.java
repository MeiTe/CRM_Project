package it.com.crm.settings;

import it.com.crm.utils.DateTimeUtil;
import it.com.crm.utils.MD5Util;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class test {


    @Test
    public static void main(String[] args) {
        /*Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        System.out.println(format);*/

        /*//失效时间
        String expireTime ="2020-05-18 14:23:14";
        //获取系统的时间
        String sysTime = DateTimeUtil.getSysTime();
        //进行时间的比较
        int i = expireTime.compareTo(sysTime);
        System.out.println(i);

        //账号是否封存
        String lockState = "0";
        if ("0".equals(lockState)){
            System.out.println("账号已经封存。");
        }
        //浏览器端的IP地址
        String ip="192.163.0.0";
        String allowIp="192.163.0.0,192.163.1,0";
        if (ip.contains(allowIp)){
            System.out.println("可以访问");
        }else {
            System.out.println("IP受限，不可以访问！");
        }

        //明文转成密文
        String md5 = MD5Util.getMD5(ip);
        System.out.println(md5);*/


        //找出两组数中相同的元素
        Integer[] array1 = {1,2,3,4,1,2,4,6,7,8,10,22,33};

        Integer[] array2 = {1,2,3,4,1,2,4,6,7,8,10,22,33,55,66,77,88,99};

        Set<Integer> sameElementSet = getIds(array1,array2);

        for(Integer i : sameElementSet) {

            System.out.println(i);

        }
    }

    public static Set<Integer> getIds(Integer[] a, Integer[] b){

        Set<Integer> same = new HashSet<Integer>();  //用来存放两个数组中相同的元素
        Set<Integer> temp = new HashSet<Integer>();  //用来存放数组a中的元素

        for (int i = 0; i < a.length; i++) {
            temp.add(a[i]);   //把数组a中的元素放到Set中，可以去除重复的元素
        }

        for (int j = 0; j < b.length; j++) {
            //把数组b中的元素添加到temp中
            //如果temp中已存在相同的元素，则temp.add（b[j]）返回false
            if(!temp.add(b[j]))
                same.add(b[j]);
        }
        return same;
    }

}
