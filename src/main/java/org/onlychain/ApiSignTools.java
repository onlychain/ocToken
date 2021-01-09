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
        String body="010401011aaea3ca7f1c55967907869884386ac47f291a2cb618f5052385a787bb4e7ddb000069463044022011a1da731e321c0954fe0a0165b5033629d77367df53a0880d6b66e196574b67022075b4d95ff9aefa9d7ee9dc292afad911891e7db69375446bbbedc655b3c0bfe52102f978d051f8b42c80d7ee530c55d5ae84a110bc01775c0f36bcbc0223c9a74548010000000077359400001976a9149cc2f63ec6cc0f9bcb9796bd2cf8f9d10396e84188ac959da208000000000000000000d2dee5ff05d4e95f02f978d051f8b42c80d7ee530c55d5ae84a110bc01775c0f36bcbc0223c9a745480100";
        String  signAsStr=new BaseActionBean().makeSign(mAccountBean.getPrivateKeyBin(),body);

        boolean s=new BaseActionBean().checkSign(mAccountBean.getPublicKeyBin(),body,signAsStr);
        System.out.println(s);
        System.out.println(signAsStr);
    }
}
