package org.onlychain.wallet.tranfer;



import org.onlychain.secp256k1.PublicKey;
import org.onlychain.secp256k1.Secp256k1;
import org.onlychain.secp256k1.Signature;
import org.onlychain.utils.OcMath;

import java.math.BigInteger;

public class Script {

    //--------vIn
    //脚本长度(字节)： 006a" +
    //脚本内容：

    /**
     * 交易输入脚本
     * @param privateKey 私钥
     * @param message 消息
     * @return 脚本结果
     */
    public static String vInScript(byte[] privateKey,byte[]  message){
        String sigResult;
        String sigContent;
        Signature sig = Secp256k1.sign(privateKey, message);
        PublicKey publicKey = Secp256k1.createPublicKey(privateKey);

        byte[] sigByte=sig.serialize();
        String sigStr= OcMath.toHexStringNoPrefix(sigByte);

        byte[] publicKeyBin=publicKey.serialize(true);
        String publicKeyStr=OcMath.toHexStringNoPrefix(publicKeyBin);

        sigContent=OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(sigByte.length)),2)+sigStr+OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(publicKeyBin.length)),2)+publicKeyStr;
        sigResult=OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(OcMath.hexStringToByteArray(sigContent).length)),4)+sigContent;
        return sigResult;
    }


    /**
     * 交易输出脚本
     * @param address 地址
     * @return 脚本结果
     */
    public static String vOutScript(String address){
        String scriptResult="76a914"+address+"88ac";
        scriptResult=OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(OcMath.hexStringToByteArray(scriptResult).length)),4)+scriptResult;
        return scriptResult;
    }

}
