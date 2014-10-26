package testingsteve;

/**
 *
 * @author Steve
 */
class ScriptWriter {
    //browsermodel calls this and it updates the generated code
    //implement different languages?

    UserInterface ui;
    int footer = 0;

    ScriptWriter(UserInterface ui) {
        this.ui = ui;
    }

    void writeHeader(String url, String browser) {
        String driverImport = "";
        String sysProp = "";
        String driverInit = "";

        switch (browser) {

            case "FF":
                driverImport = "import org.openqa.selenium.firefox.FirefoxDriver;\n";
                driverInit = "    WebDriver driver = new FirefoxDriver();\n";
                break;

            case "CR":
                driverImport = "import org.openqa.selenium.chrome.ChromeDriver;\n";
                sysProp = "    System.setProperty(\"webdriver.chrome.driver\", \"<path>\");\n";
                driverInit = "    WebDriver driver = new ChromeDriver();\n";
                break;

            case "IE":
                driverImport = "import org.openqa.selenium.ie.InternetExplorerDriver;\n";
                sysProp = "    System.setProperty(\"webdriver.ie.driver\", \"<path>\");\n";
                driverInit = "    WebDriver driver = new InternetExplorerDriver();\n";
                break;
        }

        ui.addCode("import java.util.concurrent.TimeUnit;\n"
                + driverImport
                + "import org.openqa.selenium.JavascriptExecutor;\n"
                + "import org.openqa.selenium.WebDriver;\n"
                + "import org.openqa.selenium.WebElement;\n"
                + "import org.openqa.selenium.By;\n"
                + "import org.openqa.selenium.Alert;\n"
                + "import static org.junit.Assert.*;\n\n\n"
                + "public class RecordedScript {\n\n"
                + "  public static void main(String[] args) {\n"
                + "    WebElement element;\n"
                + "    Alert alert;\n"
                + sysProp
                + driverInit
                + "    JavascriptExecutor js = (JavascriptExecutor) driver;\n"
                + "    driver.manage().timeouts().pageLoadTimeout(300, TimeUnit.SECONDS);\n"
                + "    driver.manage().timeouts().setScriptTimeout(300, TimeUnit.SECONDS);\n"
                + "    driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);\n"
                + "    driver.get(\"" + url + "\");\n\n\n"
                + "    //recorded actions//\n\n\n"
                + "    //finish//\n\n"
                + "    driver.quit();\n"
                + "  }\n"
                + "  public static void verifyEquals(String result, Object expected) {\n"
                + "    try {\n"
                + "      assertEquals(result, expected);\n"
                + "    } catch (AssertionError ae) {\n"
                + "      System.out.println(\"FAIL-expected:\"+expected+\", found:\"+result);\n"
                + "    }\n"
                + "  }\n"
                + "  public static void switchWin(WebDriver driver, String title) {\n"
                + "    String target = driver.getWindowHandle();\n"
                + "      for (String handle : driver.getWindowHandles()) {\n"
                + "      driver.switchTo().window(handle);\n"
                + "      if (driver.getTitle().equals(title)) {\n"
                + "        target = handle;\n"
                + "      }\n"
                + "    }\n"
                + "    driver.switchTo().window(target);\n"
                + "  }\n"
                + "}\n");
        footer = 24; //lines from the insert point to the bottom
    }

    void writeFindEvent(String method, String attribute) {
        ui.insertCode("    //find:" + attribute + "\n"
                + "    element = driver.findElement(By." + method + "(\"" + attribute + "\"));\n"
                + "", footer);
    }

    void writeClickEvent(String method, String attribute) {
        ui.insertCode("    //click:" + attribute + "\n"
                + "    driver.findElement(By." + method + "(\"" + attribute + "\")).click();\n"
                + "", footer);
    }

    void writeInputEvent(String data) {
        ui.insertCode("    element.clear();\n"
                + "    element.sendKeys(\"" + data + "\");\n"
                + "", footer);
    }

    void writeSelectEvent(String data) {
        ui.insertCode("    element.sendKeys(\"" + data + "\\n\");\n"
                + "", footer);
    }
    
    void writeInputjsEvent(String jsInput) {
                ui.insertCode("    js.executeScript(\""+jsInput+"\");\n"
                + "", footer);
    }
    
    void writeAlertClick(String question, boolean choice) {
        String action;
        if (choice) {
            action = "    alert.accept();\n";
        } else {
            action = "    alert.dismiss();\n";
        }
        ui.insertCode("    //alert:" + question + "\n"
                + "    alert = driver.switchTo().alert();\n"
                + action
                + "", footer);
    }

    void writeSwitchByName(String frame) {
        ui.insertCode("    //switch to:" + frame + "\n"
                + "    driver.switchTo().frame(driver.findElement(By.name(\"" + frame + "\")));\n"
                + "", footer);
    }

    void writeSwitchById(String frame) {
        ui.insertCode("    //switch to:" + frame + "\n"
                + "    driver.switchTo().frame(driver.findElement(By.id(\"" + frame + "\")));\n"
                + "", footer);
    }

    void writeSwitchBack() {
        ui.insertCode("    //switch back\n"
                + "    driver.switchTo().defaultContent();\n"
                + "", footer);
    }

    void writeSwitchWin(String title) {
        ui.insertCode("    //switch to:" + title + "\n"
                + "    switchWin(driver, \"" + title + "\");\n"
                + "", footer);
    }

    void writeVerifyElement(String element, String value, String method, boolean assertion) {
        if (value != null) {
            value = "\"" + value + "\"";
        }

        if (assertion) {
            ui.insertCode("    //assert:" + value + "\n"
                    + "    assertEquals(element.getAttribute(\"" + method + "\"), " + value + ");\n"
                    + "", footer);
        } else {
            ui.insertCode("    //verify:" + value + "\n"
                    + "    verifyEquals(element.getAttribute(\"" + method + "\"), " + value + ");\n"
                    + "", footer);
        }
    }

    void writeVerifyPage(String value, boolean assertion) {
        if (value != null) {
            value = "\"" + value + "\"";
        }

        if (assertion) {
            ui.insertCode("    //assert:" + value + "\n"
                    + "    assertEquals(driver.getTitle(), " + value + ");\n"
                    + "", footer);
        } else {
            ui.insertCode("    //verify:" + value + "\n"
                    + "    verifyEquals(driver.getTitle(), " + value + ");\n"
                    + "", footer);
        }
    }

}
