package com.zheng.netty.game.codc;

import com.zheng.netty.game.constant.Constants;
import com.zheng.netty.game.model.Response;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * response解码器
 * <pre>
 * 数据包格式
 * +——----——+——-----——+——----——+——----——+——-----——+——-----——+
 * | 包头    | 模块号   | 命令号  |  状态码 |  长度    |   数据   |
 * +——----——+——-----——+——----——+——----——+——-----——+——-----——+
 * </pre>
 * 包头4字节
 * 模块号2字节short
 * 命令号2字节short
 * 长度4字节(描述数据部分字节长度)
 * 
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class ResponseDecoder extends FrameDecoder {
    private static final int BASE_LEN = 4 + 2 + 2 + 4 + 4;
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() < BASE_LEN) {
            return null;
        }
        int beginReader;
        while (true) {
            beginReader = buffer.readerIndex();
            if (buffer.readInt() == Constants.FLAG) {
                break;
            }
        }
        // 模块
        short module = buffer.readShort();
        // cmd
        short cmd = buffer.readShort();
        // 状态码
        int stateCode = buffer.readInt();
        // len
        int len = buffer.readInt();
        if (buffer.readableBytes() < len) {
            // 数据不完整,等待剩余部分数据
            buffer.readerIndex(beginReader);
            return null;
        }
        // 数据
        byte[] data = new byte[len];
        buffer.readBytes(data);

        Response response = new Response();
        response.setModule(module);
        response.setCmd(cmd);
        response.setStateCode(stateCode);
        response.setData(data);
        
        return response;
    }
}
