package com.zheng.netty.v4.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 所有事件都会先触发该方法
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            // 处理空闲事件
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            IdleState state = idleStateEvent.state();
            SimpleDateFormat sf = new SimpleDateFormat("ss");
            switch (state) {
                case READER_IDLE:
                    System.out.println("idle state: " + state + " seconds: " + sf.format(new Date()));
                    break;
                case ALL_IDLE:
                    System.out.println("idle state: " + state + " seconds: " + sf.format(new Date()));
                    ChannelFuture future = ctx.channel().writeAndFlush("timeout, you are under line!");
                    future.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            // 闲置连接下线，(踢出闲置用户)
                            future.channel().close();
                        }
                    });
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        System.out.println("收到消息:" + msg);
        String response = "hi";
        if (Objects.equals("heart_beat_request", msg)) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd HH:MM:ss");
            response = new StringBuilder("heart_beat_response: ")
                    .append(sf.format(new Date()))
                    .toString();
        }
        channelHandlerContext.channel().writeAndFlush(response);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("exceptionCaught");
    }
}