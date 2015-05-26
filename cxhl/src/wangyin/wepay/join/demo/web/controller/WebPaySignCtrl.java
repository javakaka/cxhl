package wangyin.wepay.join.demo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wangyin.wepay.join.demo.utils.DESUtil;
import wangyin.wepay.join.demo.utils.SignUtil;
import wangyin.wepay.join.demo.web.constant.MerchantConstant;
import wangyin.wepay.join.demo.web.domain.request.PaySignEntity;
import wangyin.wepay.join.demo.web.domain.request.WebPayReqDto;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wywangzhenlong on 14-5-8.
 * <p>
 * 模拟支付-商户签名
 * </p>
 */
@Controller
@RequestMapping(value = "/demo")
public class WebPaySignCtrl {

    @Resource
    private MerchantConstant merchantConstant;

    /**
     * @param webPayReqDto
     * @param httpServletRequest
     * @return
     */
    @RequestMapping(value = "/pay/sign", method = RequestMethod.POST)
    public String paySign(WebPayReqDto webPayReqDto, HttpServletRequest httpServletRequest) {
        PaySignEntity wePayMerchantSignReqDTO = new PaySignEntity();

        wePayMerchantSignReqDTO.setVersion(webPayReqDto.getVersion());
        wePayMerchantSignReqDTO.setToken(webPayReqDto.getToken());
        wePayMerchantSignReqDTO.setMerchantNum(webPayReqDto.getMerchantNum());
        wePayMerchantSignReqDTO.setTradeNum(webPayReqDto.getTradeNum());
        wePayMerchantSignReqDTO.setTradeTime(webPayReqDto.getTradeTime());
        wePayMerchantSignReqDTO.setTradeName(webPayReqDto.getTradeName());
        wePayMerchantSignReqDTO.setCurrency(webPayReqDto.getCurrency());
        wePayMerchantSignReqDTO.setMerchantRemark(webPayReqDto.getMerchantRemark());
        wePayMerchantSignReqDTO.setTradeAmount(webPayReqDto.getTradeAmount());
        wePayMerchantSignReqDTO.setTradeDescription(webPayReqDto.getTradeDescription());
        wePayMerchantSignReqDTO.setSuccessCallbackUrl(webPayReqDto.getSuccessCallbackUrl());
        wePayMerchantSignReqDTO.setFailCallbackUrl(webPayReqDto.getFailCallbackUrl());
        wePayMerchantSignReqDTO.setNotifyUrl(webPayReqDto.getNotifyUrl());

        /**
         * 商户签名
         */
        String signStr = SignUtil.sign(wePayMerchantSignReqDTO, merchantConstant.getPayRSAPrivateKey());
        webPayReqDto.setMerchantSign(signStr);

        if ("1.0".equals(webPayReqDto.getVersion())) {
            //敏感信息未加密
        } else if ("2.0".equals(webPayReqDto.getVersion())) {
            //敏感信息加密
            try {
                //获取商户 DESkey
                String desKey = merchantConstant.getMerchantDESKey();
                //对敏感信息进行 DES加密
                webPayReqDto.setMerchantRemark(DESUtil.encrypt(webPayReqDto.getMerchantRemark(), desKey, "UTF-8"));
                webPayReqDto.setTradeNum(DESUtil.encrypt(webPayReqDto.getTradeNum(), desKey, "UTF-8"));
                webPayReqDto.setTradeName(DESUtil.encrypt(webPayReqDto.getTradeName(), desKey, "UTF-8"));
                webPayReqDto.setTradeDescription(DESUtil.encrypt(webPayReqDto.getTradeDescription(), desKey, "UTF-8"));
                webPayReqDto.setTradeTime(DESUtil.encrypt(webPayReqDto.getTradeTime(), desKey, "UTF-8"));
                webPayReqDto.setTradeAmount(DESUtil.encrypt(webPayReqDto.getTradeAmount(), desKey, "UTF-8"));
                webPayReqDto.setCurrency(DESUtil.encrypt(webPayReqDto.getCurrency(), desKey, "UTF-8"));
                webPayReqDto.setNotifyUrl(DESUtil.encrypt(webPayReqDto.getNotifyUrl(), desKey, "UTF-8"));
                webPayReqDto.setSuccessCallbackUrl(DESUtil.encrypt(webPayReqDto.getSuccessCallbackUrl(), desKey, "UTF-8"));
                webPayReqDto.setFailCallbackUrl(DESUtil.encrypt(webPayReqDto.getFailCallbackUrl(), desKey, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        httpServletRequest.setAttribute("tradeAmount", wePayMerchantSignReqDTO.getTradeAmount());
        httpServletRequest.setAttribute("tradeName", wePayMerchantSignReqDTO.getTradeName());

        httpServletRequest.setAttribute("serverUrl", webPayReqDto.getServerUrl());
        httpServletRequest.setAttribute("tradeInfo", webPayReqDto);
        return "paySubmit";
    }

}
