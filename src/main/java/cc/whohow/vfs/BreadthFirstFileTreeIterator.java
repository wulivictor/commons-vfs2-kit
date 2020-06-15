package cc.whohow.vfs;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class BreadthFirstFileTreeIterator implements Iterator<FileSelectInfo> {
    private static final Logger log = LogManager.getLogger(BreadthFirstFileTreeIterator.class);
    protected final FileSelector fileSelector;
    protected final Deque<Iterator<? extends FileSelectInfo>> deque = new LinkedList<>();
    protected FileSelectInfo fileSelectInfo;

    public BreadthFirstFileTreeIterator(FileObject fileObject, FileSelector fileSelector) throws FileSystemException {
        this.fileSelector = fileSelector;
        this.deque.addFirst(Collections.singleton(new ImmutableFileSelectInfo(
                fileObject.isFolder() ? fileObject : null, fileObject, 0)).iterator());
    }

    @Override
    public boolean hasNext() {
        try {
            while (!deque.isEmpty()) {
                Iterator<? extends FileSelectInfo> iterator = deque.getFirst();
                if (iterator.hasNext()) {
                    fileSelectInfo = iterator.next();
                    if (fileSelectInfo.getFile().getType().hasChildren()) {
                        if (fileSelector.traverseDescendents(fileSelectInfo)) {
                            FileObject[] children = fileSelectInfo.getFile().getChildren();
                            List<FileSelectInfo> list = new ArrayList<>(children.length);
                            for (FileObject child : children) {
                                list.add(new ImmutableFileSelectInfo(fileSelectInfo, child));
                            }
                            deque.addLast(list.iterator());
                        }
                    }
                    if (fileSelector.includeFile(fileSelectInfo)) {
                        return true;
                    } else {
                        fileSelectInfo.getFile().close();
                    }
                } else {
                    deque.removeFirst();
                }
            }
            return false;
        } catch (Exception e) {
            while (!deque.isEmpty()) {
                Iterator<? extends FileSelectInfo> iterator = deque.removeFirst();
                while (iterator.hasNext()) {
                    try {
                        iterator.next().getFile().close();
                    } catch (Exception ex) {
                        log.trace("close error", ex);
                    }
                }
            }
            throw FileSystemExceptions.unchecked(e);
        }
    }

    @Override
    public FileSelectInfo next() {
        return fileSelectInfo;
    }
}
