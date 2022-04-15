package PageClasses;

import Utils.MobileInitializer;
import Utils.Utilities;
import org.openqa.selenium.By;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Photos extends MobileInitializer {
    By sortSelect = By.xpath("//android.widget.Button[4]");
    By downloadFolderThumb = By.xpath("//android.widget.TextView[@text = \"Download\"]");
    By headspinTestImage = By.xpath("//android.view.ViewGroup[contains(@content-desc,\"Photo taken on\")]");
    By infoButtonInImagePreview = By.xpath("//android.widget.ImageView[@content-desc=\"More options\"]");
    By imagePath = By.xpath("//android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.TextView[1]");
    By closeInfo = By.xpath("//android.widget.ImageButton[@content-desc=\"Close info\"]");
    By gmailComposeButton = By.xpath("//android.widget.Button[@text=\"Compose\"]");
    By gmailSmartComposeOKButton = By.xpath("//android.widget.Button[@text=\"OK\"]");
    By gmailSendToTextField = By.xpath("//android.widget.RelativeLayout[1]/android.widget.RelativeLayout/android.widget.RelativeLayout/android.view.ViewGroup/android.widget.EditText");
    By gmailSubjectTextField = By.xpath("//android.widget.LinearLayout[2]/android.widget.LinearLayout/android.widget.EditText");
    By gmailAttachFileButton = By.xpath("//android.widget.TextView[@content-desc=\"Attach file\"]");
    By gmailAttachFileMenuItem = By.xpath("//android.widget.TextView[@text = \"Attach file\"]");
    By gmailImageFileToAttach = By.xpath("//android.widget.TextView[@text = \"Test.png\"]");
    By gmailSendButton = By.xpath ("//android.widget.TextView[@content-desc=\"Send\"]");

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
        super.changeScreenOrientation(type);
        Utilities.sleep(3000);
        return true;
    }

    public String openImageInfo() {
        super.clickOnElement(infoButtonInImagePreview);
        if (!super.waitForWebElementVisibilityOf(closeInfo)){
            return null;
        }
        System.out.println("Clicked on Image Preview");
        String pathText = super.getText(imagePath);
        super.clickOnElement(closeInfo);
        return pathText;
    }

    public boolean switchToGmailApp(String appPackage, String appActivity){
        super.switchApp(appPackage, appActivity);
        if (!super.waitForWebElementVisibilityOf(gmailComposeButton)){
            return false;
        }
        return true;
    }

    public boolean sendEmail(){
        super.clickOnElement(gmailComposeButton);
        if (super.waitForWebElementVisibilityOf(gmailSmartComposeOKButton)){
            super.clickOnElement(gmailSmartComposeOKButton);
        }
        if(!super.waitForWebElementVisibilityOf(gmailSendToTextField)){
            return false;
        }
        List<String> emailList = new ArrayList<String>(){
            {
                add("ashithraj.shetty@gmail.com, ");
                add("asithraj.shetty@gmail.com, ");
            }
        };

        super.type(gmailSendToTextField, emailList);//"ashithraj.shetty@gmail.com,asithraj.shetty@gmail.com")
        super.clickOnElement(gmailAttachFileButton);
        super.clickOnElement(gmailAttachFileMenuItem);
        super.clickOnElement(gmailImageFileToAttach);
        super.clickOnElement(gmailSendButton);
        return true;
    }

}
