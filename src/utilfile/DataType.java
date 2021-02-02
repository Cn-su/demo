package utilfile;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
public interface DataType {
    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
    public static JSONArray rsListToJsonArray(List<Map> rsList) {
        JSONArray jsonArray = new JSONArray();
        for (Map rowData : rsList) {
            Iterator entries = rowData.entrySet().iterator();
            JSONObject jsonObject = new JSONObject();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) entries.next();
                String key = entry.getKey();
                String value = entry.getValue();
                jsonObject.put(key, value);
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
