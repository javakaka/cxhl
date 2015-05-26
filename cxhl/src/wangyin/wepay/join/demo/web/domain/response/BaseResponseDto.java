package wangyin.wepay.join.demo.web.domain.response;

/**
 * @author lijunfu
 * @version 1.0.0
 * @since 2013-7-30
 * @comment BaseResponseDto.java
 */

public class BaseResponseDto<T>{

	private Integer resultCode;

	private String resultMsg;

	private T resultData;

	public Integer getResultCode(){
		return resultCode;
	}

	public void setResultCode(Integer resultCode){
		this.resultCode = resultCode;
	}

	public String getResultMsg(){
		return resultMsg;
	}

	public void setResultMsg(String resultMsg){
		this.resultMsg = resultMsg;
	}

	public T getResultData(){
		return resultData;
	}

	public void setResultData(T resultData){
		this.resultData = resultData;
	}

}
