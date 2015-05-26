package wangyin.wepay.join.demo.web.domain.request;

import java.io.Serializable;

/**
 * Created by wywangzhenlong on 14-8-9.
 */
public class TradeRefundEntity implements Serializable {

    private String version="";
    private String merchantNum="";
    private  String merchantSign="";
    private String data="";

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerchantNum() {
        return merchantNum;
    }

    public void setMerchantNum(String merchantNum) {
        this.merchantNum = merchantNum;
    }

    public String getMerchantSign() {
        return merchantSign;
    }

    public void setMerchantSign(String merchantSign) {
        this.merchantSign = merchantSign;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
