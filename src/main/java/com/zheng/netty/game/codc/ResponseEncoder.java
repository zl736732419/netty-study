package com.zheng.netty.game.codc;

import com.zheng.netty.game.constant.Constants;
import com.zheng.netty.game.model.Response;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class ResponseEncoder extends OneToOneEncoder {
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        Response response = (Response) msg;
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        //包头
        buffer.writeInt(Constants.FLAG);
        //module
        buffer.writeShort(response.getModule());
        //cmd
        buffer.writeShort(response.getCmd());
        buffer.writeInt(response.getStateCode());
        //长度
        buffer.writeInt(response.getDataLen());
        //data
        if(response.getData() != null){
            buffer.writeBytes(response.getData());
        }
        return buffer;
    }
}
