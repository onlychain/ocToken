package org.onlychain.bean;


import java.util.List;

public class TransActionBean {
    //版本号只占用1个字节，因此最高为255，都是整数。十六进制最高为ff，最低为01，占两个字符
    private int version;
    //Action类型只占用1个字节，因此最高为255，都是整数。十六进制最高为ff，最低为01，占两个字符
    private String actionType;
    //判断是否有交易标识：占用1个字节，00代表没有交易(trading字段)数据，01代表有交易数据
    private String ins;
    //交易数据(如果是否有标识字段为00，则没有这部分序列化内容)：
    private TradingBean trading;
    private ActionBean action;

    private int time;
    private int createdBlock;
    private String originator;
    private String actionSign;
    private String txId;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public TradingBean getTrading() {
        return trading;
    }

    public void setTrading(TradingBean trading) {
        this.trading = trading;
    }

    public ActionBean getAction() {
        return action;
    }

    public void setAction(ActionBean action) {
        this.action = action;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCreatedBlock() {
        return createdBlock;
    }

    public void setCreatedBlock(int createdBlock) {
        this.createdBlock = createdBlock;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public String getIns() {
        return ins;
    }

    public void setIns(String ins) {
        this.ins = ins;
    }

    public String getActionSign() {
        return actionSign;
    }

    public void setActionSign(String actionSign) {
        this.actionSign = actionSign;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public static class TradingBean {
        private long lockTime;
        private long cost;
        private List<VinBean> vin;
        private List<VoutBean> vout;

        public long getLockTime() {
            return lockTime;
        }

        public void setLockTime(long lockTime) {
            this.lockTime = lockTime;
        }

        public long getCost() {
            return cost;
        }

        public void setCost(long cost) {
            this.cost = cost;
        }

        public List<VinBean> getVin() {
            return vin;
        }

        public void setVin(List<VinBean> vin) {
            this.vin = vin;
        }

        public List<VoutBean> getVout() {
            return vout;
        }

        public void setVout(List<VoutBean> vout) {
            this.vout = vout;
        }

        public static class VinBean {
            private String txId;



            //交易输入数量：引用的交易输入数量，采用LEB128编码规则动态增加字节数量。
            private String n;
            private String scriptSig;

            //交易输入内容：
            //每个交易输入都有以下字段组成，因为一笔交易可能有多个交易输入，因此交易输入内容由多个以下字段拼接而成，如：交易输入1  + 交易输入2 + 交易输入3 + 交易输入4 ...
            /**
             * 交易输入内容开始
             * 交易引用hash：该交易所引用的交易哈希，字段名为txId，32个字节64个十六进制字符。
             *
             * 交易引用索引：动态长度，采用LEB128编码规则。
             *
             * 交易解锁脚本长度：交易解锁脚本长度为标明该交易输出所要使用的交易解锁脚本长度，长度为2个字节4个十六进制字符。因此交易解锁脚本最长有65535字节的长度。
             * 交易解锁脚本：交易解锁脚本长度不确定的，但是不会超过65535个字节。在进行交易解锁脚本编码规则生成的时候需要考虑清楚。
             * 交易输入内容结束
             */
            private String coinbase;

            public String getTxId() {
                return txId;
            }

            public void setTxId(String txId) {
                this.txId = txId;
            }

            public String getScriptSig() {
                return scriptSig;
            }

            public void setScriptSig(String scriptSig) {
                this.scriptSig = scriptSig;
            }

            public String getN() {
                return n;
            }

            public void setN(String n) {
                this.n = n;
            }

            public String getCoinbase() {
                return coinbase;
            }

            public void setCoinbase(String coinbase) {
                this.coinbase = coinbase;
            }
        }

        public static class VoutBean {
            private long value;
            private int n;
            private String reqSigs;
            private String address;

            public long getValue() {
                return value;
            }

            public void setValue(long value) {
                this.value = value;
            }

            public int getN() {
                return n;
            }

            public void setN(int n) {
                this.n = n;
            }

            public String getReqSigs() {
                return reqSigs;
            }

            public void setReqSigs(String reqSigs) {
                this.reqSigs = reqSigs;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }
        }
    }

    public static class ActionBean {
        private String pledge;
        private String pledgeNode;
        private String ip;
        private int port;

        public String getPledge() {
            return pledge;
        }

        public void setPledge(String pledge) {
            this.pledge = pledge;
        }

        public String getPledgeNode() {
            return pledgeNode;
        }

        public void setPledgeNode(String pledgeNode) {
            this.pledgeNode = pledgeNode;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}
