package cc.whohow.configuration.provider;

import cc.whohow.configuration.Configuration;
import cc.whohow.vfs.CloudFileObject;
import cc.whohow.vfs.FileValue;
import cc.whohow.vfs.serialize.PropertiesSerializer;

import java.util.Properties;

public class PropertiesConfiguration extends FileValue.Cache<Properties> implements Configuration<Properties> {
    public PropertiesConfiguration(CloudFileObject fileObject) {
        super(fileObject, PropertiesSerializer.get());
    }
}
