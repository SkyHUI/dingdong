package com.sky.bean;

/**
 * 
 * @Package: com.sky.beam
 * @ClassName: Result 
 * @Description:  客户请求结果实体类
 * @author: 曾朋辉
 * @date: 2017年3月21日 上午11:52:00 
 *
 */
public class Result {

	/**
	 * 请求成功
	 */
	public static final int RESULT_OK = 1;
	/**
	 * 请求失败
	 */
	public static final int RESULT_FAIL=0;

	/**
	 * 身份验证过期
	 */
	public static final int RESULT_TOKEN_FAIL = -1;

	private int success;	
	
	private String message;

	public Result() {
		super();
	}

	public Result(int success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Result [success=" + success + ", message=" + message + "]";
	}
	
	
	
	
	
}
