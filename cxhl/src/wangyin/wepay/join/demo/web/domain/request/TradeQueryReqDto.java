package wangyin.wepay.join.demo.web.domain.request;

import java.io.Serializable;

/**
 * Created by lijunfu on 14-4-28.
 * <p>
 * 交易查询
 * </p>
 */
public class TradeQueryReqDto implements Serializable {
    /**
     * 访问接口版本号
     */
    public String version;

    /**
     * 交易号 数字或字母 , 多个交易号请用英文逗号隔开
     * eg: 交易号1,交易号2,交易号3
     */
    private String tradeNum;
    /**
     * 商户号
     */
    private String merchantNum;
    /**
     * 商户签名
     */
    private String merchantSign;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
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
}
