package com.github.msemys.esjc;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class StreamAclJsonAdapter extends TypeAdapter<StreamAcl> {
    private static final String ACL_READ = "$r";
    private static final String ACL_WRITE = "$w";
    private static final String ACL_DELETE = "$d";
    private static final String ACL_META_READ = "$mr";
    private static final String ACL_META_WRITE = "$mw";

    @Override
    public void write(JsonWriter writer, StreamAcl value) throws IOException {
        writer.beginObject();
        writeRoles(writer, ACL_READ, value.readRoles);
        writeRoles(writer, ACL_WRITE, value.writeRoles);
        writeRoles(writer, ACL_DELETE, value.deleteRoles);
        writeRoles(writer, ACL_META_READ, value.metaReadRoles);
        writeRoles(writer, ACL_META_WRITE, value.metaWriteRoles);
        writer.endObject();
    }

    @Override
    public StreamAcl read(JsonReader reader) throws IOException {
        StreamAcl.Builder builder = StreamAcl.newBuilder();

        reader.beginObject();

        while (reader.peek() != JsonToken.END_OBJECT && reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case ACL_READ:
                    builder.readRoles(readRoles(reader));
                    break;
                case ACL_WRITE:
                    builder.writeRoles(readRoles(reader));
                    break;
                case ACL_DELETE:
                    builder.deleteRoles(readRoles(reader));
                    break;
                case ACL_META_READ:
                    builder.metaReadRoles(readRoles(reader));
                    break;
                case ACL_META_WRITE:
                    builder.metaWriteRoles(readRoles(reader));
                    break;
            }
        }

        reader.endObject();

        return builder.build();
    }

    private static void writeRoles(JsonWriter writer, String name, List<String> roles) throws IOException {
        if (roles != null) {
            writer.name(name);
            if (roles.size() == 1) {
                writer.value(roles.get(0));
            } else {
                writer.beginArray();
                for (String role : roles) {
                    writer.value(role);
                }
                writer.endArray();
            }
        }
    }

    private static List<String> readRoles(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.STRING) {
            return singletonList(reader.nextString());
        } else {
            List<String> roles = new ArrayList<>();

            reader.beginArray();

            while (reader.peek() != JsonToken.END_ARRAY) {
                roles.add(reader.nextString());
            }

            reader.endArray();

            return roles;
        }
    }
}
