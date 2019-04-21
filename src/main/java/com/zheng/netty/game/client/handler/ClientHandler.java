package com.zheng.netty.game.client.handler;

import com.zheng.netty.game.model.Response;
import com.zheng.netty.game.module.fuben.response.FightResponse;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class ClientHandler extends SimpleChannelHandler {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            System.out.println("messageReceived");

            Response response = (Response) e.getMessage();
            if (response.getModule() == 1) {
                if (response.getCmd() == 1) {
                    FightResponse fightResponse = new FightResponse();
                    fightResponse.readFromBytes(response.getData());
                    System.out.println(fightResponse);
                }
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            System.out.println("exceptionCaught");
            super.exceptionCaught(ctx, e);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("channelConnected");
            super.channelConnected(ctx, e);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("channelDisconnected");
            super.channelDisconnected(ctx, e);
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            System.out.println("channelClosed");
            super.channelClosed(ctx, e);
        }
    }