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

        StatusResponse statusResponse = new StatusResponse(getResponseData(20000, 1, "§4FakeMC §8- §cServer", "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyRpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDpFNTE3OEEyRTk5QTAxMUUyOUExNUJDMTA0NkE4OTA0RCIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDpFNTE3OEEyRjk5QTAxMUUyOUExNUJDMTA0NkE4OTA0RCI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkU1MTc4QTJDOTlBMDExRTI5QTE1QkMxMDQ2QTg5MDREIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOkU1MTc4QTJEOTlBMDExRTI5QTE1QkMxMDQ2QTg5MDREIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+FYrpWAAABrNJREFUeNrkW2ls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"));
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
     * Gets a server list ping response in the json format.
     *
     * @param currentPlayers    The current amount of players.
     * @param maxPlayer         The maximum amount of players.
     * @param motd              The message of the day.
     * @param favicon           The favicon (base64 string).
     * @return                  Returns the json formatted ping response.
     */
    private String getResponseData(int currentPlayers, int maxPlayer, String motd, String favicon) {
        Player player = new Player("§dMagentan", UUID.randomUUID().toString());
        Version version = new Version("1.8", 47);
        Players players = new Players(maxPlayer, currentPlayers, Collections.singletonList(player));
        Chat description = new Chat(motd);

        Gson gson = new Gson();
        return gson.toJson(new ch.inverseintegral.fakemc.ping.StatusResponse(description, players, version, favicon));
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
