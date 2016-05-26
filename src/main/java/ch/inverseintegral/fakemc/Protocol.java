package ch.inverseintegral.fakemc;

import ch.inverseintegral.fakemc.packets.*;
import ch.inverseintegral.fakemc.packets.handshake.Handshake;
import ch.inverseintegral.fakemc.packets.login.Kick;
import ch.inverseintegral.fakemc.packets.login.LoginRequest;
import ch.inverseintegral.fakemc.packets.status.Ping;
import ch.inverseintegral.fakemc.packets.status.StatusRequest;
import ch.inverseintegral.fakemc.packets.status.StatusResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the protocol for the different handshake states.
 *
 * @author Inverse Integral
 * @version 1.0
 * @since 1.0
 */
public enum Protocol {

    HANDSHAKE {{
        regsisterPacket(0x00, Handshake.class, Direction.CLIENT_TO_SERVER);
    }},

    // State number 1 set by the handshake packet
    STATUS {{
        // Status requests
        regsisterPacket(0x00, StatusRequest.class, Direction.CLIENT_TO_SERVER);
        regsisterPacket(0x00, StatusResponse.class, Direction.SERVER_TO_CLIENT);

        // Ping packets
        regsisterPacket(0x01, Ping.class, Direction.CLIENT_TO_SERVER);
        regsisterPacket(0x01, Ping.class, Direction.SERVER_TO_CLIENT);
    }},

    // State number 2 set by the handshake packet
    LOGIN {{
       regsisterPacket(0x00, LoginRequest.class, Direction.CLIENT_TO_SERVER);
       regsisterPacket(0x00, Kick.class, Direction.SERVER_TO_CLIENT);
    }};

    /**
     * Contains the protocol specific data.
     */
    public final ProtocolData data = new ProtocolData();

    /**
     * Registers a new packet with the given id and direction.
     * This method is for internal use only.
     *
     * @param id            The id of this packet
     * @param packetClass   The class that implements this packet. Must be a subclass of {@link Packet packet}
     * @param direction     The direction of this packet
     * @see ProtocolData
     * @see Direction
     */
    protected void regsisterPacket(int id, Class<? extends Packet> packetClass, Direction direction) {
        if (direction == Direction.CLIENT_TO_SERVER) {
            data.incoming.put(id, packetClass);
        } else {
            data.outgoing.put(id, packetClass);
        }
    }

    /**
     * Gets an implementation class by its packet id.
     *
     * @param id    The id of the packet
     * @return      Returns the found class or null
     */
    public Class<?> getClassById(int id) {
        return data.incoming.get(id);
    }

    /**
     * Gets the packet id from the implementation class.
     *
     * @param packetClass   The packet implementation class
     * @return              Returns the packet id or null
     */
    public Integer getIdByClass(Class<? extends Packet> packetClass) {
        return data.outgoing.entrySet().stream()
                .filter(entry -> entry.getValue().equals(packetClass))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }

    /**
     * Contains the protocol specific data.
     */
    public class ProtocolData {

        /**
         * A map of ids and incoming packets (used for deserialization).
         */
        protected Map<Integer, Class<? extends Packet>> incoming = new HashMap<>();

        /**
         * A map of ids and outgoing packets (used for serialization).
         */
        protected Map<Integer, Class<? extends Packet>> outgoing = new HashMap<>();

    }

    /**
     * Represents a communication direction.
     * Either from a client to a server or
     * from a server to a client.
     */
    public enum Direction {
        CLIENT_TO_SERVER,
        SERVER_TO_CLIENT
    }

}
