package sugonedu.hadoop;

import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.util.EnumSet;

public class AppNewAPI {
    public static void main(String[] args) throws IOException {
        FileContext fc = FileContext.getFileContext();
        String home_string = System.getProperty("user.home");
        Path home_path = new Path(home_string);
        Path password_path = new Path(home_path, "moyi/fstest/password");
        try {
            fc.delete(password_path, false);
        } catch (FileNotFoundException e) {
        }
        try {
            fc.mkdir(password_path.getParent(),
                    FsPermission.getDirDefault(), true);
        } catch (FileAlreadyExistsException e) {
        }
        EnumSet<CreateFlag> flags =  EnumSet.of(CreateFlag.CREATE);
        try (
                InputStream in = new FileInputStream("/etc/passwd");
                OutputStream out = fc.create(password_path, flags)) {
            IOUtils.copyBytes(in, out, 8192);
        }
        try (FSDataOutputStream out = fc.create(password_path,
                EnumSet.of(CreateFlag.APPEND))) {
            out.write("Hello HDFS\n".getBytes("utf-8"));
        }
    }
}
