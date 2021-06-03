package objectivetester;

/**
 *
 * @author Steve
 */
class Test5Writer extends DefaultWriter {
    //browsermodel calls this and it updates the generated code
    //creates numbered junit5 test cases

    private int n;

    Test5Writer(UserInterface ui) {
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
                sysProp = "        System.setProperty(\"webdriver.gecko.driver\", \"" + System.getProperty("webdriver.gecko.driver") + "\");\n";
                driverInit = "        driver = new FirefoxDriver();\n";
                break;

            case "CR":
                driverImport = "import org.openqa.selenium.chrome.ChromeDriver;\n";
                sysProp = "        System.setProperty(\"webdriver.chrome.driver\", \"" + System.getProperty("webdriver.chrome.driver") + "\");\n";
                driverInit = "        driver = new ChromeDriver();\n";
                break;

            case "IE":
                driverImport = "import org.openqa.selenium.ie.InternetExplorerDriver;\n";
                sysProp = "        System.setProperty(\"webdriver.ie.driver\", \"" + System.getProperty("webdriver.ie.driver") + "\");\n";
                driverInit = "        driver = new InternetExplorerDriver();\n";
                break;

            case "ED":
                driverImport = "import org.openqa.selenium.edge.EdgeDriver;\n";
                sysProp = "        System.setProperty(\"webdriver.edge.driver\", \"" + System.getProperty("webdriver.edge.driver") + "\");\n";
                driverInit = "        WebDriver driver = new EdgeDriver();\n";
                break;

            case "SA":
                driverImport = "import org.openqa.selenium.safari.SafariDriver;\n";
                driverInit = "        driver = new SafariDriver();\n";
                break;
        }

        ui.addCode("import java.time.Duration;\n"
                + driverImport
                + "import org.openqa.selenium.JavascriptExecutor;\n"
                + "import org.openqa.selenium.WebDriver;\n"
                + "import org.openqa.selenium.WebElement;\n"
                + "import org.openqa.selenium.By;\n"
                + "import org.openqa.selenium.Alert;\n"
                + "import org.openqa.selenium.support.ui.Select;\n"
                + "import org.junit.jupiter.api.AfterAll;\n"
                + "import org.junit.jupiter.api.AfterEach;\n"
                + "import org.junit.jupiter.api.BeforeAll;\n"
                + "import org.junit.jupiter.api.BeforeEach;\n"
                + "import org.junit.jupiter.api.Test;\n"
                + "import static org.junit.jupiter.api.Assertions.*;\n"
                + "import org.junit.jupiter.api.TestMethodOrder;\n"
                + "import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;\n"
                + "import org.junit.jupiter.api.Order;\n\n"
                + "@TestMethodOrder(OrderAnnotation.class)\n"
                + "public class RecordedTest {\n\n"
                + "    public RecordedTest() {\n"
                + "    }\n"
                + "    WebElement element;\n"
                + "    Alert alert;\n"
                + "    Select selector;\n"
                + "    static WebDriver driver;\n"
                + "    static JavascriptExecutor js;\n\n"
                + "    @BeforeAll\n"
                + "    public static void setUp() {\n"
                + sysProp
                + driverInit
                + "        js = (JavascriptExecutor) driver;\n"
                + "        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));\n"
                + "        driver.manage().timeouts().setScriptTimeout(Duration.ofSeconds(10));\n"
                + "        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));\n"
                + "        driver.get(\"" + url + "\");\n"
                + "    }\n\n"
                + "    @AfterAll\n"
                + "    public static void tearDown() {\n"
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
                + "    @Order(" + n + ")\n"
                + "    public void test" + n + "() {", footer);
    }

    @Override
    void writeEnd() {
        ui.insertCode("\n    }\n", footer);
    }
}
