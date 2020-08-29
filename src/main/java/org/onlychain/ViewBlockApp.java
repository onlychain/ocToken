package org.onlychain;

import org.onlychain.wallet.viewblock.GetBlockData;
import org.onlychain.wallet.viewblock.HexConvertJson;

import java.util.List;

/**
 * 16进制Action本地解析，目前只支持actionType1和actionType4
 */
public class ViewBlockApp {
    public static void main(String[] args) {

  new GetBlockData("1349624") {
           @Override
           public void getSuccess(List<String> tradList) {
               for (String temp:tradList)
               {
                 String result =  new HexConvertJson(temp).getJson();
                 if (result!=null)
                   System.out.println("-------"+result);
               }
           }
       };
    }
}
//{"message":"01010101cdb743971acc37ee83bc746f0a4ac91ce260ac773dbfb29919f689d6bbce5cf201006b483046022100e41fb8fda9d0466dd14a9057055a55d61b53b9e1a16b7b63f4f09c81b74f07c0022100cea93cc3d236ef8623dc3dea25b265b46c0f53d11722a146d0c1dc55e80cc3382103aa402d9703ef1a2c0ba665a638cfa7080059ec10f9e54be2bf7eab079167bed90200000000000186a0001976a9143b349701a36b457339e53ead5159750efc1f2a8988ac0000000000000000001976a914e462552cfde2d0eae4d696304b51588cb8b33cd688ac00000000000000000000d5d49dfa05b8ac5203aa402d9703ef1a2c0ba665a638cfa7080059ec10f9e54be2bf7eab079167bed90100","sigStr":"3045022100c19e477234f5487a3b952f20faf4cdc66f3d0054df499008ebd573b27cbf050302202e4212e5980d85287a57c3f7e827f282a6ffa415ab6ea125daa46cb57a64f973"}