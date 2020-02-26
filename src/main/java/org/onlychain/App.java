package org.onlychain;

import org.onlychain.wallet.tranfer.GetVinCoin;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new GetVinCoin("5f378522c9190a4058477c1b329eb52834d35b02") {
            @Override
            public void walletCoinList(StringBuffer json) {
                System.out.println(json);
                getCoinList();



            }

            @Override
            public void getCoinFail(Exception e) {

            }


        };
    }
}
