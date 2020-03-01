package org.onlychain.wallet.base;

import java.util.LinkedHashMap;

public class ApiConfig {
    public static String IP="http://123.56.135.26:9082";

   //获取钱包资产列表
   public final static String API_selectPurse= IP+"/Trading/selectPurse";
    public static LinkedHashMap<String,Object> selectPurse(String address){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("address",address);
        return map;
    }

    //提交交易
   public final static String API_receiveAction= IP+"/Action/receiveAction";
   public static LinkedHashMap<String,Object> receiveAction(String message){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("message",message);
        return map;
   }

    //获取系统时间、高度、轮次、轮次时间
    public final static String API_getSystemInfo= IP+"/Node/getSystemInfo";

    //查询某一个区块
    public final static String API_queryBlock= IP+"/Block/queryBlock";
    public static LinkedHashMap<String,Object> queryBlock(String height){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("height",Integer.valueOf(height));
        return map;
    }


    //查询某一个Txid的Action
    public final static String API_queryAction= IP+"/Action/queryAction";
    public static LinkedHashMap<String,Object> queryAction(String txId){
        LinkedHashMap<String,Object> map=new LinkedHashMap<>();
        map.put("txId",txId);
        return map;
    }



    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        ApiConfig.IP = IP;
    }
}
