package io.kw.serviceClients.utils;

import javax.annotation.Priority;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Priority(Integer.MIN_VALUE)
public class EntityLoggingFilter implements ClientRequestFilter, ClientResponseFilter, WriterInterceptor {

    private static final String ENTITY_STREAM_PROPERTY = "EntityLoggingFilter.entityStream";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final int maxEntitySize = 1024 * 8;

    private void log(StringBuilder sb) {
        System.out.println(sb);
    }

    private InputStream logInboundEntity(final StringBuilder b, InputStream stream) throws IOException {
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }
        stream.mark(maxEntitySize + 1);
        final byte[] entity = new byte[maxEntitySize + 1];
        final int entitySize = stream.read(entity);
        b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize), EntityLoggingFilter.DEFAULT_CHARSET));
        if (entitySize > maxEntitySize) {
            b.append("...more...");
        }
        b.append('\n');
        stream.reset();
        return stream;
    }

    @Override
    public void filter(ClientRequestContext requestContext) {
        if (requestContext.hasEntity()) {
            final OutputStream stream = new LoggingStream(requestContext.getEntityStream());
            requestContext.setEntityStream(stream);
            requestContext.setProperty(ENTITY_STREAM_PROPERTY, stream);
        }
    }

    @Override
    public void filter(ClientRequestContext requestContext,
                       ClientResponseContext responseContext) throws IOException {
        final StringBuilder sb = new StringBuilder();
        if (responseContext.hasEntity()) {
            responseContext.setEntityStream(logInboundEntity(sb, responseContext.getEntityStream()));
            log(sb);
        }
    }

    @Override
    public void aroundWriteTo(WriterInterceptorContext context)
            throws IOException, WebApplicationException {
        final LoggingStream stream = (LoggingStream) context.getProperty(ENTITY_STREAM_PROPERTY);
        context.proceed();
        if (stream != null) {
            log(stream.getStringBuilder());
        }
    }

    private class LoggingStream extends FilterOutputStream {

        private final StringBuilder sb = new StringBuilder();
        private final ByteArrayOutputStream b = new ByteArrayOutputStream();

        LoggingStream(OutputStream out) {
            super(out);
        }

        StringBuilder getStringBuilder() {
            final byte[] entity = b.toByteArray();
            sb.append(new String(entity, 0, entity.length, EntityLoggingFilter.DEFAULT_CHARSET));
            if (entity.length > maxEntitySize) {
                sb.append("...more...");
            }
            sb.append('\n');
            return sb;
        }

        @Override
        public void write(final int i) throws IOException {
            if (b.size() <= maxEntitySize) {
                b.write(i);
            }
            out.write(i);
        }
    }
}