package com.zheng.netty.game.codc;

import com.zheng.netty.game.constant.Constants;
import com.zheng.netty.game.model.Request;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * 请求解码器
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+
 * | 包头    | 模块号   | 命令号  |  长度   |   数据   |
 * +——----——+——-----——+——----——+——----——+——-----——+
 * </pre>
 * 包头4字节
 * 模块号2字节short
 * 命令号2字节short
 * 长度4字节(描述数据部分字节长度)
 * 
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class RequestDecoder extends FrameDecoder {
    
    public static final int BASE_LEN = 4 + 2 + 2 + 4;
    
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        // 获取正常的包信息，可能发生拆包粘包问题导致数据包消息格式不正确
        if (buffer.readableBytes() < BASE_LEN) {
            // 返回null会等待接受剩下的消息
            return null;
        }
        // 记录包头读取的位置
        int beginReader;
        while (true) {
            beginReader = buffer.readerIndex();
            if (buffer.readInt() == Constants.FLAG) {
                break;
            }
        }
        //模块号
        short module = buffer.readShort();
        //命令号
        short cmd = buffer.readShort();
        //长度
        int length = buffer.readInt();
        //判断请求数据包数据是否到齐
        if(buffer.readableBytes() < length){
            //还原读指针
            buffer.readerIndex(beginReader);
            return null;
        }
        //读取data数据
        byte[] data = new byte[length];
        buffer.readBytes(data);
        
        Request request = new Request();
        request.setModule(module);
        request.setCmd(cmd);
        request.setData(data);

        //继续往下传递 
        return request;
    }
}
