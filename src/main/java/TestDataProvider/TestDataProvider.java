package TestDataProvider;

import org.testng.annotations.DataProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;


public class TestDataProvider {
    @DataProvider (name="TestData")
    public Object[][] testData(){
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("TestData.yaml");
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
        return new Object[][] {{obj}};
    }
}
