package org.onlychain.wallet.tranfer;

import org.onlychain.bean.*;
import org.onlychain.crypto.Hash;
import org.onlychain.secp256k1.Secp256k1;
import org.onlychain.secp256k1.Signature;
import org.onlychain.utils.Leb128Utils;
import org.onlychain.utils.OcMath;
import org.onlychain.utils.WalletUtils;

import java.math.BigInteger;
import java.util.List;

public class MakeAction {
    AccountBean mAccountBean;
    List<PurseBean.RecordBean> coinList;
    List<OutBean> outList;
    public MakeAction(AccountBean mAccountBean, List<PurseBean.RecordBean> coinList, List<OutBean> outList) {
        this.mAccountBean = mAccountBean;
        this.coinList = coinList;
        this.outList=outList;
    }

    /**
     * 普通交易
     * @return
     */
    public BaseActionBean createActionForType1(String height){
        HeadBean head=new HeadBean(1,setVInBody()+setVOutBody());
        HeadBean.EndBean mEndBean = new HeadBean.EndBean(height,mAccountBean.getPublicKey(),head.toString());

        String result=mEndBean.getResult();
        String sigStr = OcMath.toHexStringNoPrefix(Secp256k1.sign(mAccountBean.getPrivateKeyBin(),  Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(result)))).serialize());
        BaseActionBean mBaseActionBean=new BaseActionBean();
        mBaseActionBean.setMessage(result);
        mBaseActionBean.setSigStr(sigStr);
//        mBaseActionBean.setTxid(WalletUtils.getTxId(result));
        return mBaseActionBean;
    }

    /**
     * 开通权益
     * @param height
     * @return
     */
    public BaseActionBean createActionForType4(String height){
        HeadBean head=new HeadBean(4,setVInBody()+setVOutBody());
        HeadBean.EndBean mEndBean = new HeadBean.EndBean(height,mAccountBean.getPublicKey(),head.toString());
        mEndBean.setLockTimeAdd1Year();

        String result=mEndBean.getResult();
        String sigStr = OcMath.toHexStringNoPrefix(Secp256k1.sign(mAccountBean.getPrivateKeyBin(), OcMath.hexStringToByteArray(result)).serialize());
        BaseActionBean mBaseActionBean=new BaseActionBean();
        mBaseActionBean.setMessage(result+sigStr);
//        mBaseActionBean.setTxid(WalletUtils.getTxId(result));
        return mBaseActionBean;
    }

      private String setVInBody(){
        StringBuffer result=new StringBuffer();
        //LEB128 输入笔数
        String number= Leb128Utils.encodeUleb128(coinList.size());
        for(PurseBean.RecordBean coin:coinList)
        {
            String txN=Leb128Utils.encodeUleb128(coin.getN());
            String scriptContent = coin.getTxId()+txN+Script.getLockScript(mAccountBean.getAddressNoPrefix());
            System.out.println("text--------------"+scriptContent);

            scriptContent= Script.vInScript(mAccountBean,Hash.sha256(Hash.sha256(OcMath.hexStringToByteArray(scriptContent))));
            result = result.append(coin.getTxId()).append(txN).append(scriptContent);
        }
        return number+result.toString();
    }


    private String setVOutBody(){
        StringBuffer result=new StringBuffer();
        //LEB128 输出笔数
        String number= Leb128Utils.encodeUleb128(outList.size());
        for(OutBean out:outList)
        {
            result = result.append(OcMath.toHexStringNoPrefixZeroPadded(new BigInteger(String.valueOf(out.getValue())),16)).append(Script.vOutScript(out.getAddress()));
        }
        return number+result.toString();
    }



}
