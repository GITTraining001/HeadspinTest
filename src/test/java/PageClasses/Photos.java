package PageClasses;

import Utils.MobileInitializer;
import org.openqa.selenium.By;

import java.io.IOException;

public class Photos extends MobileInitializer {
    By sortSelect = By.xpath("//android.widget.Button[4]");
    By downloadFolderThumb = By.xpath("//android.widget.TextView[@text = \"Download\"]");
    By headspinTestImage = By.xpath("//android.view.ViewGroup[contains(@content-desc,\"Photo taken on\")]");
    By infoButtonInImagePreview = By.xpath("//android.widget.ImageView[@content-desc=\"More options\"]");
    By imagePath = By.xpath("//android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.TextView[1]");
    By closeInfo = By.xpath("//android.widget.ImageButton[@content-desc=\"Close info\"]");

    public boolean copyFileToDevice() {
        boolean waitVal = super.waitForWebElementVisibilityOf(sortSelect);
        if (!waitVal) {
            return false;
        }
        System.out.println("Wait Value: " + waitVal);
        String srcPath = "C:\\Test.png";
        String destPath = "/sdcard/Download/Test.png";
        try {
            super.copyFileToDevice(srcPath, destPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean openPhotosLibrary() {
        super.clickOnElement(sortSelect);
        System.out.println("Clicked on Library");
        boolean waitVal = super.waitForWebElementVisibilityOf(downloadFolderThumb);
        if (!waitVal) {
            return false;
        }
        return true;
    }

    public boolean openImage() {
        super.clickOnElement(downloadFolderThumb);
        System.out.println("Clicked on Download");
        boolean waitVal = super.waitForWebElementVisibilityOf(headspinTestImage);
        if (!waitVal) {
            return false;
        }
        super.clickOnElement(headspinTestImage);
        System.out.println("Clicked on Image");
        waitVal = super.waitForWebElementVisibilityOf(infoButtonInImagePreview);
        if (!waitVal) {
            return false;
        }
        return true;
    }

    public boolean changeOrientation(String type) {
        super.clickOnElement(closeInfo);
        super.changeScreenOrientation(type);
        return true;
    }

    public String openImageInfo() {
        super.clickOnElement(infoButtonInImagePreview);
        if (!super.waitForWebElementVisibilityOf(closeInfo)){
            return null;
        }
        System.out.println("Clicked on Image Preview");
        return super.getText(imagePath);
    }

}
