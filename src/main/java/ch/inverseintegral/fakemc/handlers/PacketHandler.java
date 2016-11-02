package ch.inverseintegral.fakemc.handlers;

import ch.inverseintegral.fakemc.Protocol;
import ch.inverseintegral.fakemc.config.ConfigurationValues;
import ch.inverseintegral.fakemc.packets.Packet;
import ch.inverseintegral.fakemc.packets.handshake.Handshake;
import ch.inverseintegral.fakemc.packets.login.Kick;
import ch.inverseintegral.fakemc.packets.login.LoginRequest;
import ch.inverseintegral.fakemc.packets.status.Ping;
import ch.inverseintegral.fakemc.packets.status.StatusRequest;
import ch.inverseintegral.fakemc.packets.status.StatusResponse;
import ch.inverseintegral.fakemc.ping.Chat;
import ch.inverseintegral.fakemc.ping.Players;
import ch.inverseintegral.fakemc.ping.Version;
import com.google.gson.Gson;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collections;

/**
 * Handles the specific packets that are received from the server.
 *
 * @author md-5, Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public class PacketHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);

    /**
     * The current protocol state.
     * This should not be confused with the {@link MinecraftHandler#protocol protocol in the minecraft handler}.
     */
    private ProtocolState currentState = ProtocolState.HANDSHAKE;

    private String response;
    private String kickMessage;

    public PacketHandler(ConfigurationValues configurationValues) {
        Gson gson = new Gson();
        Version version = new Version("1.8", 47);
        Players players = new Players(configurationValues.getMaxPlayers(), configurationValues.getCurrentPlayers(), Collections.emptyList());
        Chat chat = new Chat(configurationValues.getMotd());

        this.response = gson.toJson(new ch.inverseintegral.fakemc.ping.StatusResponse(chat, players, version, configurationValues.getFavicon()));
        this.kickMessage = configurationValues.getKickMessage();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        Method handlerMethod = this.getClass().getDeclaredMethod("handle", ChannelHandlerContext.class, msg.getClass());
        handlerMethod.invoke(this, ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Caught a channel exception", cause);
        ctx.channel().close();
    }

    protected void handle(ChannelHandlerContext ctx, Handshake handshake) {
        this.checkState(ProtocolState.HANDSHAKE);

        switch (handshake.getRequestedProtocol()) {
            case 1:
                ctx.channel().pipeline().get(MinecraftHandler.class).setProtocol(Protocol.STATUS);
                this.currentState = ProtocolState.STATUS;
                break;
            case 2:
                ctx.channel().pipeline().get(MinecraftHandler.class).setProtocol(Protocol.LOGIN);
                this.currentState = ProtocolState.USERNAME;
                break;
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
        logger.info("Ping received with content {}", ping.getTime());

        ctx.channel().writeAndFlush(ping).addListener(ChannelFutureListener.CLOSE);
    }

    private void handle(ChannelHandlerContext ctx, LoginRequest loginRequest) {
        this.checkState(ProtocolState.USERNAME);
        logger.info("The player {} tried to join the server", loginRequest.getData());

        Kick kick = new Kick(getKickData(this.kickMessage));
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