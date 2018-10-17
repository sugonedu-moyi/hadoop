package sugonedu.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;

public class App {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path hdfsPath = new Path("/home/ua36/moyi/fstest/password");
        if (fs.exists(hdfsPath)) {
            fs.delete(hdfsPath, false);
        }

        if (!fs.exists(hdfsPath.getParent())) {
            fs.mkdirs(hdfsPath.getParent());
        }
        InputStream in = new BufferedInputStream(
                new FileInputStream("/etc/passwd"));
        OutputStream out = fs.create(hdfsPath);
        IOUtils.copyBytes(in, out, 4096, true);

        try (FSDataOutputStream append = fs.append(hdfsPath)) {
            append.write("Hello HDFS\n".getBytes());
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new BufferedInputStream(fs.open(hdfsPath))))) {
            String line = null;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
                if (line.contains("daemon")) {
                    System.out.println(line);
                }
            }
            System.out.println(count);
        }
    }
}
