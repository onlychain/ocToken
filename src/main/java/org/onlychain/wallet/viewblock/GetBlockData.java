package org.onlychain.wallet.viewblock;

import com.alibaba.fastjson.JSON;
import org.onlychain.bean.BlockBean;
import org.onlychain.net.Request;
import org.onlychain.wallet.base.ApiConfig;

import java.util.List;

public abstract class GetBlockData {
    private String height;
    public abstract void getSuccess(StringBuffer json,List<String> tradList);


    public GetBlockData(String height) {
        this.height = height;
        queryBlock();
    }


    private void queryBlock(){
        new Request(ApiConfig.API_queryBlock,ApiConfig.queryBlock(height)) {
            @Override
            public void success(StringBuffer json) {

                BlockBean.RecordBean mRecord= JSON.parseObject(json.toString(), BlockBean.class).getRecord();
                getSuccess(json,mRecord.getTradingInfo());
            }
            @Override
            public void fail(Exception e) {

            }
        };

    }

}
