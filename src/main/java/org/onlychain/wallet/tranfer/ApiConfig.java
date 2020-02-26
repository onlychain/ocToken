package org.onlychain.wallet.tranfer;

public class ApiConfig {
    public static String IP="http://123.56.135.26:9082";

   //获取钱包资产列表
   public final static String API_selectPurse= IP+"/Trading/selectPurse";

    //提交交易
   public final static String API_receiveAction= IP+"/Action/receiveAction";






    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        ApiConfig.IP = IP;
    }
}
