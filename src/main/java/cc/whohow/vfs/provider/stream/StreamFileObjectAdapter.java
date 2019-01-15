package cc.whohow.vfs.provider.stream;

import cc.whohow.vfs.*;
import cc.whohow.vfs.operations.ProviderFileOperations;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileContentInfo;
import org.apache.commons.vfs2.operations.FileOperations;
import org.apache.commons.vfs2.util.RandomAccessMode;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

public class StreamFileObjectAdapter implements DataFileObject, FreeFileObject, ReadonlyFileObject, SimpleFileContent {
    private static final FileContentInfo DEFAULT_CONTENT_INFO = new DefaultFileContentInfo(null, null);
    private final InputStream stream;
    private final Map<String, Object> attributes;
    private final FileContentInfo contentInfo;
    private volatile boolean open = true;

    public StreamFileObjectAdapter(InputStream stream) {
        this(stream, Collections.emptyMap(), DEFAULT_CONTENT_INFO);
    }

    public StreamFileObjectAdapter(InputStream stream, Map<String, Object> attributes, FileContentInfo contentInfo) {
        this.stream = stream;
        this.attributes = attributes;
        this.contentInfo = contentInfo;
    }

    @Override
    public FileObject getFile() {
        return this;
    }

    @Override
    public long getSize() throws FileSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastModifiedTime() throws FileSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setLastModifiedTime(long modTime) throws FileSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getAttributes() throws FileSystemException {
        return attributes;
    }

    @Override
    public void setAttribute(String attrName, Object value) throws FileSystemException {
        attributes.put(attrName, value);
    }

    @Override
    public void removeAttribute(String attrName) throws FileSystemException {
        attributes.remove(attrName);
    }

    @Override
    public InputStream getInputStream() throws FileSystemException {
        return new FilterInputStream(stream) {
            @Override
            public void close() throws IOException {
                open = false;
            }
        };
    }

    @Override
    public RandomAccessContent getRandomAccessContent(RandomAccessMode mode) throws FileSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public OutputStream getOutputStream(boolean bAppend) throws FileSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws FileSystemException {
        open = false;
    }

    @Override
    public FileContentInfo getContentInfo() throws FileSystemException {
        return contentInfo;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public boolean exists() throws FileSystemException {
        return true;
    }

    @Override
    public FileContent getContent() throws FileSystemException {
        return this;
    }

    @Override
    public FileOperations getFileOperations() throws FileSystemException {
        return new ProviderFileOperations(this);
    }

    @Override
    public FileName getName() {
        return new UnknownFileName(this);
    }

    @Override
    public String getPublicURIString() {
        return getName().getURI();
    }

    @Override
    public URL getURL() throws FileSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAttached() {
        return true;
    }

    @Override
    public boolean isContentOpen() {
        return open;
    }

    @Override
    public void refresh() throws FileSystemException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return getName().toString();
    }
}
