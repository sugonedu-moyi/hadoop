package sugonedu.hadoop.mapside;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JoinMapper extends Mapper<Object, Text, Text, Text> {

    private Map<String, String[]> nameToUser = new HashMap<>();

    @Override
    protected void setup(Context context) throws IOException {
        Path path = Paths.get("users.txt");
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split("\t");
                if (split.length == 3) {
                    nameToUser.put(split[0], split);
                }
            }
        }
    }

    @Override
    protected void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        String name = split[0];
        String[] user = nameToUser.get(name);
        if (user != null) {
            List<String> list = new ArrayList<>(Arrays.asList(split));
            list.addAll(Arrays.asList(user));
            String join = String.join(",", list);
            context.write(new Text(name), new Text(join));
        }
    }
}
