package Tools;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import ConfigParser.ConfigParser;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


/*import com.charter.sit.utility.BrowserBotException;
import com.mobile.browser.automation.framrwork.MobileBaseTest;*/

import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.MobileDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.PushesFiles;
//import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.slf4j.LoggerFactory;

public class MobileAppBaseClass {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MobileAppBaseClass.class);
    private static final int TIMEOUT_IN_SECONDS = 40;
    WebDriverWait wait;
    public static long max = 60;
    public static long med = 30;
    public static long min = 10;
    // public static String Currentplatform=MobileBaseTest.DeviceOS;
    public String SelectedPage = null;
    public static String mobilePlatform = null;
    public static String deviceName = null;
    public static String bundleID = null;
    private WebDriver driver;
    private AndroidDriver dr;

    private void initDriver() throws InterruptedException, Exception {
        /*try {*/
        String reportDirectory = "report";
        String reportFormat = "xml";
        ConfigParser confParse = new ConfigParser();
        Map<String, Object> obj = new HashMap<>();
        obj = confParse.parseConfig();
        LinkedHashMap<String, ArrayList> DeviceConfig = (LinkedHashMap<String, ArrayList>) obj.get("Device");
        List<String> osList = (ArrayList<String>) DeviceConfig.get("OS");
        mobilePlatform = osList.get(0);
        List<String> deviceNameList = (ArrayList<String>) DeviceConfig.get("DeviceName");
        deviceName = deviceNameList.get(0);
        DesiredCapabilities capability = new DesiredCapabilities();
        if (mobilePlatform.toLowerCase().contains("ios")) {
            // capability.setCapability("autoDismissAlerts", true);
            capability.setCapability("reportDirectory", reportDirectory);
            capability.setCapability("reportFormat", reportFormat);
            capability.setCapability("autoAcceptAlerts", true);
            capability.setCapability("noReset", false);
            capability.setCapability("platformName", "iOS");
            capability.setCapability("automationName", "XCUITest");
            capability.setCapability("udid", "32099cab6fd9befb780f9bc0cbbdd0b73d3243d4");
            capability.setCapability("newCommandTimeout", "300");
            capability.setCapability("bundledID", bundleID);
            driver = new IOSDriver(new URL("http://0.0.0.0:4723/wd/hub"), capability);
        } else {
            capability.setCapability("autoAcceptAlerts", true);
            capability.setCapability("platformName", "Android");
            capability.setCapability("deviceName", deviceName);
            capability.setCapability("automationName", "UiAutomator1");
            capability.setCapability(MobileCapabilityType.UDID, "63e75439");//"63e75439");
            capability.setCapability("platformVersion", "11.0");
            capability.setCapability("newCommandTimeout", "300");
            capability.setCapability("noReset", true);
            capability.setCapability("fullReset", false);
            capability.setCapability("appPackage", "com.google.android.apps.photos");
            capability.setCapability("appActivity", "com.google.android.apps.photos.home.HomeActivity");


            driver = new AndroidDriver(new URL("http://0.0.0.0:4723/wd/hub"), capability);
            dr = (AndroidDriver) driver;
        }

		/*} catch (Exception e) {
			e.printStackTrace();
			LOG.info("Here in catch");
			//System.exit(1);
		}*/
    }

    public WebDriver getDriver() {
        return this.driver;
    }

    public void setDriver() throws Exception {
        initDriver();
    }

	/*public <U> U wait(WebDriver driver, ExpectedCondition<U> condition) throws Exception {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver).ignoring(RuntimeException.class)
				.withTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS).pollingEvery(500L, TimeUnit.MILLISECONDS);
		try {
			return wait.until(condition);
		} catch (TimeoutException err) {
			String errMessage = "Bot encountered a timeout while waiting for a condition,  "
					+ err.getLocalizedMessage();
			throw new Exception(errMessage);
		}
	}*/

	/*public static boolean waitForWebElementVisibilityOf(WebDriver driver, final WebElement element) {
		// LOG.info(new
		// Object(){}.getClass().getEnclosingMethod().getName()+" : "+element);
		boolean waitValue = false;
		try {
			new WebDriverWait(driver, max).until(ExpectedConditions.visibilityOf(element));
			LOG.info("Element is present: " + element.toString());
			waitValue = true;
		} catch (StaleElementReferenceException e) {
			waitValue = true;
		} catch (Exception e) {
		}
		return waitValue;
	}
	
	*/

    protected boolean waitForWebElementVisibilityOf(By elementXPath) {
        // LOG.info(new
        // Object(){}.getClass().getEnclosingMethod().getName()+" : "+element);
        boolean waitValue = false;
        WebElement element = null;
        int count = 0;
        while (true) {
            try {
                element = this.driver.findElement(elementXPath);
                break;
            } catch (Exception e) {
                if (count == 10) {
                    return false;
                }
                count++;
            }
        }
        try {
            new WebDriverWait(driver, max).until(ExpectedConditions.visibilityOf(element));
            LOG.info("Element is present: " + element.toString());
            waitValue = true;
        } catch (StaleElementReferenceException e) {
            waitValue = true;
        } catch (Exception e) {
        }
        return waitValue;
    }

    protected boolean waitForWebElementVisibilityOf(By elementXPath, long timeout) {
        // LOG.info(new
        // Object(){}.getClass().getEnclosingMethod().getName()+" : "+element);
        boolean waitValue = false;
        int count = 0;

        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(this.driver.findElement(elementXPath)));
//            LOG.info("Element is present: " + element.toString());
            waitValue = true;
        } catch (StaleElementReferenceException e) {
            waitValue = true;
        } catch (Exception e) {
        }
        return waitValue;
    }

    public static boolean waitForPageLoad(WebDriver driver) {
        // LOG.info(new
        // Object(){}.getClass().getEnclosingMethod().getName());
        boolean waitValue = false;
        try {
            driver.manage().timeouts().pageLoadTimeout(max, TimeUnit.SECONDS);
            waitValue = true;
        } catch (TimeoutException e) {
            waitValue = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return waitValue;
    }

	/*public static boolean waitForWebElementVisibilityOf(WebDriver driver, final WebElement element,
			long timeinSeconds) {
		// LOG.info(new
		// Object(){}.getClass().getEnclosingMethod().getName()+" : "+element);
		boolean waitValue = false;
		try {
			new WebDriverWait(driver, timeinSeconds).until(ExpectedConditions.visibilityOf(element));
			waitValue = true;
		} catch (TimeoutException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return waitValue;
	}

	public static boolean waitForURLContains(WebDriver driver, String PartialURL) {
		// LOG.info(new
		// Object(){}.getClass().getEnclosingMethod().getName()+" :
		// "+PartialURL);
		boolean waitValue = false;
		try {
			new WebDriverWait(driver, min).until(ExpectedConditions.titleContains(PartialURL));
			waitValue = true;
		} catch (TimeoutException e) {
			LOG.info("URL does not contain " + PartialURL + " after waiting for " + min + " seconds");
		} catch (Exception e) {
			LOG.info("exception caught in waitForURLContains method:");
			LOG.info("URL does not contain " + PartialURL);
			e.printStackTrace();
		}
		return waitValue;
	}*/

	/*public static boolean waitForURLContains(WebDriver driver, String PartialURL, int timeOutInSeconds) {
		// LOG.info(new
		// Object(){}.getClass().getEnclosingMethod().getName()+" :
		// "+PartialURL);
		boolean waitValue = false;
		try {
			new WebDriverWait(driver, timeOutInSeconds).until(ExpectedConditions.titleContains(PartialURL));
			LOG.info("URL contains " + PartialURL);
			waitValue = true;
		} catch (TimeoutException e) {
			LOG.info(
					"URL does not contain " + PartialURL + " after waiting for " + timeOutInSeconds + " seconds");
		} catch (Exception e) {
			LOG.info("exception caught in waitForURLContains method:");
			LOG.info("URL does not contain " + PartialURL);
			e.printStackTrace();
		}
		return waitValue;
	}*/

	/*public static boolean waitForWebElementInvisibilityOf(WebDriver driver, final WebElement element) {
		LOG.info(new Object() {
		}.getClass().getEnclosingMethod().getName() + " : " + element);
		boolean waitValue = false;
		ArrayList<WebElement> elements = new ArrayList<WebElement>();
		elements.add(element);
		try {
			new WebDriverWait(driver, max).until(ExpectedConditions.visibilityOf(element));// .invisibilityOfAllElements(elements));
			waitValue = true;
		} catch (ElementNotVisibleException e) {
			LOG.info("Element is NOT present: " + element.toString());
			waitValue = true;
		} catch (NoSuchElementException e) {
			LOG.info("Element is NOT present: " + element.toString());
			waitValue = true;
		} catch (java.util.NoSuchElementException e) {
			LOG.info("Element is NOT present: " + element.toString());
			waitValue = true;
		} catch (TimeoutException e) {
			LOG.info("Element: " + element.toString() + "is still visible");
			waitValue = false;
		} catch (Exception e) {
			LOG.info("exception caught in waitForWebElementInvisibilityOf method:");
			LOG.info(e);
		}
		return waitValue;
	}*/

    protected void sleep(int timeOutInSeconds) {
        LOG.info(new Object() {
        }.getClass().getEnclosingMethod().getName() + " for " + timeOutInSeconds + " seconds");
        try {
            Thread.sleep(timeOutInSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

//	protected void clickOnElement(WebDriver driver, int startX, int startY) {
//		LOG.info(new Object() {
//		}.getClass().getEnclosingMethod().getName() + " : X-" + startX + " Y-" + startY);
//		try {
//			if (!TestNGInitializer.mobilePlatform.equalsIgnoreCase("ios")) {
//				TouchAction action = new TouchAction(((AndroidDriver) driver));
//				action.tap(startX, startY).perform();
//			} else {
//				TouchAction action = new TouchAction(((IOSDriver) driver));
//				action.tap(startX, startY).perform();
//				// action.press(startX, startY).perform();
//			}
//			sleep(3);
//			// LOG.info("Clicked on element: " + element);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    protected void clickOnElement(WebElement element) {
        LOG.info(new Object() {
        }.getClass().getEnclosingMethod().getName() + " : " + element.toString());
        try {
            element.click();
            // LOG.info("Clicked on element: " + element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void clickOnElement(By element) {
        LOG.info(new Object() {
        }.getClass().getEnclosingMethod().getName() + " : " + element.toString());
        int count = 0;
        WebElement element1 = null;
        while (true) {
            try {
                element1 = dr.findElement(element);
                element1.click();
                break;
                // LOG.info("Clicked on element: " + element);
            } catch (Exception e) {
                e.printStackTrace();
                if (count == 10) {
                    return;
                }
                count++;
            }
        }

    }

    protected void clickOnElement(WebDriver driver, WebElement element) {
        LOG.info(new Object() {
        }.getClass().getEnclosingMethod().getName() + " : " + element.toString());
        try {
            // WebElement element1=driver.findElement((By) element);
            element.click();
            // LOG.info("Clicked on element: " + element);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getWindowHandle(WebDriver driver) {
        return driver.getWindowHandle();
    }

    protected void clickOnElementWithText(WebElement element, String elementText) {
        @SuppressWarnings("unchecked")
        List<WebElement> elementList = (List<WebElement>) element;
        int index = 0;
        for (WebElement element2 : elementList) {
            if ((element2.getText().trim().equalsIgnoreCase(elementText)) && (element2.isDisplayed())) {
                element2.click();
                ++index;
                break;
            }
        }
        if (index == elementList.size())
            throw new RuntimeException("Could not locate any element described by the locator " + element.toString()
                    + " with text " + elementText);
    }

    protected void type(WebElement element, String text) {
        LOG.info("Entering Text");
        element.sendKeys(new CharSequence[]{text});
        LOG.info("Entered " + text + " into the " + element + " text field");
    }

    protected void type(By element, String text, WebDriver driver) {
        LOG.info("Entering Text");
        WebElement element1 = driver.findElement(element);
        element1.click();
        element1.clear();
        element1.sendKeys(new CharSequence[]{text});
        LOG.info("Entered " + text + " into the " + element + " text field");
    }

    protected void type(WebElement element, String text, Keys keys) {
        LOG.info("Entering " + text + " into the " + element + " text field and pressing " + keys.toString());

        element.sendKeys(new CharSequence[]{text});
        element.sendKeys(new CharSequence[]{keys});
    }

    protected void goToUrl(WebDriver driver, String url) {
        LOG.info("Loading the URL:" + url);
        driver.get(url);
    }

    protected String getCssAttribute(WebElement element, String attribute) {
        LOG.info("Getting CSS value of " + attribute + " from the locator " + element);

        return element.getCssValue(attribute);
    }

    protected String getAttribute(WebElement element, String attribute) {
        String value = element.getAttribute(attribute);
        LOG.info("Read " + attribute + " attribute value " + value + ", from the locator " + element);

        return value;
    }

    protected void submit(WebElement element) {
        LOG.info("Submitting the Form");
        element.submit();
    }

    protected int getNumberOfOpenWindows(WebDriver driver) {
        return driver.getWindowHandles().size();
    }

    protected void navigateBack(WebDriver driver) {
        driver.navigate().back();
        LOG.info("Navigating to the previous page");
    }

    protected void navigateForward(WebDriver driver) {
        driver.navigate().forward();
        LOG.info("Navigating to the next page");
    }

    protected void selectValueFromDropDown(WebElement element, String visibleText) {
        LOG.info("Selecting " + visibleText + " from the DropDown");
        WebElement dropDownElement = element;
        Select dropDownSelect = new Select(dropDownElement);
        dropDownSelect.selectByVisibleText(visibleText);
    }

    protected void selectValueFromDropDown(WebElement element, int index) {
        LOG.info("Selecting " + index + " from the DropDown");
        WebElement dropDownElement = element;
        Select dropDownSelect = new Select(dropDownElement);
        dropDownSelect.selectByIndex(index);
    }

    protected void dragAndDrop(WebDriver driver, WebElement fromElement, WebElement toElement) {
        Actions action = new Actions(driver);
        action.dragAndDrop(fromElement, toElement).build().perform();
    }

    protected void forceRefresh(WebDriver driver) {
        Actions action = new Actions(driver);
        LOG.info("Forcefully refreshing the page");
        action.keyDown(Keys.CONTROL).sendKeys(new CharSequence[]{Keys.F5}).keyUp(Keys.CONTROL).perform();
    }

    protected void hoverOver(WebDriver driver, WebElement element) {
        Actions action = new Actions(driver);
        LOG.info("Hovering over the mouse on the element " + element);
        action.moveToElement(element).build().perform();
        ((JavascriptExecutor) driver).executeScript("$('arguments[0]').hover();", element);
    }

    protected void hoverOver1(WebDriver driver, WebElement element, WebElement element1) {
        Actions action = new Actions(driver);
        LOG.info("Hovering over the mouse on the element " + element);
        action.moveToElement(element).click(element1).build().perform();
    }

    protected void pressKey(WebElement element, Keys key) {
        element.sendKeys(new CharSequence[]{key});
    }

    protected Boolean isElementEnabled(WebElement element) {
        return Boolean.valueOf(element.isEnabled());
    }

    protected void switchToWindowByTitle(WebDriver driver, String titleOfNewWindow) {
        Set<?> windowHandles = driver.getWindowHandles();
        for (Object windowHandle : windowHandles) {
            driver.switchTo().window((String) windowHandle);
            if (driver.getTitle().contains(titleOfNewWindow))
                return;
        }
    }

    protected void switchToDefaultContent(WebDriver driver) {
        driver.switchTo().defaultContent();
    }

    protected void refreshPage(WebDriver driver) {
        driver.navigate().refresh();
    }

    protected void doubleClick(WebDriver driver, WebElement element) {
        Actions builder = new Actions(driver);
        Action hoverOverRegistrar = (Action) builder.doubleClick(element);
        hoverOverRegistrar.perform();
    }

    protected void clearCookies(WebDriver driver) {
        driver.manage().deleteAllCookies();
    }

    protected String getCurrentUrl(WebDriver driver) {
        String retVal = null;
        try {
            LOG.info("Current URL: " + driver.getCurrentUrl());
            return retVal = driver.getCurrentUrl();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    protected List<WebElement> findElements(WebElement element) throws Exception {
        try {
            return findElements(element);
        } catch (NoSuchElementException e) {
            String msg = "Element could not be located " + element.toString();
            LOG.info(msg);
            throw new Exception(msg);
        }
    }

    protected void closePopupWindow(WebDriver driver) {
        driver.close();
        Iterator<?> i$ = driver.getWindowHandles().iterator();
        if (!(i$.hasNext()))
            return;
        String name = (String) i$.next();
        driver.switchTo().window(name);
        LOG.info("popup window closed : " + name);
    }

    protected void closePopupWindow(WebDriver driver, String windowID) {
        driver.switchTo().window(windowID).close();
    }

    protected void closepopUpAndSwitchtoParent(WebDriver driver, String windowID) {
        closePopupWindow(driver, windowID);
        Iterator<?> i$ = driver.getWindowHandles().iterator();
        if (!(i$.hasNext()))
            return;
        String name = (String) i$.next();
        driver.switchTo().window(name);
    }

    protected boolean isElementChecked(WebElement element) {
        LOG.info("Checking if element is selected: " + element);
        if (element.isSelected()) {
            LOG.info("Element is checked: " + element.toString());
            return true;
        }

        LOG.info("Element is NOT checked: " + element.toString());
        return false;
    }

	/*protected static boolean isElementPresent(WebElement locator, WebDriver driver) {

		boolean waitValue = false;
		try {
			waitValue = new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOf(locator)).isDisplayed();
			LOG.info(locator + "Is Present");

		} catch (Exception e) {
			LOG.info(locator + "Is Not present on the page");
			// LOGGER.error(e);
		}
		return waitValue;

	}*/

//	protected static boolean isElementPresent(By locator, WebDriver driver) {
//		boolean waitValue = false;
//		boolean waitnew = false;
//
//		try {
//			driver=TestNGInitializer.driver;
//			// Thread.sleep(6000);
//			/*WebElement element= driver.findElement(locator);
//			while(!element.isDisplayed()){
//				Thread.sleep(1000);
//			}*/
//			//WebElement element= driver.findElement(locator);
//			//waitValue = new WebDriverWait(driver,300).ignoring(NoSuchElementException.class).until(ExpectedConditions.visibilityOf(driver.findElement(locator))).isDisplayed();
//			LOG.info("Waiting for element to be visible -->"+locator);
//			waitnew = new WebDriverWait(driver,30)
//    				.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
//    		waitValue=true;
//			LOG.info(locator + "Is Present");
//			LOG.info(locator + "Is Present");
//			//waitValue = true;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.info(locator + "Is Not present on the page");
//		}
//		return waitValue;
//	}

    protected static boolean isElementVisible(WebElement element) {
        LOG.info("Checking for visibility of element: " + element);
        if (element.isDisplayed()) {
            LOG.info("Element is present: " + element.toString());
            return true;
        }
        LOG.info("Element is NOT present: " + element.toString());
        return false;
    }

    protected static boolean isElementVisible(By locator, WebDriver driver) {
        WebElement element = driver.findElement(locator);
        LOG.info("Checking for visibility of element: " + element);
        if (element.isDisplayed()) {
            LOG.info("Element is present: " + element.toString());
            return true;
        }
        LOG.info("Element is NOT present: " + element.toString());
        return false;
    }

    protected void switchToParentWindow(WebDriver driver, String parentWindowId) {
        String windowId = "";
        Set<?> set = driver.getWindowHandles();
        LOG.info("Number of windows opened: " + set.size());
        Iterator<?> iterator = set.iterator();
        while (iterator.hasNext()) {
            windowId = (String) iterator.next();
            if (windowId.equals(parentWindowId)) {
                LOG.info("Switching to the window: " + parentWindowId);
                driver.switchTo().window(parentWindowId);
            }
            LOG.info("windowId" + windowId);
        }
    }

    protected void selectMultipleListItems(WebDriver driver, WebElement listBoxLocator, int[] indexes) {
        Actions action = new Actions(driver);
        WebElement listItem = listBoxLocator;
        List<?> listOptions = listItem.findElements(By.tagName("option"));

        action.keyDown(Keys.CONTROL).perform();
        for (int i : indexes) {
            ((WebElement) listOptions.get(i)).click();

        }
        action.keyUp(Keys.CONTROL).perform();
    }

    public static void getScreenshot(AndroidDriver driver, String outputlocation) throws Exception {
        try {
            // Thread.sleep(1000);
            LOG.info("Capturing the snapshot of the page ");
            for (int i = 1; i < 6; i++) {
                File srcFiler = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(srcFiler, new File(outputlocation + i + ".png"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void switchToPopUpWindow(WebDriver driver, String parentWindowId) {
        String windowId = "";
        Set<?> set = driver.getWindowHandles();
        LOG.info("Number of windows opened: " + set.size());
        Iterator<?> iterator = set.iterator();
        while (iterator.hasNext()) {
            windowId = (String) iterator.next();
            if (!(windowId.equals(parentWindowId))) {
                LOG.info("Switching to the window: " + parentWindowId);
                driver.switchTo().window(windowId);
            }
            LOG.info("Popup windowId" + windowId);
        }
    }

    protected void switchToFrame(WebDriver driver, WebElement frame) {
        LOG.info("Switching to the frame: " + frame);
        driver.switchTo().frame(frame);
    }

    protected String getText(WebDriver driver, WebElement element) {
        LOG.info("Getting the text of the element: " + element);
        return element.getText();
    }

    protected String getText(By locator) {

        WebElement element = null;
        int count = 0;
        while (true) {
            try {
                element = this.driver.findElement(locator);
                break;
            } catch (Exception e) {
                if (count == 10) {
                    return null;
                }
                count++;
            }
        }
        driver.findElement(locator);
        String text = "";
        boolean waitValue = false;
        LOG.info("Getting the text of the element: " + element);
        try {
            waitValue = new WebDriverWait(driver, 15)
                    .until(ExpectedConditions.visibilityOf(driver.findElement(locator))).isDisplayed();
            waitValue = true;
            text = element.getText();
        } catch (Exception e) {
            LOG.info(locator + "does not have text attribute");
        }
        return text;
    }

    protected String getAlertText(WebDriver driver) {
        LOG.info("Getting the Alert text");
        return driver.switchTo().alert().getText();
    }

    protected void acceptAlert(WebDriver driver) {
        LOG.info("Confirming the operation");
        driver.switchTo().alert().accept();
    }

    protected void dismissAlert(WebDriver driver) {
        LOG.info("Cancelling the operation");
        driver.switchTo().alert().dismiss();
    }

	/*protected boolean isAlertPresent(WebDriver driver) {
		LOG.info("checking for alert popup");
		boolean foundAlert = false;
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.alertIsPresent());
			foundAlert = true;
		} catch (TimeoutException e) {
			foundAlert = false;
		}
		return foundAlert;
	}*/

    protected String executeJavaScript(WebDriver driver, String script) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String text = (String) jsExecutor.executeScript(script, new Object[0]);
        return text;
    }

    protected Set<String> getWindowHandles(WebDriver driver) {
        return driver.getWindowHandles();
    }

    protected String getWindowTitle(WebDriver driver) {
        LOG.info("Getting the title of the page");
        return driver.getTitle();
    }

    protected boolean switchToWindowWithElement(WebDriver driver, WebElement element) {
        for (String window : driver.getWindowHandles()) {
            driver.switchTo().window(window);
            if (isElementVisible(element))
                return true;
        }
        return false;
    }

    protected boolean switchToWindowWithURLPart(WebDriver driver, String URLPart) {
        for (String window : driver.getWindowHandles()) {
            driver.switchTo().window(window);
            if (getCurrentUrl(driver).contains(URLPart))
                return true;
        }
        return false;
    }

    protected List<String> getTextOfSimilarElements(WebDriver driver, String xpath) {
        List<String> elementTextList = new ArrayList<String>();
        try {
            List<WebElement> elementList = driver.findElements(By.xpath(xpath));
            LOG.info(
                    "There are " + elementList.size() + " Similar elements(Element with locator " + xpath + " )");

            for (WebElement anElement : elementList) {
                elementTextList.add(anElement.getText());
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return elementTextList;
    }

    protected int getSizeOfSimilarElements(WebDriver driver, String xpath) {
        int retVal = 0;
        try {
            List<WebElement> elementList = driver.findElements(By.xpath(xpath));
            retVal = elementList.size();
            LOG.info(
                    "There are " + elementList.size() + " Similar elements(Element with locator " + xpath + " )");

        } catch (Exception e) {
            e.getMessage();
        }
        return retVal;
    }

    protected String getPageSource() {
        return getPageSource();
    }

    protected void setCheckBox(WebElement element, boolean check) {
        WebElement checkbox = element;
        if (check) {
            if (!(checkbox.isSelected())) {
                checkbox.click();
            }
        } else if (checkbox.isSelected())
            checkbox.click();
    }

    protected void clearTextBox(WebElement element) {
        element.clear();
    }

    protected void chooseOptionUsingJavaScript(WebDriver driver, WebElement propKey, boolean select) {

        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        LOG.info("Choosing the option button " + propKey + " using Java Script");
        jsExecutor.executeScript("arguments[0].checked = arguments[1];",
                new Object[]{propKey, Boolean.valueOf(select)});
    }

    protected void clickOnElementUsingJavaScript(WebDriver driver, WebElement propKey) {
        // LOG.info("ClickUsingJS: "+driver);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        LOG.info("Clicking on the element " + propKey + " using JavaScript");
        /*
         * jsExecutor.executeScript("arguments[0].click();", new Object[] {
         * propKey });
         */
        jsExecutor.executeScript("arguments[0].click();", propKey);
    }

    protected void typeUsingJavaScript(WebDriver driver, WebElement propKey, String text) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        LOG.info("typing " + text + " on the element " + propKey + " using JavaScript");
        jsExecutor.executeScript("arguments[0].value=arguments[1];", new Object[]{propKey, text});
    }

    protected void scrollToElementJavaScript(WebDriver driver, WebElement propKey, boolean scrollTop) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        LOG.info("scrolling to the element " + propKey + " using JavaScript");
        jsExecutor.executeScript("arguments[0].scrollIntoView(arguments[1]);",
                new Object[]{propKey, Boolean.valueOf(scrollTop)});
    }

    public void Scroll(WebDriver driver, int startX, int StartY, int endX, int endY) throws InterruptedException {
        LOG.info("Scrolling");
        try {
            TouchAction action;
            if (!mobilePlatform.equalsIgnoreCase("ios")) {
                action = new TouchAction(((AndroidDriver) driver));
            } else {
                action = new TouchAction(((IOSDriver) driver));
            }
            //action.longPress(startX, StartY).moveTo(endX, endY).release().perform();
        } catch (Exception e) {
            LOG.info("Exception block inside SScrollDownEndOfPage method.");
            e.printStackTrace();
        }
    }

    protected String getBrowser(WebDriver driver) {
        Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
        String browserName = cap.getBrowserName().toLowerCase();
        return browserName;
    }

    protected void Swipe(String dir) throws InterruptedException {
        LOG.info("Entered Swipe Loop");
        final int ANIMATION_TIME = 200; // ms

        final int PRESS_TIME = 200; // ms

        int edgeBorder = 10; // better avoid edges
        PointOption pointOptionStart, pointOptionEnd;

        // init screen variables
        Dimension dims = driver.manage().window().getSize();

        // init start point = center of screen
        pointOptionStart = PointOption.point(dims.width / 2, dims.height / 2);

        switch (dir) {
            case "down": // center of footer
                pointOptionEnd = PointOption.point(dims.width / 2, dims.height - edgeBorder);
                break;
            case "up": // center of header
                pointOptionEnd = PointOption.point(dims.width / 2, edgeBorder);
                break;
            case "left": // center of left side
                pointOptionEnd = PointOption.point(edgeBorder, dims.height / 2);
                break;
            case "right": // center of right side
                pointOptionEnd = PointOption.point(dims.width - edgeBorder, dims.height / 2);
                break;
            default:
                throw new IllegalArgumentException("swipeScreen(): dir: '" + dir + "' NOT supported");
        }

        // execute swipe using TouchAction
        try {
            new TouchAction(dr)
                    .press(pointOptionStart)
                    // a bit more reliable when we add small wait
                    .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                    .moveTo(pointOptionEnd)
                    .release().perform();
        } catch (Exception e) {
            System.err.println("swipeScreen(): TouchAction FAILED\n" + e.getMessage());
            return;
        }

        // always allow swipe action to complete
        try {
            Thread.sleep(ANIMATION_TIME);
        } catch (InterruptedException e) {
            // ignore
        }
    }

//	public void Scroll(WebDriver driver, int startX, int StartY, int endX, int endY) throws InterruptedException {
//		LOG.info("Scrolling");
//		try {
//			TouchAction action;
//			if (!TestNGInitializer.mobilePlatform.equalsIgnoreCase("ios")) {
//				action = new TouchAction(((AndroidDriver) driver));
//			} else {
//				action = new TouchAction(((IOSDriver) driver));
//			}
//			action.longPress(startX, StartY).moveTo(endX, endY).release().perform();
//		} catch (Exception e) {
//			LOG.info("Exception block inside SScrollDownEndOfPage method.");
//			e.printStackTrace();
//		}
//	}

    public static void fn_ScrollToBottom(WebDriver driver) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(1000);
    }

    public static void fn_ScrollingByCoordinatesofAPage(WebDriver driver, int coOrdinate) throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + coOrdinate + ")");
        Thread.sleep(1000);
    }

    public static void fn_ScrollToAParticlarElement(WebDriver driver, WebElement element) throws InterruptedException {
        // WebElement
        // element=driver.findElement(By.xpath(cst.OR.getProperty(object)));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
        Thread.sleep(1000);
    }

    public List<String> delimiterSplit(String delimitertext) {
        List<String> datalist = new ArrayList<>();
        try {
            if (delimitertext.contains(";")) {
                String[] split = delimitertext.split(";");
                for (String string : split) {
                    datalist.add(string);
                }
            } else {
                datalist.add(delimitertext);
            }
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return datalist;
    }

    //	public void appRefresh() {
//		if (!TestNGInitializer.mobilePlatform.equalsIgnoreCase("IOS")) {
//			/*
//			 * if (!Environment.equalsIgnoreCase("yes")) {
//			 *
//			 * }
//			 *//* else */ {
//				LOG.info("Refreshing app on device");
//				((AndroidDriver) driver).pressKeyCode(187);
//				sleep(3);
//				((AndroidDriver) driver).pressKeyCode(187);
//			}
//		}
//	}
//
//
//	public static String captureScreenshot(String imgPath) {
//		LOG.info("Capturing Screenshot");
//		Screen s = new Screen();
//		String retval = null;
//		try {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd hh mm ss a");
//			Calendar now = Calendar.getInstance();
//
//			File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//			String filename=formatter.format(now.getTime()) + ".png";
//			retval = imgPath + filename +".png";
//		    File targetFile=new File(retval);
//		    FileUtils.copyFile(f,targetFile);
//			return retval;
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.info(e.getMessage());
//			return retval;
//		}
//	}
    protected void copyFileToDevice(String srcPath, String destPath) throws IOException {
        try {
            dr.pushFile(destPath, new File(srcPath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    protected void changeScreenOrientation(String orientationType) {
        switch (orientationType) {
            case "landscape":
                dr.rotate(ScreenOrientation.LANDSCAPE);
                break;
            case "portrait":
                dr.rotate(ScreenOrientation.PORTRAIT);
                break;
            default:
                break;
        }
    }

}