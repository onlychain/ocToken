package org.onlychain.wallet.bean;



import org.onlychain.utils.Leb128Utils;
import org.onlychain.utils.OcMath;

import java.math.BigInteger;

/**
 * 解析数据
 */
public class TransactionAnalysis {
    //区块序列化数据
    private String blockData;
    private int subIndex=0;

    //版本号
    private BigInteger version;
    //Action类型
    private BigInteger actionType;
    //是否有交易字段
    private BigInteger isTrading;
    //交易数据
    private TransData transData;


    public TransData getTransData() {
        return transData;
    }

    public BigInteger getIsTrading() {
        return isTrading;
    }

    public BigInteger getActionType() {
        return actionType;
    }

    public BigInteger getVersion() {
        return version;
    }

    public TransactionAnalysis(String blockData) {
        this.blockData = blockData;
        version= OcMath.toBigInt(getSubText(1));
        actionType= OcMath.toBigInt(getSubText(1));
        isTrading= OcMath.toBigInt(getSubText(1));


        //交易数据，通过isTrading判断是否有交易数据
        if(Integer.valueOf(String.valueOf(isTrading))==1){
            transData=new TransData();
            transData.setVinNumber(getLebNum());
            transData.setTxId(getSubText(32));
            transData.setN(getLebNum());
            transData.setScriptLength(getSubText(2));
        }

    }




    /**
     * 要非常确定该顺序的字段是采用leb128编码，否则会造成误取
     * @return
     */
    private String getLebNum(){
        int startIndex=subIndex;
        int endIndex;
        while (true)
        {
            if (Leb128Utils.isLebEnd(blockData.substring(subIndex,subIndex+2)))
            {
                endIndex=subIndex+2;
                subIndex+=2;
                break;
            } else
                subIndex+=2;
        }
        return blockData.substring(startIndex,endIndex);
    }


    /**
     * 获取分割内容
     * @param byteSize 分割字段所占字节大小
     * @return
     */
    private String getSubText(int byteSize){
        String result=blockData.substring(subIndex,subIndex+byteSize*2);
        subIndex=subIndex+result.length();
      return result;
    }



    //交易输入内容
    public static class TransData {
        //交易输入数量
        private String vinNumber;
        //交易引用hash
        private String txId;
        //交易引用索引
        private String n;
        //交易解锁脚本长度
        private String scriptLength;
        //交易解锁脚本
        private String scriptContent;

        public String getVinNumber() {
            return vinNumber;
        }

        public void setVinNumber(String vinNumber) {
            this.vinNumber = vinNumber;
        }

        public String getTxId() {
            return txId;
        }

        public void setTxId(String txId) {
            this.txId = txId;
        }

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public String getScriptLength() {
            return scriptLength;
        }

        public void setScriptLength(String scriptLength) {
            this.scriptLength = scriptLength;
        }

        public String getScriptContent() {
            return scriptContent;
        }

        public void setScriptContent(String scriptContent) {
            this.scriptContent = scriptContent;
        }
    }

}
