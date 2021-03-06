package org.onlychain.wallet.tranfer;

import com.alibaba.fastjson.JSON;
import org.onlychain.bean.*;
import org.onlychain.net.Request;
import org.onlychain.secp256k1.PublicKey;
import org.onlychain.secp256k1.Secp256k1;
import org.onlychain.utils.OcMath;
import org.onlychain.wallet.base.ApiConfig;
import org.onlychain.wallet.iface.ImpGetAction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.onlychain.wallet.tranfer.GetVinCoin.*;

/**
 * 各类交易类型封装在此
 */
public abstract class StartTranfer {
    public abstract void receiveAction(BaseActionBean localCommitBean, StringBuffer json);
    public abstract void receiveFail(Exception e);
    private String ActionMsg;
    private String Json;
    private AccountBean mAccountBean;
    private List<OutBean> outList;
    private List<PurseBean.RecordBean> coinLis;
    private long blockHeight;

    public StartTranfer(AccountBean mAccountBean) {
        this.mAccountBean=mAccountBean;
    }


    public StartTranfer inputCoin(List<PurseBean.RecordBean> coinLis){
        this.coinLis=coinLis;
        return this;
    }
    public StartTranfer initBlockHeight(long blockHeight){
        this.blockHeight=blockHeight;
        return this;
    }

    public StartTranfer inputCoin(PurseBean.RecordBean coin){
        List<PurseBean.RecordBean> coinLis=new ArrayList<>();
        coinLis.add(coin);
        this.coinLis=coinLis;
        return this;
    }


    public StartTranfer inputAddress(List<OutBean> outList){
        this.outList=outList;
        return this;
    }

    public StartTranfer inputAddressList(List<OutBean> outoutList){
        //最多不要超过50
        if (outoutList.size()>50)
            return null;
        this.outList=outoutList;
        return this;
    }


    public StartTranfer inputAddress(OutBean out){
        this.outList=new ArrayList<>();
        this.outList.add(out);
        return this;
    }

    public StartTranfer openInterest(PurseBean.RecordBean coin){
        List<PurseBean.RecordBean> coinLis=new ArrayList<>();
        coinLis.add(coin);
        this.coinLis=coinLis;
//        commit(TYPE_4_FOR_INTEREST);
        return this;
    }


    public void commit(){
        commit(TYPE_1_FOR_TRANSFER);
    }

    /**
     * 获取质押裸交易数据
     * @return
     */
    public void getPledgeSignData(final ImpGetAction mImpGetAction){
        final long pledgeCoin=Long.valueOf(String.valueOf(sumCoinList(this.coinLis)));
        this.outList=new ArrayList<>();
        this.outList.add(new OutBean(pledgeCoin,mAccountBean.getAddressNoPrefix()));
        if(blockHeight == 0) {
            new Request(ApiConfig.API_getSystemInfo) {
                @Override
                public void success(StringBuffer json) {
                    GetSystemInfoBean.RecordBean mGetSystemInfoBean = JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();
                    //根据coin数量计算锁定高度
                    long lockHeight = (30 * (int) Math.floor(pledgeCoin / Long.valueOf(BASE_NUMBER))) + Long.valueOf(String.valueOf(mGetSystemInfoBean.getBlockHeight()));
                    MakeAction mMakeAction = new MakeAction(mAccountBean, TYPE_1_FOR_TRANSFER, coinLis, outList, lockHeight);
                    final BaseActionBean localCommitBean = mMakeAction.createAction(String.valueOf(mGetSystemInfoBean.getBlockHeight()));
                    mImpGetAction.receive(localCommitBean.getCommitData());
                }

                @Override
                public void fail(Exception e) {
                    receiveFail(new Exception("获取最新高度失败,请检查节点是否正常"));
                }
            };
        }else{
            //根据coin数量计算锁定高度
            long lockHeight = (30 * (int) Math.floor(pledgeCoin / Long.valueOf(BASE_NUMBER))) + Long.valueOf(String.valueOf(blockHeight));
            MakeAction mMakeAction = new MakeAction(mAccountBean, TYPE_1_FOR_TRANSFER, coinLis, outList, lockHeight);
            final BaseActionBean localCommitBean = mMakeAction.createAction(String.valueOf(blockHeight));
            mImpGetAction.receive(localCommitBean.getCommitData());
        }
    }


    /**
     * 合并零钱或拆额零钱
     * @param excreteCoin
     * @return
     */
    public StartTranfer inputExcreteCoins(long excreteCoin){
        this.outList=new ArrayList<>();
        this.outList.add(new OutBean(excreteCoin,mAccountBean.getAddressNoPrefix()));
        return this;
    }

    /**
     * 获取签名数据
     * @param actionType
     * @param mImpGetAction
     */
    public void getAction(final int actionType,final ImpGetAction mImpGetAction){
        //找零
        long findCoin=0;
        if(actionType==TYPE_1_FOR_TRANSFER){
            findCoin= Long.valueOf(sumCoinList(coinLis).subtract(sumOutList(outList)).toString());
            //如果零钱全部转出则无需给自己找零
            if (findCoin!=0)
                this.outList.add(new OutBean(findCoin,mAccountBean.getAddressNoPrefix()));
        }else if(actionType==TYPE_4_FOR_INTEREST){
            this.outList=new ArrayList<>();
            this.outList.add(new OutBean(this.coinLis.get(0).getValue(),mAccountBean.getAddressNoPrefix()));
        }


        if(coinLis!=null&&coinLis.size()>0&&Long.valueOf(sumCoinList(coinLis).toString())>0)
        {
            if (findCoin<0)
            {
                System.out.println("有余额但不够抵扣");
                return;
            }
        }else {
            System.out.println("完全没有余额");
            return;
        }
        if(blockHeight == 0) {
            new Request(ApiConfig.API_getSystemInfo) {
                @Override
                public void success(StringBuffer json) {

                    GetSystemInfoBean.RecordBean mGetSystemInfoBean=JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();

                    MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
                    final BaseActionBean localCommitBean=mMakeAction.createAction(String.valueOf(mGetSystemInfoBean.getBlockHeight()));
                    mImpGetAction.receive(localCommitBean.getCommitData());
                }
                @Override
                public void fail(Exception e) {
                    receiveFail(new Exception("获取最新高度失败,请检查节点是否正常"));
                }
            };
        }else{
            MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
            final BaseActionBean localCommitBean=mMakeAction.createAction(String.valueOf(blockHeight));
            mImpGetAction.receive(localCommitBean.getCommitData());
        }
    }


    public void commit(final int actionType){
        //找零
        long findCoin=0;
        if(actionType==TYPE_1_FOR_TRANSFER){
            findCoin= Long.valueOf(sumCoinList(coinLis).subtract(sumOutList(outList)).toString());
            //如果零钱全部转出则无需给自己找零
            if (findCoin!=0)
                this.outList.add(new OutBean(findCoin,mAccountBean.getAddressNoPrefix()));
        }else if(actionType==TYPE_4_FOR_INTEREST){
            this.outList=new ArrayList<>();
            this.outList.add(new OutBean(this.coinLis.get(0).getValue(),mAccountBean.getAddressNoPrefix()));
        }


        if(coinLis!=null&&coinLis.size()>0&&Long.valueOf(sumCoinList(coinLis).toString())>0)
        {
            if (findCoin<0)
            {
                System.out.println("有余额但不够抵扣");
                return;
            }
        }else {
            System.out.println("完全没有余额");
            return;
        }

        System.out.println(sumCoinList(coinLis));
        System.out.println(sumOutList(outList));
        System.out.println(findCoin);

        doCommit(actionType);
    }




    /**
     * 做最后整合提交
     */
    public void doCommit(final int actionType){
        if(blockHeight == 0){//未输入自定义高度，去获取最新高度后再提交交易
            //先从节点获取最新高度信息
            new Request(ApiConfig.API_getSystemInfo) {
                @Override
                public void success(StringBuffer json) {
                    GetSystemInfoBean.RecordBean mGetSystemInfoBean=JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();
                    requestReceiveAction(actionType,mGetSystemInfoBean.getBlockHeight());
                }
                @Override
                public void fail(Exception e) {
                    receiveFail(new Exception("获取最新高度失败,请检查节点是否正常"));
                }
            };
        }else{//已经输入自定义高度，则使用自定义的高度进行提交交易
            requestReceiveAction(actionType,blockHeight);
        }
    }

    private void requestReceiveAction(final int actionType,final long blockHeight){
        MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
        final BaseActionBean localCommitBean=mMakeAction.createAction(String.valueOf(blockHeight));
        System.out.println("提交的目标数据："+localCommitBean.getCommitData());
        //开始将Action序列化信息提交至节点
        new Request(ApiConfig.API_receiveAction,ApiConfig.receiveAction(localCommitBean.getCommitData())) {
            @Override
            public void success(StringBuffer json) {
                receiveAction(localCommitBean,json);
            }

            @Override
            public void fail(Exception e) {
                receiveFail(e);
            }
        };
    }



    public BigDecimal sumCoinList(List<PurseBean.RecordBean> coinList){
        BigDecimal sum=new BigDecimal("0");
        for (PurseBean.RecordBean coin : coinList)
            sum=sum.add(new BigDecimal(coin.getValue()));
        return sum;
    }

    public BigDecimal sumOutList(List<OutBean> outList){
        BigDecimal sum=new BigDecimal("0");
        for (OutBean out : outList)
            sum=sum.add(new BigDecimal(out.getValue()));
        return sum;
    }
}
