package sugonedu.hadoop;

import java.util.List;

public interface MapTask {

    List<Pair> map(String key, String value);

}
