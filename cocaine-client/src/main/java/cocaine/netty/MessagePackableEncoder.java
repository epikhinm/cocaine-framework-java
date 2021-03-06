package cocaine.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.log4j.Logger;
import org.msgpack.MessagePack;
import org.msgpack.MessagePackable;

/**
 * @author Anton Bobukh <abobukh@yandex-team.ru>
 */
public class MessagePackableEncoder extends MessageToByteEncoder<MessagePackable> {

    private static final Logger logger = Logger.getLogger(MessagePackableEncoder.class);

    private final MessagePack pack;

    public MessagePackableEncoder(MessagePack pack) {
        super(MessagePackable.class);
        this.pack = pack;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        ctx.flush();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, MessagePackable msg, ByteBuf out) throws Exception {
        logger.debug("Encoding message packable: " + msg);
        out.writeBytes(pack.write(msg));
    }

}
