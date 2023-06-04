package img2md;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DirCache {

    private static volatile List<String> dirs = new ArrayList<>();

    public static String[] get() {
        if (dirs.isEmpty()) {
            try {
                String userHome = System.getProperty("user.home");
                String filePath = userHome + "/.pasteimages" + "/cache.txt";
                File dirCacheFile = new File(filePath);
                List<String> cacheDirs = FileUtils.readLines(dirCacheFile, Charset.defaultCharset());
                cacheDirs = cacheDirs.stream().map(DirCache::removeLastSeparator).collect(Collectors.toList());
                dirs.addAll(cacheDirs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (dirs.isEmpty()) {
            dirs.add(".{document_name}_images");
        }
        return dirs.toArray(new String[dirs.size()]);
    }


    public static synchronized void addIfNotExist(String newPath) {
        if (StringUtils.isBlank(newPath))
            return;
        newPath = removeLastSeparator(newPath);
        if (!dirs.contains(newPath)) {
            dirs.add(newPath);
            String userHome = System.getProperty("user.home");
            String filePath = userHome + "/.pasteimages" + "/cache.txt";
            File dirCacheFile = new File(filePath);
            try {
                if (!dirCacheFile.exists()) {
                    dirCacheFile.getParentFile().mkdirs();
                }
                FileUtils.writeLines(dirCacheFile, Arrays.asList(newPath), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static String removeLastSeparator(String dir) {
        if (dir.endsWith("/")) {
            return dir.substring(0, dir.length() - 1);
        }
        return dir;
    }
}
