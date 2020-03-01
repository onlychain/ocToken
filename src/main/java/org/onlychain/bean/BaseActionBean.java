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
    private String commitData;

    /**
     * s使用公钥验证消息的真实性
     * @param publicKeyBin
     * @return
     */
    public boolean checkSig(byte[] publicKeyBin){
        if (Secp256k1.verify(PublicKey.parse(publicKeyBin), Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(message))), Signature.parse(OcMath.hexStringToByteArray(sigStr))))
            return true;
        else
            return false;
    }

    public String getCommitData() {
        return message+sigStr;
    }

    public void setCommitData(String commitData) {
        this.commitData = commitData;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }


    public String getTxid() {
        return WalletUtils.getTxId(getCommitData());
    }

    public byte[] getTxidBin() {
        return WalletUtils.getTxIdBin(getCommitData());
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

