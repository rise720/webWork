package com.cac.webWork.model;

/**
 * 
 * @ClassName: Result
 * @Description: restful接口统一返回json格式
 * @author JinWH
 * @date 2020年6月15日
 *
 * @param <T>
 */
public class Result<T> {
	
	private int errCode;	//错误代码，0为正确
	private String errMsg;  //错误原因，“”为正确
	private T data;         //返回数据
	
	private Result(T data){
        this.errCode = 0;
        this.errMsg = "";
        this.data = data;
    }
	
	private Result(int errCode, String errMsg) {
		if(errCode == 0) 			
			this.errMsg = "";
		else
			this.errMsg = errMsg == null ? "" : errMsg;
		this.errCode = errCode;
	}
	
	/**
	 * 
	 * @Title: success
	 * @Description: 调用成功方法
	 * @param @param data
	 * @param @return 参数
	 * @return Result<T> 返回类型
	 * @throws
	 */
	public static <T> Result<T> success(T data){
		return new Result<T>(data);
	}

	/**
	 * 
	 * @Title: fail
	 * @Description: 调用失败方法
	 * @param @param errCode
	 * @param @param errMsg
	 * @param @return 参数
	 * @return Result<T> 返回类型
	 * @throws
	 */
	public static <T> Result<T> fail(int errCode, String errMsg){
		return new Result<T>(errCode, errMsg);
	}

	public int getErrCode() {
		return errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public T getData() {
		return data;
	}
}
