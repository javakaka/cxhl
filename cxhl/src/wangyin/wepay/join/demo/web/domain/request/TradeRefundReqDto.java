package wangyin.wepay.join.demo.web.domain.request;


import java.io.Serializable;

/**
 * Created by lijunfu on 14-4-29.
 * <p/>
 * 退款请求
 */
public class TradeRefundReqDto implements Serializable {
    /**
     * 访问接口版本号
     */
    public String version;
    /**
     * 交易号 数字或字母
     */
    private String tradeNum;
    /**
     * 原交易号 数字或字母
     */
    private String oTradeNum;
    /**
     * 交易金额 单位 分
     */
    private int tradeAmount;
    /**
     * 交易币种 固定值 CNY
     */
    private String tradeCurrency;
    /**
     * 交易日期 yyyyMMdd
     */
    private String tradeDate;
    /**
     * 交易时间 HHmmss
     */
    private String tradeTime;
    /**
     * 交易通知地址
     */
    private String tradeNotice;
    /**
     * 交易备注
     */
    private String tradeNote;
    /**
     * 商户号
     */
    private String merchantNum;
    /**
     * 商户签名
     */
    private String merchantSign;


    public String getTradeNum() {
        return tradeNum;
    }

    public void setTradeNum(String tradeNum) {
        this.tradeNum = tradeNum;
    }

    public String getoTradeNum() {
        return oTradeNum;
    }

    public void setoTradeNum(String oTradeNum) {
        this.oTradeNum = oTradeNum;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(int tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeCurrency() {
        return tradeCurrency;
    }

    public void setTradeCurrency(String tradeCurrency) {
        this.tradeCurrency = tradeCurrency;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeNotice() {
        return tradeNotice;
    }

    public void setTradeNotice(String tradeNotice) {
        this.tradeNotice = tradeNotice;
    }

    public String getTradeNote() {
        return tradeNote;
    }

    public void setTradeNote(String tradeNote) {
        this.tradeNote = tradeNote;
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
