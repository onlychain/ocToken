package org.onlychain;

import org.onlychain.wallet.base.ApiConfig;
import org.onlychain.wallet.viewblock.GetBlockData;
import org.onlychain.wallet.viewblock.HexConvertJson;

import java.util.List;

/**
 * 16进制Action本地解析，目前只支持actionType1和actionType4
 */
public class ViewBlockApp {
    public static void main(String[] args) {
        //更换节点IP
        ApiConfig.init("http://39.98.135.66:9082");

  new GetBlockData("1349624") {
           @Override
           public void getSuccess(List<String> tradList) {
               for (String temp:tradList)
               {
                 String result =  new HexConvertJson("010401011aaea3ca7f1c55967907869884386ac47f291a2cb618f5052385a787bb4e7ddb00006b483046022100dc73afb3e9c1e0612b2f63d836bc96c5b165328f5c4ad5d1f547b9aa5e740c0a022100f6a8a7be3387fb6da3e2b422d875ffd9a7916d9996abd512196bee5231837a62210355c48844bb6392dbd251167c92904eabbbc1982b39dc7ebcb2141063f0281e1f010000000077359400001976a9143b349701a36b457339e53ead5159750efc1f2a8988acb6bcb88906000000000000000000b6d5b3fa05e9ac5d0355c48844bb6392dbd251167c92904eabbbc1982b39dc7ebcb2141063f0281e1f0100304502207f1070c3270c0996dc6cb55c68859bfe42b25168181524008a86880c37136f030221008972bd00bfaa227cb2ccf8d3af390b843e7d6092ea4001a4ad32da1f20057c6c").getJson();
                 if (result!=null)
                   System.out.println("-------"+result);
               }
           }
       };
    }
}
//{"message":"01010101cdb743971acc37ee83bc746f0a4ac91ce260ac773dbfb29919f689d6bbce5cf201006b483046022100e41fb8fda9d0466dd14a9057055a55d61b53b9e1a16b7b63f4f09c81b74f07c0022100cea93cc3d236ef8623dc3dea25b265b46c0f53d11722a146d0c1dc55e80cc3382103aa402d9703ef1a2c0ba665a638cfa7080059ec10f9e54be2bf7eab079167bed90200000000000186a0001976a9143b349701a36b457339e53ead5159750efc1f2a8988ac0000000000000000001976a914e462552cfde2d0eae4d696304b51588cb8b33cd688ac00000000000000000000d5d49dfa05b8ac5203aa402d9703ef1a2c0ba665a638cfa7080059ec10f9e54be2bf7eab079167bed90100","sigStr":"3045022100c19e477234f5487a3b952f20faf4cdc66f3d0054df499008ebd573b27cbf050302202e4212e5980d85287a57c3f7e827f282a6ffa415ab6ea125daa46cb57a64f973"}