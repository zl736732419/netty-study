package com.zheng.netty.game.codc;

import com.zheng.netty.game.constant.Constants;
import com.zheng.netty.game.model.Request;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class RequestEncoder extends OneToOneEncoder {
    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        Request request = (Request) msg;
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
        //包头
        buffer.writeInt(Constants.FLAG);
        //module
        buffer.writeShort(request.getModule());
        //cmd
        buffer.writeShort(request.getCmd());
        //长度
        buffer.writeInt(request.getDataLen());
        //data
        if(request.getData() != null){
            buffer.writeBytes(request.getData());
        }
        return buffer;
    }
}
