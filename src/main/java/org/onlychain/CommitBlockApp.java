package org.onlychain;
import org.onlychain.bean.*;
import org.onlychain.utils.OcMath;
import org.onlychain.utils.WalletUtils;
import org.onlychain.wallet.base.ApiConfig;
import org.onlychain.wallet.iface.ImpQueryAction;
import org.onlychain.wallet.tranfer.GetVinCoin;
import org.onlychain.wallet.tranfer.QueryUtils;
import org.onlychain.wallet.tranfer.StartTranfer;
import java.math.BigDecimal;

public class CommitBlockApp
{
    public static void main(String[] args )
    {

        //根据随机私钥生成账户
        AccountBean mAccountBeanRandom=WalletUtils.createAccount();

        //根据确定的私钥生成账户
        final AccountBean mAccountBean=WalletUtils.createAccount(OcMath.hexStringToByteArray("2603e40800407582ca68f327bdc7222922c7e53d6b2f73c12e4cdc6407752500"));
        //获取带oc前缀的地址
        System.out.println("带oc前缀的地址    "+mAccountBean.getAddress());
        //获取不带oc前缀的地址
        System.out.println("不带oc前缀的地址  "+mAccountBean.getAddressNoPrefix());
        //获取公钥
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
                //判断权益是否开通成功
                System.out.println("是否拥有权益："+(getBalance(TYPE_4_FOR_INTEREST).compareTo(new BigDecimal("100"))>-1 ? "有":"没有"));


                //获取一笔最适合开通权益的零钱（默认在100~120之间取）
                System.out.println("获取一笔最适合开通权益的零钱   "+getCoinForInterest().getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最大的零钱
                System.out.println("在指定范围内获取一笔最大的零钱 "+getCoinForMax("0","10000").getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最小的零钱
                System.out.println("在指定范围内获取一笔最小的零钱 "+getCoinForMin("10","10000").getValue()/Long.valueOf(BASE_NUMBER));


                //设定转账的目标地址
                OutBean mOutBean= new OutBean();
                mOutBean.setAddress("459d0c3fde261eeaecd2c47b484f20db3ef7558b");
                mOutBean.setValue(1*Long.valueOf(BASE_NUMBER));

                StartTranfer mStartTranfer=new StartTranfer(mAccountBean) {
                    @Override
                    public void receiveAction(final BaseActionBean localCommitBean, StringBuffer json) {
                        //获取Action的签名
                        System.out.println(localCommitBean.getSigStr());
                        //获取Action的消息体
                        System.out.println(localCommitBean.getMessage());
                        //获取Action的txid
                        System.out.println(localCommitBean.getTxid());
                        //使用公钥验证消息签名的真实性
                        System.out.println(localCommitBean.checkSig(mAccountBean.getPublicKeyBin()));
                        //获取提交后的结果,提交成功不等于上链成功，需要通过查询txid为准
                        System.out.println("提交成功的返回结果=="+json);

                        //通过查询Txid来确定是否上链成功
                        new QueryUtils(4,localCommitBean.getTxid(), new ImpQueryAction() {
                            @Override
                            public void inChainSuceess(StringBuffer json) {
                                System.out.println("上链成功的交易数据："+json);
                                System.out.println("上链成功TXID："+localCommitBean.getTxid());
                            }
                            @Override
                            public void inChainFail() {
                                System.out.println("上链失败");
                            }
                        });
                    }
                    @Override
                    public void receiveFail(Exception e) {
                        System.out.println(e);
                    }
                };

                //
                // 开始向节点提交签名后的数据
                //
                if(getBalance(TYPE_4_FOR_INTEREST).compareTo(new BigDecimal("100"))>-1)
                {
                    //如果已开通权益，即可使用转账功能
                    mStartTranfer.inputCoin(getCoinList(TYPE_1_FOR_TRANSFER)).inputAddress(mOutBean).commit();
                }
                else{
                    //开通权益
                    mStartTranfer.openInterest(getCoinForMin("100","120"));
                }

            }


            @Override
            public void getCoinFail(Exception e) {
                System.out.println((e));
            }

        };

    }
}
