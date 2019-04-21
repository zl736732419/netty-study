package com.zheng.netty.serialize;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

/**
 * @Author zhenglian
 * @Date 2019/4/21
 */
public class BufferFactory {
    public static ChannelBuffer getBuffer() {
        ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
        return channelBuffer;
    }
    
    public static ChannelBuffer getBuffer(byte[] bytes) {
        ChannelBuffer channelBuffer = ChannelBuffers.wrappedBuffer(bytes);
        return channelBuffer;
    }
}
