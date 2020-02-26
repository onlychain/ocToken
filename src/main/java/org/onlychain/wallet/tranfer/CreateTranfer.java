package org.onlychain.wallet.tranfer;

import org.onlychain.crypto.Hash;
import org.onlychain.crypto.RIPEMD160Digest;
import org.onlychain.net.Request;
import org.onlychain.secp256k1.PublicKey;
import org.onlychain.secp256k1.Secp256k1;
import org.onlychain.utils.OcMath;
import org.onlychain.wallet.walletbean.PurseBean;

import java.util.LinkedHashMap;

//发起请求
public abstract class CreateTranfer {
   private byte[] privateKey;
   private String ActionMsg;
   private String Json;
   private String ip;
   public abstract void receiveAction(StringBuffer json);

    public CreateTranfer(String ip) {
        this.ip= ip;
    }

    public CreateTranfer createAction(byte[] privateKey,String fromAddress,String toAddress,String howCoin){
        this.privateKey=privateKey;
        PublicKey publicKey = Secp256k1.createPublicKey(privateKey);
        String privateKeyHex= OcMath.toHexStringNoPrefix(privateKey);
        String publicKeyHex= OcMath.toHexStringNoPrefix(publicKey.serialize(false));
        String address=getAdressByPublic(publicKeyHex);

       //将vin的json构造成输入list
       //16进制拼接
        this.ActionMsg="";

        LinkedHashMap<String,String> parameter=new LinkedHashMap<>();
        parameter.put("address",address);
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

    public static String getAdressByPublic(String hexPublicKey) {
        byte[] sha256 = Hash.sha256(OcMath.hexStringToByteArray(hexPublicKey));
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(sha256, 0, sha256.length);
        byte[] out = new byte[digest.getDigestSize()];
        digest.doFinal(out, 0);
        return  OcMath.toHexStringNoPrefix(out).toLowerCase();
    }
}
