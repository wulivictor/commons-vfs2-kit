package cc.whohow.configuration.provider;

import cc.whohow.fs.util.ByteBufferReadableChannel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.commons.vfs2.FileObject;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * YAML配置文件
 */
public class YamlConfiguration<T> extends AbstractFileBasedConfiguration<T> {
    protected static final YAMLMapper OBJECT_MAPPER = new YAMLMapper();

    static {
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    protected final JavaType type;
    protected final ObjectMapper objectMapper;

    public YamlConfiguration(FileObject fileObject, Class<T> type) {
        this(fileObject, type, OBJECT_MAPPER);
    }

    public YamlConfiguration(FileObject fileObject, TypeReference<T> type) {
        this(fileObject, type, OBJECT_MAPPER);
    }

    public YamlConfiguration(FileObject fileObject, String type) {
        this(fileObject, type, OBJECT_MAPPER);
    }

    public YamlConfiguration(FileObject fileObject, JavaType type) {
        this(fileObject, type, OBJECT_MAPPER);
    }

    public YamlConfiguration(FileObject fileObject, Class<T> type, ObjectMapper objectMapper) {
        this(fileObject, objectMapper.getTypeFactory().constructType(type), objectMapper);
    }


    public YamlConfiguration(FileObject fileObject, TypeReference<T> type, ObjectMapper objectMapper) {
        this(fileObject, objectMapper.getTypeFactory().constructType(type), objectMapper);
    }


    public YamlConfiguration(FileObject fileObject, String type, ObjectMapper objectMapper) {
        this(fileObject, objectMapper.getTypeFactory().constructFromCanonical(type), objectMapper);
    }

    public YamlConfiguration(FileObject fileObject, JavaType type, ObjectMapper objectMapper) {
        super(fileObject);
        this.type = type;
        this.objectMapper = objectMapper;
    }

    @Override
    protected ByteBuffer serialize(T value) throws IOException {
        return ByteBuffer.wrap(objectMapper.writeValueAsBytes(value));
    }

    @Override
    protected T deserialize(ByteBuffer bytes) throws IOException {
        return objectMapper.readValue(new ByteBufferReadableChannel(bytes), type);
    }
}