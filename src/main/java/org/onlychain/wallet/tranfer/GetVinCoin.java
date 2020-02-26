package org.onlychain.wallet.tranfer;

import org.onlychain.net.Request;
import org.onlychain.utils.CheckUtils;

import java.util.LinkedHashMap;

public abstract class GetVinCoin {

public abstract void walletCoinList(StringBuffer json);
public abstract void getCoinFail(Exception e);
    //开通权益
    public static final int TYPE_4_FOR_INTEREST=4;
    //普通交易
    public static final int TYPE_1_FOR_TRANSFER=1;

    private StringBuffer jsons;
    public GetVinCoin(String address) {
        if (CheckUtils.checkAddress(address))
        {
            LinkedHashMap<String,String>  parameter=new LinkedHashMap<>();
            parameter.put("address",address);
            new Request(ApiConfig.API_selectPurse,parameter) {
                @Override
                public void success(StringBuffer json) {
                    jsons=json;
                    walletCoinList(json);
                }

                @Override
                public void fail(Exception e) {
                    getCoinFail(e);
                }
            };
        }else {
            String result="{\"请输入正确的地址 = oc+40位16进制chars\"}";
        }
    }


    //获取零钱列表
    public void getCoinList(){


    }

    //获取类型获取零钱列表
    public void getCoinListByType(int type){

    }

    //获取余额
    public void getBalance(){

    }

    //根据类型获取余额
    public void getBalanceByType(int type){

    }
    public static class TradingBean {
        private long lockTime;
        private long cost;

    }
}
