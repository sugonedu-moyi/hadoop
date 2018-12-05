package sugonedu.hadoop.redside;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JoinReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        // 根据标签，将记录放入对应的数据集
        List<String> userList = new ArrayList<>();
        List<String> logList = new ArrayList<>();
        for (Text value : values) {
            String recordTag = value.toString();
            String[] sp = recordTag.split("|");
            if (sp[1].equals("user")) {
                userList.add(sp[0]);
            } else if (sp[1].equals("log")) {
                logList.add(sp[0]);
            }
        }

        // 对两个数据集的记录做Join
        for (String log : logList) {
            for (String user : userList) {
                Text outValue = new Text(log + "," + user);
                context.write(key, outValue);
            }
        }
    }
}
