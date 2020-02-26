package org.onlychain.wallet;


import org.onlychain.crypto.Hash;
import org.onlychain.utils.OcMath;

public class OCUtils {
    /**
     * 获取整交易的TxId
     * @param input
     * @return
     */
    public static String getTxId(byte[] input){
        return OcMath.toHexStringNoPrefix(Hash.sha256(Hash.sha256(input)));
    }

    /**
     * 获取整交易的TxId
     * @param input
     * @return
     */
    public static String getTxId(String input){
        return OcMath.toHexStringNoPrefix(Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(input))));
    }







}
