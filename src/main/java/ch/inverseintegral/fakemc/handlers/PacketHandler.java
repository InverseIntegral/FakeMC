package ch.inverseintegral.fakemc.handlers;

import ch.inverseintegral.fakemc.Protocol;
import ch.inverseintegral.fakemc.packets.*;
import ch.inverseintegral.fakemc.packets.handshake.Handshake;
import ch.inverseintegral.fakemc.packets.login.Kick;
import ch.inverseintegral.fakemc.packets.login.LoginRequest;
import ch.inverseintegral.fakemc.packets.status.Ping;
import ch.inverseintegral.fakemc.packets.status.StatusRequest;
import ch.inverseintegral.fakemc.packets.status.StatusResponse;
import ch.inverseintegral.fakemc.ping.Chat;
import ch.inverseintegral.fakemc.ping.Player;
import ch.inverseintegral.fakemc.ping.Players;
import ch.inverseintegral.fakemc.ping.Version;
import com.google.gson.Gson;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.security.auth.login.LoginException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.UUID;

/**
 * Handles the specific packets that are received from the server.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    /**
     * The current protocol state.
     * This should not be confused with the {@link MinecraftHandler#protocol protocol in the minecraft handler}.
     */
    private ProtocolState currentState = ProtocolState.HANDSHAKE;

    private String response;

    public PacketHandler(String response) {
        this.response = response;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        Method handlerMethod = this.getClass().getDeclaredMethod("handle", ChannelHandlerContext.class, msg.getClass());
        handlerMethod.invoke(this, ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }

    protected void handle(ChannelHandlerContext ctx, Handshake handshake) {
        this.checkState(ProtocolState.HANDSHAKE);

        if (handshake.getRequestedProtocol() == 1) {
            ctx.channel().pipeline().get(MinecraftHandler.class).setProtocol(Protocol.STATUS);
            this.currentState = ProtocolState.STATUS;
        } else if (handshake.getRequestedProtocol() == 2) {
            ctx.channel().pipeline().get(MinecraftHandler.class).setProtocol(Protocol.LOGIN);
            this.currentState = ProtocolState.USERNAME;
        }
    }

    protected void handle(ChannelHandlerContext ctx, StatusRequest statusRequest) {
        this.checkState(ProtocolState.STATUS);

        StatusResponse statusResponse = new StatusResponse(response);
        ctx.channel().writeAndFlush(statusResponse);

        this.currentState = ProtocolState.PING;
    }

    protected void handle(ChannelHandlerContext ctx, Ping ping) {
        this.checkState(ProtocolState.PING);
        ctx.channel().writeAndFlush(ping).addListener(ChannelFutureListener.CLOSE);
    }

    private void handle(ChannelHandlerContext ctx, LoginRequest loginRequest) {
        this.checkState(ProtocolState.USERNAME);

        Kick kick = new Kick(getKickData("§cThis server is under maintenance"));
        ctx.channel()
                .writeAndFlush(kick)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Checks if the current state is equal to the given state.
     * Otherwise throws an exception.
     *
     * @param expectedState The expected state.
     */
    private void checkState(ProtocolState expectedState) {
        if (this.currentState != expectedState) {
            throw new IllegalStateException(expectedState.name() + " is expected but currently is " + this.currentState.name());
        }
    }

    /**
     * Gets some kick data.
     * @param kickMessage   The kick message that should show up
     *                      when the player connects to the server.
     * @return              Returns the json string of the kick data.
     */
    private String getKickData(String kickMessage) {
        Gson gson = new Gson();
        return gson.toJson(new Chat(kickMessage));
    }

    /**
     * The different state the protocol can have.
     */
    private enum  ProtocolState {

        HANDSHAKE,
        STATUS,
        PING,
        USERNAME

    }

}
