package com.xuecheng.base.exception;

public enum CommonError {

    UNKOWN_ERROR("执行过程异常"),
    PARAMS_ERROR("非法参数"),
    OBJECT_NULL("空对象"),
    QUERY_NULL("查询为空"),
    REQUEST_NULL("请求参数为空");

    private String errMessage;

    public String getErrMessage() {
        return errMessage;
    }

    private CommonError(String errMessage) {
        this.errMessage = errMessage;
    }
}
