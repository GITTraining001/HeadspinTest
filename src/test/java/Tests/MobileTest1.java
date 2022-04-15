package Tests;

import PageClasses.Photos;
import TestDataProvider.TestDataProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MobileTest1 extends Photos {
    @Test (dataProvider = "TestData", dataProviderClass = TestDataProvider.class)
    public void CopyImageFileToDeviceAndEmail(Map<String, Object> obj) {
        String className = super.methodName;
        String appPackage = "";
        String appActivity = "";
        LinkedHashMap<String, String> filterWordsMap = (LinkedHashMap<String, String>) obj.get(className);
        appPackage = filterWordsMap.get("appPackage");
        appActivity = filterWordsMap.get("appActivity");

        System.out.print("------Classname: " + "-----------");
        super.copyFileToDevice();
        super.openPhotosLibrary();
        super.openImage();
        String retText = super.openImageInfo();
        super.changeOrientation("landscape");
        Assert.assertTrue(retText.contains("Test.png"), "Failed to copy image to device");
        System.out.println("Image file name in image info screen: " +retText);
        super.changeOrientation("portrait");
        super.switchToGmailApp(appPackage, appActivity);
        super.sendEmail();

    }

}
