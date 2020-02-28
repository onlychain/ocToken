package org.onlychain;

import org.onlychain.bean.AccountBean;
import org.onlychain.bean.BaseActionBean;
import org.onlychain.bean.HeadBean;
import org.onlychain.bean.OutBean;
import org.onlychain.utils.OcMath;
import org.onlychain.utils.WalletUtils;
import org.onlychain.wallet.base.ApiConfig;
import org.onlychain.wallet.tranfer.GetVinCoin;
import org.onlychain.wallet.tranfer.MakeAction;

import java.util.ArrayList;
import java.util.List;


public class App 
{
    public static void main(String[] args )
    {

        //根据随机私钥生成账户
        AccountBean mAccountBeanRandom=WalletUtils.createAccount();

        //根据确定的私钥生成账户
        final AccountBean mAccountBean=WalletUtils.createAccount(OcMath.hexStringToByteArray(""));
        //获取带oc前缀的地址
        System.out.println("带oc前缀的地址    "+mAccountBean.getAddress());
        //获取不带oc前缀的地址
        System.out.println("不带oc前缀的地址  "+mAccountBean.getAddressNoPrefix());

        System.out.println("公钥  "+mAccountBean.getPublicKey());

        //更换节点IP
        ApiConfig.setIP("http://123.56.135.26:9082");

        //获取链上指定地址的钱包零钱
        new GetVinCoin(mAccountBean) {
            @Override
            public void walletCoinList(StringBuffer json) {

                //获取钱包所有的零钱列表
                getCoinList();
                //获取钱包可流通的零钱列表
                getCoinList(TYPE_1_FOR_TRANSFER);
                //获取钱包开通权益的零钱列表
                getCoinList(TYPE_4_FOR_INTEREST);


                //获取所有类型的余额
                System.out.println("获取所有类型的余额    "+getBalance());
                //获取可流通类型的余额
                System.out.println("获取可流通类型的余额  "+getBalance(TYPE_1_FOR_TRANSFER));
                //获取被开通权益的余额
                System.out.println("获取被开通权益的余额  "+getBalance(TYPE_4_FOR_INTEREST));


                //获取一笔最适合开通权益的零钱（默认在100~120之间取）
                System.out.println("获取一笔最适合开通权益的零钱   "+getCoinForInterest().getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最大的零钱
                System.out.println("在指定范围内获取一笔最大的零钱 "+getCoinForMax("0","10000").getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最小的零钱
                System.out.println("在指定范围内获取一笔最小的零钱 "+getCoinForMin("10","10000").getValue()/Long.valueOf(BASE_NUMBER));

                List<OutBean> outList= new ArrayList<>();
                outList.add(new OutBean(100*100000000,mAccountBean.getAddress()));
                MakeAction mMakeAction = new MakeAction(mAccountBean,getCoinList(TYPE_1_FOR_TRANSFER),outList);
                BaseActionBean mBaseActionBean=mMakeAction.createActionForType1("11111");

                //获取Action
                System.out.println("~~~~~~~~~"+mBaseActionBean.getMessage()+mBaseActionBean.getSigStr());
                //获取Action的txid
                System.out.println(mBaseActionBean.getTxid());
                //使用公钥验证消息签名的真实性
                System.out.println(mBaseActionBean.checkSig(mAccountBean.getPublicKeyBin()));
            }

            @Override
            public void getCoinFail(Exception e) {
                System.out.println((e));
            }


        };




    }
}
