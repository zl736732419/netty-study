package com.zheng.netty.game.model;

import java.util.Arrays;

/**
 * 网络通信底层响应实体
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class Response {
    /**
     * 请求模块
     */
    private short module;

    /**
     * 命令号
     */
    private short cmd;

    /**
     * 状态码
     */
    private int stateCode;

    /**
     * 数据部分
     */
    private byte[] data;

    public short getModule() {
        return module;
    }

    public void setModule(short module) {
        this.module = module;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataLen() {
        if (null == data) {
            return 0;
        } else {
            return data.length;
        }
    }
    
    @Override
    public String toString() {
        return "Response{" +
                "module=" + module +
                ", cmd=" + cmd +
                ", stateCode=" + stateCode +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
