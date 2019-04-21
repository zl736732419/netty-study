package com.zheng.netty.game.server.handler;

import com.zheng.netty.game.model.Request;
import com.zheng.netty.game.model.Response;
import com.zheng.netty.game.model.StateCode;
import com.zheng.netty.game.module.fuben.request.FightRequest;
import com.zheng.netty.game.module.fuben.response.FightResponse;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

import java.net.SocketAddress;

public class ServerHandler extends SimpleChannelHandler {
    /**
     * 消息处理
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println("messageReceived");
        Request request = (Request) e.getMessage();

        if (request.getModule() == 1) {
            if (request.getCmd() == 1) {
                FightRequest fightRequest = new FightRequest();
                fightRequest.readFromBytes(request.getData());
                System.out.println(fightRequest);
                // 响应消息
                FightResponse fightResponse = new FightResponse();
                fightResponse.setGold(9999);
                Response response = new Response();
                response.setModule((short) 1);
                response.setCmd((short) 1);
                response.setStateCode(StateCode.SUCCESS);
                response.setData(fightResponse.getBytes());
                ctx.getChannel().write(response);
            }
        }
    }

    /**
     * 异常处理，messageReceived发生异常时会调用
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("exceptionCaught");
        System.out.println(e.getCause().fillInStackTrace());
        super.exceptionCaught(ctx, e);
    }

    /**
     * 连接建立
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelConnected");
        super.channelConnected(ctx, e);
    }

    /**
     * 只有连接建立才会进行断开
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelDisconnected");
        SocketAddress address = e.getChannel().getRemoteAddress();
        System.out.println("客户端" + address.toString() + "断开连接");
        super.channelDisconnected(ctx, e);
    }

    /**
     * 连接关闭，不管连接是否建立，都会关闭
     *
     * @param ctx
     * @param e
     * @throws Exception
     */
    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        System.out.println("channelClosed");
        super.channelClosed(ctx, e);
    }
}