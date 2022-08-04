package gc.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        this.cachingDir = cachingDir;
    }

    @Override
    protected String load(String key) {
        Path filePath = Path.of(key);
        String result = null;
        try {
            result = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
