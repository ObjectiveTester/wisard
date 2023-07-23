package objectivetester;

/**
 *
 * @author Steve
 */
class TestWriter extends DefaultWriter {
    //browsermodel calls this and it updates the generated code
    //creates numbered junit test cases

    private int n;

    TestWriter(UserInterface ui) {
        this.n = 0;
        this.ui = ui;
    }

    @Override
    void writeHeader(String url, String browser) {
        String driverImport = "";
        String sysProp = "";
        String driverInit = "";

        switch (browser) {

            case "FF":
                driverImport = "import org.openqa.selenium.firefox.FirefoxDriver;\n";
                if (System.getProperty("webdriver.gecko.driver") != null) {
                    sysProp = "        System.setProperty(\"webdriver.gecko.driver\", \""+System.getProperty("webdriver.gecko.driver")+"\");\n";
                }
                driverInit = "        driver = new FirefoxDriver();\n";
                break;

            case "CR":
                driverImport = "import org.openqa.selenium.chrome.ChromeDriver;\n";
                if (System.getProperty("webdriver.chrome.driver") != null) {
                    sysProp = "        System.setProperty(\"webdriver.chrome.driver\", \""+System.getProperty("webdriver.chrome.driver")+"\");\n";
                }
                driverInit = "        driver = new ChromeDriver();\n";
                break;

            case "ED":
                driverImport = "import org.openqa.selenium.edge.EdgeDriver;\n";
                if (System.getProperty("webdriver.edge.driver") != null) {
                    sysProp = "        System.setProperty(\"webdriver.edge.driver\", \""+System.getProperty("webdriver.edge.driver")+"\");\n";
                }
                driverInit = "        WebDriver driver = new EdgeDriver();\n";
                break;

            case "SA":
                driverImport = "import org.openqa.selenium.safari.SafariDriver;\n";
                driverInit = "        driver = new SafariDriver();\n";
                break;
        }

        ui.addCode("import java.time.Duration;;\n"
                + driverImport
                + "import org.openqa.selenium.JavascriptExecutor;\n"
                + "import org.openqa.selenium.WebDriver;\n"
                + "import org.openqa.selenium.WebElement;\n"
                + "import org.openqa.selenium.By;\n"
                + "import org.openqa.selenium.Alert;\n"
                + "import org.openqa.selenium.support.ui.Select;\n"
                + "import org.junit.After;\n"
                + "import org.junit.AfterClass;\n"
                + "import org.junit.Before;\n"
                + "import org.junit.BeforeClass;\n"
                + "import org.junit.Test;\n"
                + "import static org.junit.Assert.*;\n"
                + "import org.junit.FixMethodOrder;\n"
                + "import org.junit.runners.MethodSorters;\n\n"
                + "@FixMethodOrder(MethodSorters.NAME_ASCENDING)\n"
                + "public class RecordedTest {\n\n"
                + "    public RecordedTest() {\n"
                + "    }\n"
                + "    WebElement element;\n"
                + "    Alert alert;\n"
                + "    Select selector;\n"
                + "    static WebDriver driver;\n"
                + "    static JavascriptExecutor js;\n\n"
                + "    @BeforeClass\n"
                + "    public static void setUpClass() {\n"
                + sysProp
                + driverInit
                + "        js = (JavascriptExecutor) driver;\n"
                + "        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));\n"
                + "        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(10));\n"
                + "        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));\n"
                + "        driver.get(\"" + url + "\");\n"
                + "    }\n\n"
                + "    @AfterClass\n"
                + "    public static void tearDownClass() {\n"
                + "        driver.quit();\n"
                + "    }\n\n"
                + "    public static void switchWin(WebDriver driver, String title) {\n"
                + "        String target = driver.getWindowHandle();\n"
                + "        for (String handle : driver.getWindowHandles()) {\n"
                + "            driver.switchTo().window(handle);\n"
                + "            if (driver.getTitle().equals(title)) {\n"
                + "                target = handle;\n"
                + "            }\n"
                + "        }\n"
                + "        driver.switchTo().window(target);\n"
                + "    }\n"
                + "}\n");
        footer = 18; //lines from the insert point to the bottom
    }

    @Override
    void writeStart() {
        n++;
        ui.insertCode("\n    @Test\n"
                + "    public void test" + n + "() {", footer);
    }

    @Override
    void writeEnd() {
        ui.insertCode("\n    }\n", footer);
    }
}
