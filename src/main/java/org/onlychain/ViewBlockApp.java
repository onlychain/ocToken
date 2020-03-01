package org.onlychain;

import org.onlychain.wallet.viewblock.GetBlockData;
import org.onlychain.wallet.viewblock.HexConvertJson;

import java.util.List;

/**
 * 16进制Action本地解析，目前只支持actionType1和actionType4
 */
public class ViewBlockApp {
    public static void main(String[] args) {

       new GetBlockData("2038301") {
           @Override
           public void getSuccess(List<String> tradList) {
               for (String temp:tradList)
               {
                 String result =  new HexConvertJson(temp).getJson();
                 if (result!=null)
                   System.out.println(result);
               }
           }
       };
    }
}
