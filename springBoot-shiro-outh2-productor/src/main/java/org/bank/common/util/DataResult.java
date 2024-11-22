package org.bank.common.util;

import lombok.Data;
import org.bank.common.exception.code.BaseResponseCode;
import org.bank.common.exception.code.ResponseCodeInterface;

/**
 * @BelongsProject: BankCloud
 * @BelongsPackage: org.bank.utils
 * @Author: lizongle
 * @Description:
 * @Version: 1.0
 */
@Data
public class DataResult<T> {
    /**
     * 请求响应code，0为成功 其他为失败
     */
    private int code;

    /**
     * 响应异常码详细信息
     */
    private String msg;

    /**
     * 响应内容 ， code 0 时为 返回 数据
     */
    private T data;

    public DataResult(int code, T data) {
        this.code = code;
        this.data = data;
        this.msg=null;
    }

    public DataResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DataResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data=null;
    }


    public DataResult() {
        this.code= BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
        this.data=null;
    }

    /**
     * @description:
     * @author: lizongle
     * @param: [data]
     * @return:
     **/
    public DataResult(T data) {
        this.data = data;
        this.code=BaseResponseCode.SUCCESS.getCode();
        this.msg=BaseResponseCode.SUCCESS.getMsg();
    }

    /**
     * @description:
     * @author: lizongle
     * @param: [responseCodeInterface]
     * @return:
     **/
    public DataResult(ResponseCodeInterface responseCodeInterface) {
        this.data = null;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }

    /**
     * @description:
     * @author: lizongle
     * @param: [responseCodeInterface, data]
     * @return:
     **/
    public DataResult(ResponseCodeInterface responseCodeInterface, T data) {
        this.data = data;
        this.code = responseCodeInterface.getCode();
        this.msg = responseCodeInterface.getMsg();
    }

    /**
     * @description:操作成功 data为null
     * @author: lizongle
     * @param: []
     * @return: org.bank.utils.DataResult
     **/
    public static <T>DataResult<T> success(){
        return new <T>DataResult<T>();
    }

    /**
     * @description:操作成功 data 不为null
     * @author: lizongle
     * @param: [data]
     * @return: org.bank.utils.DataResult
     **/
    public static <T>DataResult<T> success(T data){
        return new <T>DataResult<T>(data);
    }

    /**
     * @description:自定义 返回操作 data 可控
     * @author: lizongle
     * @param: [code, msg, data]
     * @return: org.bank.utils.DataResult
     **/
    public static <T>DataResult<T> getResult(int code, String msg, T data){
        return new <T>DataResult<T>(code,msg,data);
    }


    /**
     * @description:自定义返回  data为null
     * @author: lizongle
     * @param: [code, msg]
     * @return: org.bank.utils.DataResult
     **/
    public static <T>DataResult<T> getResult(int code, String msg){
        return new <T>DataResult<T>(code,msg);
    }

    /**
     * @description:自定义返回 入参一般是异常code枚举 data为空
     * @author: lizongle
     * @param: [responseCode]
     * @return: org.bank.utils.DataResult
     **/
    public static <T>DataResult<T> getResult(BaseResponseCode responseCode){
        return new <T>DataResult<T>(responseCode);
    }

    /**
     * @description:自定义返回 入参一般是异常code枚举 data 可控
     * @author: lizongle
     * @param: [responseCode, data]
     * @return: org.bank.utils.DataResult
     **/
    public static <T>DataResult<T> getResult(BaseResponseCode responseCode, T data){

        return new <T>DataResult<T>(responseCode,data);
    }
}
