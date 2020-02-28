package org.onlychain.bean;



import org.onlychain.utils.Leb128Utils;
import org.onlychain.utils.OcMath;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析数据
 */
public class TransactionAnalysisS {
    //区块序列化数据
    private String blockData;
    private int subIndex=0;
    TransActionBean mTransActionBean;

    public TransActionBean getmTransActionBean() {
        return mTransActionBean;
    }

    public TransactionAnalysisS(String blockData) {
        this.blockData = blockData;

        mTransActionBean=new TransActionBean();
        mTransActionBean.setVersion(hexToInteger(getSubText(1)));
        mTransActionBean.setActionType(getSubText(1));
        mTransActionBean.setIns(getSubText(1));

        //交易数据，通过Ins判断是否有交易数据
        if(mTransActionBean.getIns().equals("01")){
            TransActionBean.TradingBean Trading=new TransActionBean.TradingBean();

            List<TransActionBean.TradingBean.VinBean> mVinBeanList=new ArrayList<>();
            TransActionBean.TradingBean.VinBean mVinBean=new TransActionBean.TradingBean.VinBean();
            mVinBean.setTxId(getSubText(32));
            mVinBean.setN(getLebNum());
            mVinBeanList.add(mVinBean);
//            Trading.setCost(Leb128Utils.decodeUnsigned(getLebNum()));
//            Trading.setLockTime();

            Trading.setVin(mVinBeanList);
            mTransActionBean.setTrading(Trading);

        }
    }

    private int hexToInteger(String hex){
        return Integer.valueOf(String.valueOf(OcMath.toBigInt(hex)));
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



}
