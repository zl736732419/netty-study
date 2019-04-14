package com.zheng.netty.v5.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloHandler extends SimpleChannelInboundHandler<String> {
        @Override
        protected void messageReceived(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
            System.out.println("收到消息:" + msg);
            channelHandlerContext.channel().writeAndFlush("hi");
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