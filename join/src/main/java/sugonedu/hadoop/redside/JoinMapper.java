package sugonedu.hadoop.redside;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class JoinMapper extends Mapper<Object, Text, Text, Text> {

    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        // 分割字段
        String[] split = value.toString().split("\t");
        String username = split[0];
        // 获取文件名
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String filename = fileSplit.getPath().getName();
        // 根据文件名设置标签
        String tag = "";
        if (filename.endsWith("users.txt")) {
            tag = "user";
        } else if (filename.endsWith("user-logs.txt")) {
            tag = "log";
        }
        // 输出key：用户名
        // 输出value：带标签的记录
        String record = String.join(",", split);
        String recordTag = record + "|" + tag;
        Text outKey = new Text(username);
        Text outValue = new Text(recordTag);
        context.write(outKey, outValue);
    }

}
