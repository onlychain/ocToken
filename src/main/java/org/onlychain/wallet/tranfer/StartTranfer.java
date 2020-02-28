package org.onlychain.wallet.tranfer;

import org.onlychain.bean.AccountBean;
import org.onlychain.net.Request;
import org.onlychain.secp256k1.PublicKey;
import org.onlychain.secp256k1.Secp256k1;
import org.onlychain.utils.OcMath;
import org.onlychain.bean.PurseBean;

import java.util.LinkedHashMap;

//发起请求
public abstract class StartTranfer {
   private byte[] privateKey;
   private String ActionMsg;
   private String Json;
   private String ip;
   public abstract void receiveAction(StringBuffer json);

    public StartTranfer(String ip) {
        this.ip= ip;
    }

    public StartTranfer createAction(AccountBean mAccountBean){
        this.privateKey=privateKey;
        PublicKey publicKey = Secp256k1.createPublicKey(privateKey);
        String privateKeyHex= OcMath.toHexStringNoPrefix(privateKey);
        String publicKeyHex= OcMath.toHexStringNoPrefix(publicKey.serialize(false));
//        String address=getAdressByPublic(publicKeyHex);

       //将vin的json构造成输入list
       //16进制拼接
        this.ActionMsg="";

        LinkedHashMap<String,String> parameter=new LinkedHashMap<>();
        parameter.put("address","address");
        new Request(this.ip+"/Trading/selectPurse",parameter) {
            @Override
            public void success(StringBuffer json) {
//               System.out.println(json);
                PurseBean mPurseBean;

                commitAction(encodeAction());
            //构造交易整体字符串

            }

            @Override
            public void fail(Exception e) {

            }
        };
      return this;
   }


   public void commitAction(String ActionMsg){
       LinkedHashMap<String,String> mapValue=new LinkedHashMap<>();
       mapValue.put("message",ActionMsg);
       new Request(ip+"/Action/receiveAction",mapValue) {
           @Override
           public void success(StringBuffer json) {
//               System.out.println(json);
               receiveAction(json);
           }

           @Override
           public void fail(Exception e) {

           }
       };
   }



    //LEB128 输入笔数：01 " +
    //引用的txid:  "9aa9b9e37418ec493967a88c3f4847dff1feabb1bdb37c369cbc064f7729f648" +
    //LEB128 索引:   "01
   public String encodeAction(){
        return "actionMsg";
   }


}
