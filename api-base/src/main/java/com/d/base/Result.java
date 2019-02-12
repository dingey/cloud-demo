package com.d.base;

import com.d.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.beans.Transient;

@Slf4j
@Getter
@Setter
@SuppressWarnings("unused")
public class Result<T> {

	@ApiModelProperty("错误代码：0成功；其他失败")
	private int code;

	@ApiModelProperty("错误提示")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String message;

	@ApiModelProperty("数据")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	@ApiModelProperty("结果是否异步标识：1或空同步；0异步；")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer sync;

	public Result(int code, T data, String message) {
		this.code = code;
		this.data = data;
		this.message = message;
	}

	public Result(int code, String message, T data, int sync) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
		this.sync = sync;
	}

	/**
	 * 同步处理成功
	 * 
	 * @param data 数据
	 * @return 结果
	 */
	public static <T> Result<T> success(T data) {
		return new Result<T>(0, data, null);
	}

	/**
	 * 已提交异步处理，需要轮询接口判断是否实际成功
	 * 
	 * @param data 数据
	 * @return 结果
	 */
	public static <T> Result<T> successAsync(T data) {
		return new Result<T>(0, null, data, 0);
	}

	public static <T> Result<T> fail(int code) {
		if (code < 1) {
			throw new RuntimeException("code must large than 0.");
		}
		ResultCode resultCode = ResultCode.fromCode(code);
		if (resultCode != null) {
			return fail(resultCode);
		}
		return new Result<T>(code, null, null);
	}

	public static <T> Result<T> fail(String message) {
		return new Result<T>(0, null, message);
	}

	public static <T> Result<T> fail(ResultCode code) {
		return new Result<T>(code.getCode(), null, code.getMessage());
	}

	@Transient
	public boolean success() {
		return code == 0;
	}

	@Transient
	public boolean async() {
		return sync == null || sync == 0;
	}

	@Override
	public String toString() {
		return JsonUtil.single().toJson(this);
	}
}
