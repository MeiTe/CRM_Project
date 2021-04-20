package it.com.crm.settings;

public class compart {
    public static void main(String[] args) {
        String str1 ="0,7,9,3,4";
        String str2 = "88,6,7,9,10";
        Boolean result = containRepeatChar(str1,str2);
        System.out.println("result:"+result);
    }
    public static boolean containRepeatChar(String str,String str1){
        try {
            String[] arr1 = str.split(",");
            String[] arr2 = str1.split(",");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < arr2.length; i++) {
                for (int j = 0; j < arr1.length; j++) {
                    if (arr1[j].equals(arr2[i])) {
                        sb.append(arr1[j] + ",");
                    }
                }
            }
            System.out.println("结果：" + sb.toString().substring(0, sb.toString().length() - 1));
            return true;
        }catch (Exception e){
            System.out.println("结果:没有重复的数据");
            return false;
        }
    }
}
