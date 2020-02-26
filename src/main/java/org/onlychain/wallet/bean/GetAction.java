package org.onlychain.wallet.bean;


import org.onlychain.utils.Leb128Utils;
import org.onlychain.utils.OcMath;

import java.util.ArrayList;
import java.util.List;

public class GetAction {
    //区块序列化数据
    private String blockData;
    private int subIndex=0;
    private ActionsBean mActionsBean;

    public ActionsBean getmActionsBean() {
        return mActionsBean;
    }

    public GetAction(String blockData) {
        this.blockData = blockData;
        mActionsBean=new ActionsBean();
        mActionsBean.setVersion(getSubText(1));
        mActionsBean.setActionType(getSubText(1));
        mActionsBean.setHasTrading(getSubText(1));

        //判断是否有交易数据体
        if(mActionsBean.getHasTrading().equals("01")){
            ActionsBean.TradingBean mTradingBean=new ActionsBean.TradingBean();
//            mTradingBean.setVinNum(hexToLong(getSubText(1)));
//----------------------------------------交易输入---------------------------------------------------------
            mTradingBean.setVinNum(Leb128Utils.decodeUnsigned(getLebNum()));
            List<ActionsBean.TradingBean.VinBean> mVinBeanList=new ArrayList<>();
            for (long index=0;index<mTradingBean.getVinNum();index++)
            {
                ActionsBean.TradingBean.VinBean mVinBean=new ActionsBean.TradingBean.VinBean();
                mVinBean.setTxId(getSubText(32));
                mVinBean.setN(getLebNum());
                mVinBean.setScriptLength(hexToInteger(getSubText(2)));
                mVinBean.setScriptSig(getSubText(mVinBean.getScriptLength()));
                mVinBeanList.add(mVinBean);
            }
            mTradingBean.setVin(mVinBeanList);

//----------------------------------------交易输出---------------------------------------------------------
            mTradingBean.setVoutNum(Leb128Utils.decodeUnsigned(getLebNum()));
            List<ActionsBean.TradingBean.VoutBean> mVoutBeanList=new ArrayList<>();
            for (long index=0;index<mTradingBean.getVoutNum();index++)
            {

                ActionsBean.TradingBean.VoutBean mVoutBean=new ActionsBean.TradingBean.VoutBean();
                mVoutBean.setValue(hexToLong(getSubText(8)));
                mVoutBean.setN(String.valueOf(index));
                mVoutBean.setReqSigs(getSubText(27));
                mVoutBean.setAddress(mVoutBean.getReqSigs().substring(10,50));
                mVoutBeanList.add(mVoutBean);
            }
            mTradingBean.setVout(mVoutBeanList);

            mTradingBean.setLockTime(Leb128Utils.decodeUnsigned(getLebNum()));
            mTradingBean.setCost(hexToLong(getSubText(8)));
            mActionsBean.setTrading(mTradingBean);
        }
//----------------------------------------判断是否有Action体---------------------------------------------------------
        mActionsBean.setHasAction(getSubText(1));
        if(mActionsBean.getHasAction().equals("01"))
        {

        }

        mActionsBean.setTime(Leb128Utils.decodeUnsigned(getLebNum()));
        mActionsBean.setCreatedBlock(Leb128Utils.decodeUnsigned(getLebNum()));
        mActionsBean.setOriginator(getSubText(33));
        mActionsBean.setInsLength(getSubText(1));
        mActionsBean.setIns(getSubText(1));
        mActionsBean.setActionSign(blockData.substring(subIndex));
    }



    private int hexToInteger(String hex){
        return Integer.valueOf(String.valueOf(OcMath.toBigInt(hex)));
    }

    private Long hexToLong(String hex){
        return Long.valueOf(String.valueOf(OcMath.toBigInt(hex)));
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
