package org.onlychain.bean;

import org.onlychain.crypto.Hash;
import org.onlychain.secp256k1.PublicKey;
import org.onlychain.secp256k1.Secp256k1;
import org.onlychain.secp256k1.Signature;
import org.onlychain.utils.OcMath;
import org.onlychain.utils.WalletUtils;

public class BaseActionBean {
    private String message;
    private String sigStr;
    private String txid;


    /**
     * s使用公钥验证消息的真实性
     * @param publicKeyBin
     * @return
     */
    public boolean checkSig(byte[] publicKeyBin){
        byte[] messageBin=  Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(message)));
//        System.out.println("==="+(message.substring(message.length()-144)));
        Signature sig = Signature.parse(OcMath.hexStringToByteArray(sigStr));
        PublicKey publicKey = PublicKey.parse(publicKeyBin);
        if (Secp256k1.verify(publicKey, messageBin, sig))
            return true;
        else
            return false;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }


    public String getTxid() {
        return WalletUtils.getTxId(message.substring(0,message.length()-140));
    }

    public String getSigStr() {
        return sigStr;
    }

    public void setSigStr(String sigStr) {
        this.sigStr = sigStr;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }


}

