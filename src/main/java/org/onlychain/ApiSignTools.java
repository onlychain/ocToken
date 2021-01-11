package org.onlychain;

import org.onlychain.bean.AccountBean;
import org.onlychain.bean.BaseActionBean;
import org.onlychain.utils.OcMath;
import org.onlychain.utils.WalletUtils;

/**
 * 对自定义消息进行加签和解签
 * 使用场景：只允许来自本人的私钥（公钥、地址）身份进行访问自己数据，避免有人通过接口可以查看他人数据
 */
public class ApiSignTools {
    public static void main(String[] args) {
        String pri="c7c0e51106b3a63a9ccf547c1f391493e13a12950c7d25b6ecbcc8bff112d9ab";
        AccountBean mAccountBean= WalletUtils.createAccount(OcMath.hexStringToByteArray(pri));
        String body="伟大";
        String  signAsStr=new BaseActionBean().makeSign(mAccountBean.getPrivateKeyBin(),body);

        boolean s=new BaseActionBean().checkSign(mAccountBean.getPublicKeyBin(),body,signAsStr);
        System.out.println(s);
        System.out.println(signAsStr);
    }
}
