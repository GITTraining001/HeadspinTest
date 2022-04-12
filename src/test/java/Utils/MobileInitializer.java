package Utils;


import Tools.MobileAppBaseClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public class MobileInitializer extends MobileAppBaseClass {
    public WebDriver driver;
    public String methodName;
    @BeforeSuite
    public void initSuite() throws Exception{
        super.setDriver();
        this.driver = super.getDriver();
    }
    @BeforeMethod
    public void beforeTestCase(Method m) {
        methodName = m.getName();
    }
    @AfterSuite
    public void teardown(){
        super.changeScreenOrientation("portrait");
        this.driver.quit();
    }
    public WebDriver getDriver(){
        return this.driver;
    }
}
