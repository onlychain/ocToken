package org.onlychain;
import com.sun.xml.internal.bind.v2.TODO;
import org.onlychain.bean.*;
import org.onlychain.utils.OcMath;
import org.onlychain.utils.WalletUtils;
import org.onlychain.wallet.base.ApiConfig;
import org.onlychain.wallet.iface.ImpGetAction;
import org.onlychain.wallet.iface.ImpQueryAction;
import org.onlychain.wallet.tranfer.GetVinCoin;
import org.onlychain.wallet.tranfer.QueryUtils;
import org.onlychain.wallet.tranfer.StartTranfer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CommitBlockApp
{
    public static void main(String[] args )
    {
        //根据随机私钥生成账户
        AccountBean mAccountBeanRandom=WalletUtils.createAccount();
        //获取带oc前缀的地址
        System.out.println("随机版，带oc前缀的地址    "+mAccountBeanRandom.getAddress());
        //获取不带oc前缀的地址
        System.out.println("随机版，不带oc前缀的地址  "+mAccountBeanRandom.getAddressNoPrefix());
        //获取公钥
        System.out.println("随机版，公钥  "+mAccountBeanRandom.getPublicKey());

        //根据确定的私钥生成账户
        final AccountBean mAccountBean=WalletUtils.createAccount(OcMath.hexStringToByteArray("ea23e889d590a443831a785a398ce74179f09dece2fe5bfda41f795c50240c61"));
        //获取带oc前缀的地址
        System.out.println("带oc前缀的地址    "+mAccountBean.getAddress());
        //获取不带oc前缀的地址
        System.out.println("不带oc前缀的地址  "+mAccountBean.getAddressNoPrefix());
        //获取公钥
        System.out.println("公钥  "+mAccountBean.getPublicKey());

        //更换节点IP
        ApiConfig.init("http://39.98.135.66:9082");

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
                System.out.println("是否拥有权益："+(getBalance(TYPE_4_FOR_INTEREST).compareTo(new BigDecimal("20"))>-1 ? "有":"没有"));

                //获取一笔最适合开通权益的零钱（默认在20~30之间取）
                System.out.println("获取一笔最适合开通权益的零钱   "+getCoinForInterest().getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最大的零钱
                System.out.println("在指定范围内获取一笔最大的零钱 "+getCoinForMax("0","10000").getValue()/Long.valueOf(BASE_NUMBER));
                //在指定范围内，获取一笔最小的零钱
                System.out.println("在指定范围内获取一笔最小的零钱 "+getCoinForMin("10","10000").getValue()/Long.valueOf(BASE_NUMBER));


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

                // 开始向节点提交签名后的数据
                //获取当前钱包的零钱是否存在至少一笔type为4的标准权益质押量
                if(getBalance(TYPE_4_FOR_INTEREST).compareTo(new BigDecimal("20"))>-1)
                {
                    //设定转账的目标地址
                    OutBean mOutBean= new OutBean();
                    mOutBean.setAddress("459d0c3fde261eeaecd2c47b484f20db3ef7558b");
                    mOutBean.setValue(1*Long.valueOf(BASE_NUMBER));

                    OutBean mOutBean2= new OutBean();
                    mOutBean2.setAddress("419d0c3fde261eeaecd2c47b484f20db3ef7558b");
                    mOutBean2.setValue(1*Long.valueOf(BASE_NUMBER));

                    OutBean mOutBean3= new OutBean();
                    mOutBean3.setAddress("479d0c3fde261eeaecd2c47b484f20db3ef7558b");
                    mOutBean3.setValue(1*Long.valueOf(BASE_NUMBER));

                    //如果已开通权益，即可使用单笔转账功能
                    //mStartTranfer.inputCoin(getCoinList(TYPE_1_FOR_TRANSFER)).inputAddress(mOutBean).commit();

                    //如果已开通权益，即可使用多笔转账功
                    List<OutBean> outoutList = new ArrayList<>();
                    outoutList.add(mOutBean);
                    outoutList.add(mOutBean2);
                    outoutList.add(mOutBean3);

                    //TODO --------------------------------以下业务不能同时运行

                    //TODO 提交普通交易
                    //mStartTranfer.inputCoin(getCoinList(TYPE_1_FOR_TRANSFER)).inputAddressList(outoutList).commit();

                    //TODO 获取质押的裸交易数据
                    /*mStartTranfer.inputCoin(getCoinList(TYPE_1_FOR_TRANSFER)).getPledgeSignData(new ImpGetAction() {
                        @Override
                        public void receive(String actionStr) {
                            System.out.println("得到质押裸交易签名==="+actionStr);
                        }
                    });
                    */

                    //TODO 对零钱进行合并或拆额，如果自己的out为1个则为合并，如果自己的out为2个则为拆额 ,取整用 X*Long.valueOf(BASE_NUMBER)
                    //TODO 选某笔零钱进行拆额
                    //mStartTranfer.inputCoin(getCoinList(TYPE_1_FOR_TRANSFER).get(0)).inputExcreteCoins(3900l).commit();
                    //TODO 选多笔零钱进行合并
                    mStartTranfer.inputCoin(getCoinList(TYPE_1_FOR_TRANSFER)).inputExcreteCoins(133889333234000l).commit();
                } else{
                    //TODO 开通权益
                    mStartTranfer.openInterest(getCoinForMin("20","30"));
                }
            }

            @Override
            public void getCoinFail(Exception e) {
                System.out.println((e));
            }
        };

    }
}
