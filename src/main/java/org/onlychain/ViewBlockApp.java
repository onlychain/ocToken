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

        //获取指定高度的区块数据并解析成json,目前只保留actionType1和actionType4
        final long height=1349624;
        new GetBlockData(String.valueOf(height)) {
            @Override
            public void getSuccess(StringBuffer json,List<String> tradList) {
                System.out.println("通过API获取到指定高度的区块数据  ：  "+json);
                for (String temp:tradList)
                {
                    String result =  new HexConvertJson(temp).getJson();

                    if (result!=null)
                        System.out.println("得到高度: "+height+" 的 [tradingInfo] 数据，且"+"TYPE为"+
                                temp.substring(0,2)+"的数据 :  "
                                +result);
                }
            }
        };


        //对指定的序列化Action数据进行解析,格式为json
        System.out.println();
        System.out.println("指定Action解析 ： "+new HexConvertJson("010101011cd82a95c96b1746df83f9987550d60a391fbe4e19a98702530164827373612f0d006a4730450221009ba3732cc991075c7053dde71e5adae9a2a4cdee9f00fe6181c426f40e8aeab70220543905af96b21d207a8c4129c9f1401a7c4f991dec3bccd4e1578dadcd772039210382d1ca55c9d65fddb18b2779b17d0d20467df82b85981794bb94d2eea03a6d3c0100000000244B7716001876a914bf3a2cfbcade5cf8ab114bf669a9f2ffeae67f88ac00000000000000000000f39fb3ff05e2a7d0070382d1ca55c9d65fddb18b2779b17d0d20467df82b85981794bb94d2eea03a6d3c01003045022100e4f8a9668f76e2ce2e21fd0c0e2c0f17160c3b67ef7a5cdaca475ccf2690b078022027300c894bbc213e77cc35f7ce0bf8b8140933012ae6f839f0303e7a882caaab").getJson());
    }
}
//{"message":"01010101cdb743971acc37ee83bc746f0a4ac91ce260ac773dbfb29919f689d6bbce5cf201006b483046022100e41fb8fda9d0466dd14a9057055a55d61b53b9e1a16b7b63f4f09c81b74f07c0022100cea93cc3d236ef8623dc3dea25b265b46c0f53d11722a146d0c1dc55e80cc3382103aa402d9703ef1a2c0ba665a638cfa7080059ec10f9e54be2bf7eab079167bed90200000000000186a0001976a9143b349701a36b457339e53ead5159750efc1f2a8988ac0000000000000000001976a914e462552cfde2d0eae4d696304b51588cb8b33cd688ac00000000000000000000d5d49dfa05b8ac5203aa402d9703ef1a2c0ba665a638cfa7080059ec10f9e54be2bf7eab079167bed90100","sigStr":"3045022100c19e477234f5487a3b952f20faf4cdc66f3d0054df499008ebd573b27cbf050302202e4212e5980d85287a57c3f7e827f282a6ffa415ab6ea125daa46cb57a64f973"}