package com.zheng.netty.v5.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HiHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            String channelId = channelHandlerContext.channel().toString();
            System.out.println("channel " + channelId + "response: " + msg);
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