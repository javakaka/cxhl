package wangyin.wepay.join.demo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.misc.BASE64Decoder;
import wangyin.wepay.join.demo.utils.*;
import wangyin.wepay.join.demo.web.constant.MerchantConstant;
import wangyin.wepay.join.demo.web.domain.request.TradeQueryEntity;
import wangyin.wepay.join.demo.web.domain.request.TradeQueryReqDto;
import wangyin.wepay.join.demo.web.domain.response.BaseResponseDto;
import wangyin.wepay.join.demo.web.domain.response.QueryResultTradeEntity;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wywangzhenlong on 14-8-9.
 */
@Controller
@RequestMapping(value = "/demo")
public class WebQuerySign {

    @Resource
    private MerchantConstant merchantConstant;

    @RequestMapping(value = "/query/sign", method = RequestMethod.POST)
    public String paySign(TradeQueryReqDto tradeQueryReqDto, HttpServletRequest httpServletRequest) {

        String decrypData="";
        String tradeJsonData = "{\"tradeNum\": \"" + tradeQueryReqDto.getTradeNum() + "\"}";
        try {
            //1.对交易信息进行3DES加密
            String threeDesData = TDESUtil.encrypt2HexStr(RSACoder.decryptBASE64(merchantConstant.getMerchantDESKey()), tradeJsonData);

            //2.对3DES加密的数据进行签名
            String sha256Data = SHAUtil.Encrypt(threeDesData, null);
            byte[] rsaResult = RSACoder.encryptByPrivateKey(sha256Data.getBytes(), merchantConstant.getPayRSAPrivateKey());
            String merchantSign = RSACoder.encryptBASE64(rsaResult);

            //3.构造最终交易查询请求json
            TradeQueryEntity queryTradeDTO = new TradeQueryEntity();
            queryTradeDTO.setVersion(tradeQueryReqDto.getVersion());
            queryTradeDTO.setMerchantNum(tradeQueryReqDto.getMerchantNum());
            queryTradeDTO.setMerchantSign(FormatUtil.stringBlank(merchantSign));
            queryTradeDTO.setData(threeDesData);

            String json = JsonUtil.write2JsonStr(queryTradeDTO);

            //4.发送请求
            String resultJsonData = HttpsClientUtil.sendRequest(merchantConstant.getWangyinServerQueryUrl(), json);

            //5.验签返回数据
            BaseResponseDto<Map<String, Object>> result = (BaseResponseDto<Map<String, Object>>) JsonUtil.json2Object(resultJsonData, BaseResponseDto.class);

            //查询状态 成功
            if (result.getResultCode() == 0) {
                Map<String, Object> mapResult =  result.getResultData();
                //有返回数据
                if (null != mapResult) {
                    String data = mapResult.get("data").toString();
                    String sign = mapResult.get("sign").toString();
                    //1.解密签名内容
                    byte[] decryptBASE64Arr = new BASE64Decoder().decodeBuffer(sign);
                    byte[] decryptArr = RSACoder.decryptByPublicKey(decryptBASE64Arr, merchantConstant.getCommonRSAPublicKey());
                    String decryptStr = ByteUtil.byte2HexString(decryptArr);

                    //2.对data进行sha256摘要加密
                    String sha256SourceSignString = ByteUtil.byte2HexLowerCase(SHA256Util.encrypt(data.getBytes("UTF-8")));

                    //3.比对结果
                    if (decryptStr.equals(sha256SourceSignString)) {
                        /**
                         * 验签通过
                         */
                        //解密data
                        decrypData = TDESUtil.decrypt4HexStr(RSACoder.decryptBASE64(merchantConstant.getMerchantDESKey()), data);

                        //注意 结果为List集合
                        List<Map<String,Object>> resultList=JsonUtil.jsonArray2List(decrypData);

                        List<QueryResultTradeEntity> viewList=new ArrayList<QueryResultTradeEntity>();
                        for(Map<String ,Object> m:resultList ){
                            QueryResultTradeEntity qrte=new QueryResultTradeEntity();
                            qrte.setTradeCurrency(m.get("tradeCurrency")+"");
                            qrte.setTradeDate(m.get("tradeDate") + "");
                            qrte.setTradeTime(m.get("tradeTime") + "");
                            qrte.setTradeAmount(Integer.parseInt(m.get("tradeAmount") + ""));
                            qrte.setTradeNote(m.get("tradeNote") + "");
                            qrte.setTradeNum(m.get("tradeNum") + "");
                            qrte.setTradeStatus(m.get("tradeStatus")+"");
                            viewList.add(qrte);
                        }
                        httpServletRequest.setAttribute("viewList", viewList);
                        //错误消息
                        if(resultList.size()<1){
                            httpServletRequest.setAttribute("errorMsg", decrypData);
                        }
                    } else {

                        /**
                         * 验签失败  不受信任的响应数据
                         * 终止
                         */
                        return "";
                    }
                }
            }
            //执行查询 失败
            else{
                httpServletRequest.setAttribute("errorMsg", result.getResultMsg());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "queryResult";
    }
}