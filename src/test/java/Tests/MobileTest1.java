package Tests;

import PageClasses.Photos;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class MobileTest1 extends Photos {
    @Test
    public void TestSortingOfFirstAndSecondColumn() {
        System.out.print("------Classname: " + "-----------");
        super.copyFileToDevice();
        super.openPhotosLibrary();
        super.openImage();
        String retText = super.openImageInfo();
        super.changeOrientation("landscape");
        Assert.assertTrue(retText.contains("Test.png"), "Failed to copy image to device");
        System.out.println("Image file name in image info screen: " +retText);
    }

}
