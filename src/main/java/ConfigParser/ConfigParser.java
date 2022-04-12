package ConfigParser;

import org.testng.annotations.DataProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;


public class ConfigParser {
    public Map<String, Object> parseConfig(){
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("Config.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
        return obj;
    }
}
