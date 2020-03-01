package org.onlychain.wallet.tranfer;

import com.alibaba.fastjson.JSON;
import org.onlychain.bean.*;
import org.onlychain.net.Request;
import org.onlychain.secp256k1.PublicKey;
import org.onlychain.secp256k1.Secp256k1;
import org.onlychain.utils.OcMath;
import org.onlychain.wallet.base.ApiConfig;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.onlychain.wallet.tranfer.GetVinCoin.*;

/**
 * 各类交易类型封装在此
 */
public abstract class StartTranfer {
    public abstract void receiveAction(BaseActionBean localCommitBean,StringBuffer json);
    public abstract void receiveFail(Exception e);
   private String ActionMsg;
   private String Json;
    private AccountBean mAccountBean;
    private List<OutBean> outList;
    private List<PurseBean.RecordBean> coinLis;

    public StartTranfer(AccountBean mAccountBean) {
        this.mAccountBean=mAccountBean;
    }


   public StartTranfer inputCoin(List<PurseBean.RecordBean> coinLis){
        this.coinLis=coinLis;
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

    public StartTranfer inputAddress(OutBean out){
        this.outList=new ArrayList<>();
        this.outList.add(out);
        return this;
    }


    public void openInterest(PurseBean.RecordBean coin){
        List<PurseBean.RecordBean> coinLis=new ArrayList<>();
        coinLis.add(coin);
        this.coinLis=coinLis;
        commit(TYPE_4_FOR_INTEREST);
    }


    public void commit(){
        commit(TYPE_1_FOR_TRANSFER);
    }


    public void commit(final int actionType){
        //找零
        long findCoin=0;
        if(actionType==TYPE_1_FOR_TRANSFER){
            findCoin= Long.valueOf(sumCoinList(coinLis).subtract(sumOutList(outList)).toString());
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


        //先从节点获取最新高度信息
      new Request(ApiConfig.API_getSystemInfo) {
            @Override
            public void success(StringBuffer json) {
                GetSystemInfoBean.RecordBean mGetSystemInfoBean=JSON.parseObject(json.toString(), GetSystemInfoBean.class).getRecord();

                MakeAction mMakeAction = new MakeAction(mAccountBean,actionType,coinLis,outList);
                final BaseActionBean localCommitBean=mMakeAction.createAction(String.valueOf(mGetSystemInfoBean.getBlockHeight()));

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
            @Override
            public void fail(Exception e) {
                receiveFail(new Exception("获取最新高度失败,请检查节点是否正常"));
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
