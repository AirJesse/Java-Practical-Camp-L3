package org.bank.common.util;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.utils
 * @Author: lizongle
 * @Description:编码工具类
 * @Version: 1.0
 */
public class CodeUtil {

    private static final String DEPT_TPYE="JKD";
    private static final String PERMISSION_TPYE="JKP";
    /**
     * 右补位，左对齐
     * @pparam oriStr  原字符串
     * @param len  目标字符串长度
     * @param alexin  补位字符
     * @return  目标字符串
     * 以alexin 做为补位
     */
    public  static String padRight(String oriStr,int len,String alexin){
        String str = "";
        int strlen = oriStr.length();
        if(strlen < len){
            for(int i=0;i<len-strlen;i++){
                str=str+alexin;
            }
        }
        str=str+oriStr;
        return str;
    }

    public static String deptCode(String oriStr,int len,String alexin){
        return DEPT_TPYE+padRight(oriStr, len, alexin);
    }

}
