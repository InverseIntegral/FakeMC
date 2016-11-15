package ch.inverseintegral.fakemc.server.handlers;

import ch.inverseintegral.fakemc.config.ConfigurationValues;
import ch.inverseintegral.fakemc.server.Protocol;
import ch.inverseintegral.fakemc.server.ServerStatistics;
import ch.inverseintegral.fakemc.server.packets.Packet;
import ch.inverseintegral.fakemc.server.packets.login.Kick;
import ch.inverseintegral.fakemc.server.packets.login.LoginRequest;
import ch.inverseintegral.fakemc.server.packets.status.Ping;
import ch.inverseintegral.fakemc.server.packets.status.StatusRequest;
import ch.inverseintegral.fakemc.server.ping.Chat;
import ch.inverseintegral.fakemc.server.ping.Players;
import ch.inverseintegral.fakemc.server.ping.StatusResponse;
import ch.inverseintegral.fakemc.server.ping.Version;
import com.google.gson.Gson;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Handles the specific packets that are received from the server.
 *
 * @author md-5
 * @author Inverse Integral
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

    private static final Gson gson = new Gson();

    private final String favicon;
    private final ServerStatistics serverStatistics;
    private final ConfigurationValues configurationValues;

    public PacketHandler(ConfigurationValues configurationValues, String favicon, ServerStatistics serverStatistics) {
        this.favicon = favicon;
        this.configurationValues = configurationValues;
        this.serverStatistics = serverStatistics;
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

    protected void handle(ChannelHandlerContext ctx, ch.inverseintegral.fakemc.server.packets.handshake.Handshake handshake) {
        this.checkState(ProtocolState.HANDSHAKE);

        serverStatistics.addHandshake(handshake.getHost(), handshake.getPort(), handshake.getProtocolVersion());

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

        ctx.channel().writeAndFlush(getResponse());
        this.currentState = ProtocolState.PING;
    }

    protected void handle(ChannelHandlerContext ctx, Ping ping) {
        this.checkState(ProtocolState.PING);
        logger.info("Ping received with content {}", ping.getTime());

        ctx.channel().writeAndFlush(ping).addListener(ChannelFutureListener.CLOSE);
    }

    private void handle(ChannelHandlerContext ctx, LoginRequest loginRequest) {
        this.checkState(ProtocolState.USERNAME);

        serverStatistics.addPlayerLogin(loginRequest.getData());
        logger.info("The player {} tried to join the server", loginRequest.getData());

        Kick kick = new Kick(getKickData());
        ctx.channel()
                .writeAndFlush(kick)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Gets the status response json string from the current configuration and
     * encapsulates it in the {@link ch.inverseintegral.fakemc.server.packets.status.StatusResponse status response}.
     *
     * @return  The status response object constructed from the configuration values.
     */
    private ch.inverseintegral.fakemc.server.packets.status.StatusResponse getResponse() {
        Players players = new Players(configurationValues.getMaxPlayers(), configurationValues.getCurrentPlayers());
        Chat chat = new Chat(configurationValues.getMotd());

        return new ch.inverseintegral.fakemc.server.packets.status.StatusResponse(gson.toJson(new StatusResponse(chat, players, Version.V_1_8, favicon)));
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
     * Gets the kick message in json format.
     * @return  Returns the json string of the kick data.
     */
    private String getKickData() {
        return gson.toJson(new Chat(configurationValues.getKickMessage()));
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