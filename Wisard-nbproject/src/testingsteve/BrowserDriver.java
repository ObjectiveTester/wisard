package testingsteve;

/**
 *
 * @author Steve
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.*;

class BrowserDriver implements Runnable {

    WebDriver driver;
    JavascriptExecutor js;
    UserInterface ui;
    DefaultWriter writer;
    boolean safe = false;

    BrowserDriver(UserInterface ui) {
        this.ui = ui;
    }

    void initWriter(String selected) {
        if (selected.contains("junit")) {
            writer = new TestWriter(ui);
        } else {
            writer = new ScriptWriter(ui);
        }
    }

    boolean initFF(String url, String path) {
        //FF
        try {
            System.setProperty("webdriver.gecko.driver", path);
            driver = new FirefoxDriver();
            js = (JavascriptExecutor) driver;
            writer.writeHeader(url, "FF");
            driver.get(url);
        } catch (IllegalStateException ise) {
            ui.errorMessage("Path to Gecko driver is invalid, check settings.");
            return false;
        } catch (WebDriverException e) {
            ui.errorMessage("Invalid URL?");
            return false;
        }
        return true;
    }

    boolean initCR(String url, String path) {
        //Chrome
        try {
            System.setProperty("webdriver.chrome.driver", path);
            driver = new ChromeDriver();
            js = (JavascriptExecutor) driver;
            writer.writeHeader(url, "CR");
            driver.get(url);
        } catch (IllegalStateException ise) {
            ui.errorMessage("Path to Chrome driver is invalid, check settings.");
            return false;
        } catch (WebDriverException e) {
            ui.errorMessage("Invalid URL?");
            return false;
        }
        return true;
    }

    boolean initIE(String url, String path) {
        //IE
        try {
            System.setProperty("webdriver.ie.driver", path);
            driver = new InternetExplorerDriver();
            js = (JavascriptExecutor) driver;
            writer.writeHeader(url, "IE");
            driver.get(url);
        } catch (IllegalStateException ise) {
            ui.errorMessage("Path to IE driver is invalid, check settings.");
            return false;
        } catch (WebDriverException e) {
            ui.errorMessage("Invalid URL?");
            return false;
        }
        return true;
    }

    boolean initED(String url, String path) {
        //Edge
        try {
            System.setProperty("webdriver.edge.driver", path);
            driver = new EdgeDriver();
            js = (JavascriptExecutor) driver;
            writer.writeHeader(url, "ED");
            driver.get(url);
        } catch (IllegalStateException ise) {
            ui.errorMessage("Path to Edge driver is invalid, check settings.");
            return false;
        } catch (WebDriverException e) {
            ui.errorMessage("Invalid URL?");
            return false;
        }
        return true;
    }

    boolean initSA(String url) {
        //SA
        try {
            driver = new SafariDriver();
            js = (JavascriptExecutor) driver;
            writer.writeHeader(url, "SA");
            driver.get(url);
        } catch (SessionNotCreatedException e) {
            ui.errorMessage(e.getMessage().substring(0, e.getMessage().indexOf(10)));
            return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        if (driver != null) {
            safe = false;
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
            //examine the root contents
            examine(new StringBuffer(Const.PAGE));
            safe = true;
        }
    }

    void examine(StringBuffer t) {
        try {
            List<WebElement> inputElements = new ArrayList<>();
            String value;
            String location = t.toString();
            StringBuffer target = new StringBuffer(t.toString());
            driver.switchTo().defaultContent();
            //System.out.println("location: " + location);

            if (target.toString().contentEquals(Const.PAGE)) {
                String current = driver.getWindowHandle();
                //indicate current window
                ui.addItem(Const.TITLE, Const.CURRENT, "", "", driver.getTitle(), current, true);
                //show all available windows
                List<String> winList = new ArrayList<>(driver.getWindowHandles());
                for (String handle : winList) {
                    driver.switchTo().window(handle);
                    if (!handle.contentEquals(current)) {
                        ui.addItem(Const.TITLE, Const.WINDOW, "", "", driver.getTitle(), handle, true);
                    }
                }
                driver.switchTo().window(current);

            } else {
                //drill down the frames stack
                traverse(target.toString(), false);
            }

            try {
                //links
                //System.out.println("links:" + Integer.parseInt((js.executeScript("return document.links.length;")).toString()));
                List<WebElement> links = (List<WebElement>) (js.executeScript("return document.links;"));
                for (WebElement element : links) {
                    ui.addItem("link", location, element.getAttribute("text"), element.getAttribute("id"), element.getAttribute("href"), element, element.isDisplayed());
                }
            } catch (Exception e) {
                ui.errorMessage("Failed to find links");
            }

            try {
                //forms
                //System.out.println("forms:" + Integer.parseInt((js.executeScript("return document.forms.length;")).toString()));
                //you can't return all the forms, so do each individually
                int i = 0;
                WebElement form = (WebElement) ((js.executeScript("return document.forms[" + i + "];")));
                while (form != null) {
                    ui.addItem("form" + i, location, form.getAttribute("name"), form.getAttribute("id"), "", form, form.isDisplayed());
                    //System.out.println("form " + i + ", " + Integer.parseInt((js.executeScript("return document.forms[" + i + "].length;")).toString()) + " elements");

                    List<WebElement> formElement = (List<WebElement>) (js.executeScript("return document.forms[" + i + "].elements;"));
                    for (WebElement element : formElement) {
                        if ((element.getAttribute("type").contentEquals("radio")) || (element.getAttribute("type").contentEquals("checkbox"))) {
                            value = element.getAttribute("checked");
                        } else {
                            value = element.getAttribute("value");
                        }

                        ui.addItem("form" + i + ":" + element.getAttribute("type"), location, element.getAttribute("name"), element.getAttribute("id"), value, element, element.isDisplayed());
                        inputElements.add(element);
                    }
                    i++;
                    form = (WebElement) ((js.executeScript("return document.forms[" + i + "];")));
                }
            } catch (Exception e) {
                ui.errorMessage("Failed to find form content");
            }

            try {
                //non-form input elements
                List<WebElement> inputs = driver.findElements(By.tagName("input"));
                inputs.addAll(driver.findElements(By.tagName("select")));
                inputs.addAll(driver.findElements(By.tagName("button")));

                for (WebElement element : inputs) {
                    //see if we've added this webElement already
                    Boolean present = false;
                    for (WebElement in : inputElements) {
                        if (in.equals(element)) {
                            present = true;
                        }
                    }
                    if (!present) {
                        if ((element.getAttribute("type").contentEquals("radio")) || (element.getAttribute("type").contentEquals("checkbox"))) {
                            value = element.getAttribute("checked");
                        } else {
                            value = element.getAttribute("value");
                        }
                        ui.addItem("input:" + element.getAttribute("type"), location, element.getAttribute("name"), element.getAttribute("id"), value, element, element.isDisplayed());
                        inputElements.add(element);
                    }

                }
            } catch (Exception e) {
                ui.errorMessage("Failed to find input elements");
            }

            try {
                //images
                //System.out.println("images:" + Integer.parseInt((js.executeScript("return document.images.length;")).toString()));
                List<WebElement> images = (List<WebElement>) (js.executeScript("return document.images;"));
                for (WebElement element : images) {
                    ui.addItem("image", location, element.getAttribute("alt"), element.getAttribute("id"), element.getAttribute("src"), element, element.isDisplayed());
                }
            } catch (Exception e) {
                ui.errorMessage("Failed to find images");
            }

            try {
                //anchors
                //System.out.println("anchors:" + Integer.parseInt((js.executeScript("return document.anchors.length;")).toString()));
                List<WebElement> anchors = (List<WebElement>) (js.executeScript("return document.anchors;"));
                for (WebElement element : anchors) {
                    ui.addItem("anchor", location, element.getAttribute("text"), element.getAttribute("id"), element.getAttribute("name"), element, element.isDisplayed());
                }
            } catch (Exception e) {
                ui.errorMessage("Failed to find anchors");
            }

            //frame nav
            //find the frames and iframes on this page
            //do it the hard way, xml parsers are too strict
            String frame = "";

            Pattern frametag = Pattern.compile("(?i)(<i?frame[^set]\\b[^>]*>(.*?))");
            Matcher fm = frametag.matcher(driver.getPageSource());
            while (fm.find()) {
                Pattern idtag = Pattern.compile("(?i)(?: id)\\s*=\\s*\"(\\S+)\"");
                Pattern nametag = Pattern.compile("(?i)(?: name)\\s*=\\s*\"(\\S+)\"");
                Matcher im = idtag.matcher(fm.group(1));
                Matcher nm = nametag.matcher(fm.group(1));
                while (im.find()) {
                    //use 'id'...          
                    frame = Const.ID + im.group(1);
                }
                while (nm.find()) {
                    //unles 'frames' is set
                    frame = nm.group(1);
                }

                //System.out.println("f:" + frame + " from " + fm.group(1));
                if (frame.contains("/")) {
                    //nested locators dont work
                    //change this so we try to elementFind it as a test first?
                    frame = "";
                }
                if (!frame.isEmpty()) {
                    //if there is no name or id we can't navigate to the frame

                    if (target.toString().contentEquals(Const.PAGE)) {
                        target.delete(0, target.length());
                        examine(target.append(frame));
                    } else {
                        examine(target.append(Const.SEP).append(frame));
                    }

                    //reset location for subsequent frames at this location
                    target = new StringBuffer(location);

                }
            }

        } catch (TimeoutException | StaleElementReferenceException | ElementNotVisibleException e) {
            ui.rescan();
        } catch (WebDriverException e) {
            ui.abort();
        }
    }

    boolean safe() {
        return safe;
    }

    void close() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (WebDriverException e) {
                driver = null;
            }
        }
    }

    private WebElement waitfor(By method) {
        WebElement webElement = null;
        try {
            webElement = new WebDriverWait(driver, 5).until(ExpectedConditions.presenceOfElementLocated(method));
        } catch (TimeoutException | StaleElementReferenceException e) {
            ui.rescan();
        }
        return webElement;
    }

    private void traverse(String frameList, Boolean write) {
        String[] frames = frameList.split(Const.SEP);
        for (String frame : frames) {
            if (frame.startsWith(Const.ID)) {
                WebElement nextFrame = waitfor(By.id(frame.replace(Const.ID, "")));
                //System.out.println("by.id " + frame.replace(Const.ID, ""));
                if (write) {
                    writer.writeSwitchById(frame.replace(Const.ID, ""));
                }
                driver.switchTo().frame(nextFrame);
            } else {
                WebElement nextFrame = waitfor(By.name(frame));
                //System.out.println("by.frames " + frame);
                if (write) {
                    writer.writeSwitchByName(frame);
                }
                driver.switchTo().frame(nextFrame);
            }
        }
    }

    void highlight(WebElement webElement, String target) {
        if (!target.endsWith(Const.INVISIBLE)) {
            try {
                driver.switchTo().defaultContent();
                //drill down the frames
                if (!target.contentEquals(Const.PAGE)) {
                    traverse(target, false);
                }
                String style = webElement.getAttribute("style");
                //System.out.println(webElement + style);
                js.executeScript("arguments[0].setAttribute('style', " + Const.HIGHLIGHT + ");", webElement);
                Thread.sleep(500);
                js.executeScript("arguments[0].setAttribute('style', 'arguments[1]');", webElement, style);
            } catch (TimeoutException | StaleElementReferenceException e) {
                ui.rescan();
            } catch (ElementNotVisibleException | InterruptedException e) {
            } catch (WebDriverException e) {
                ui.abort();
            }
        }
    }

    String[] elementFind(WebElement webElement, String target, String action) {
        target = target.replace(Const.INVISIBLE, "");
        driver.switchTo().defaultContent();
        //see if we can elementFind it first
        if (!target.contentEquals(Const.PAGE)) {
            traverse(target, false);
        }
        String name = webElement.getText();
        String method[] = elementFinder(webElement);
        driver.switchTo().defaultContent();
        if (method[0] != null) {
            //we can elementFind it, so write code to navigate
            if (!target.contentEquals(Const.PAGE)) {
                //navigate to the webElement
                traverse(target, true);
            }

            if (!action.equals(Const.CLICK)) {
                //write code to get the element
                writer.writeFindEvent(method[0], method[1]);
            }

            //if we're just doing a elementFind, tidy up
            if (action.equals(Const.FIND)) {
                if (!target.contentEquals(Const.PAGE)) {
                    //if we had to navigate here, switch back
                    writer.writeSwitchBack();
                }
            }
        } else {
            //failed to elementFind the webElement

            ui.errorMessage("Unable to uniquely identify " + name);
            writer.comment("Unable to uniquely identify " + name);
        }
        return method;
    }

    void find(WebElement webElement, String target, String action) {
        writer.writeStart();
        elementFind(webElement, target, action);
        writer.writeEnd();
    }

    void click(WebElement webElement, String target) {
        if (!target.endsWith(Const.INVISIBLE)) {
            writer.writeStart();
            String method[] = elementFind(webElement, target, Const.CLICK);
            if (method[0] != null) {
                writer.writeClickEvent(method[0], method[1]);
                webElement.click();

                //if that caused an alert to popup, deal with it
                //prompting alerts are not dealt with yet, but are supported in webdriver
                //does anyone really use them?
                try {
                    Alert alert = driver.switchTo().alert();
                    if (alert != null) {
                        String title = alert.getText();
                        Boolean action = ui.alertResponse(title);
                        if (action == false) {
                            alert.dismiss();
                        } else {
                            alert.accept();
                        }
                        writer.writeAlertClick(title, action);
                        ui.rescan();
                    }
                } catch (NoAlertPresentException na) {
                    //System.out.println("no alert");
                }

                if (!target.contentEquals(Const.PAGE)) {
                    //if we had to navigate here, switch back
                    writer.writeSwitchBack();
                }
                driver.switchTo().defaultContent();
            }
            writer.writeEnd();
        }
    }

    void input(WebElement webElement, String target) {
        String data = ui.enterValue(webElement.getAttribute("name"));
        if (data != null) {
            writer.writeStart();
            String method[] = elementFind(webElement, target, "");
            if (method[0] != null) {
                writer.writeInputEvent(data);
                if (!target.contentEquals(Const.PAGE)) {
                    //if we had to navigate here, switch back
                    writer.writeSwitchBack();
                }
                webElement.clear();
                webElement.sendKeys(data);
                driver.switchTo().defaultContent();
            }
            writer.writeEnd();
        }
    }

    void inputjs(WebElement webElement, String target) {
        String data = ui.enterValue(webElement.getAttribute("name"));
        if (data != null) {
            writer.writeStart();
            String method[] = elementFind(webElement, target, "click");
            if (method[0] != null) {

                //this only works if the first match is the right element
                String jsInput = "javascript:var e=document.getElementsByName(\"" + method[1] + "\");e[0].value=\"" + data + "\";";

                if (method[0].contentEquals("id")) {
                    jsInput = "javascript:document.getElementById(\"" + method[1] + "\").value=\"" + data + "\";";
                }
                if (method[0].contentEquals("className")) {
                    jsInput = jsInput.replace("getElementsByName", "getElementsByClassName");
                }
                writer.writeInputjsEvent(jsInput.replace("\"", "\\\""));
                if (!target.contentEquals(Const.PAGE)) {
                    //if we had to navigate here, switch back
                    writer.writeSwitchBack();
                }
                js.executeScript(jsInput);
                driver.switchTo().defaultContent();
            }
            writer.writeEnd();
        }
    }

    void select(WebElement webElement, String target) {
        //extract the choices
        Select selector = new Select(webElement);
        List<WebElement> selectOptions = selector.getOptions();
        List<String> items = new ArrayList<>();
        for (WebElement option : selectOptions) {
            items.add(option.getText());
        }
        String[] choices = items.toArray(new String[0]);
        //make a choice
        String data = ui.enterSelection(webElement.getAttribute("name"), choices);
        if (data != null) {
            writer.writeStart();
            String method[] = elementFind(webElement, target, "");
            if (method[0] != null) {
                writer.writeSelectEvent(data);
                if (!target.contentEquals(Const.PAGE)) {
                    //if we had to navigate here, switch back
                    writer.writeSwitchBack();
                }
                selector.selectByVisibleText(data);
                driver.switchTo().defaultContent();
                writer.writeEnd();
            }
        }

    }

    void verify(WebElement webElement, String target, String element, String expected) {
        target = target.replace(Const.INVISIBLE, "");
        writer.writeStart();
        String method[] = elementFind(webElement, target, "");
        if (method[0] != null) {
            String verifymethod;
            if (element.contains("link")) {
                verifymethod = "href";
            } else if (element.contains("image")) {
                verifymethod = "src";
            } else if (element.contains("anchor")) {
                verifymethod = "name";
            } else if (element.contains("radio") || element.contains("checkbox")) {
                verifymethod = "checked";
            } else {
                verifymethod = "value";
            }

            //System.out.println("expected:" + expected);
            //System.out.println("actual:" + webElement.getAttribute(verifymethod));
            //System.out.println("using:" + verifymethod);
            writer.writeVerifyElement(expected, verifymethod);

            if (!target.contentEquals(Const.PAGE)) {
                //if we had to navigate here, switch back
                writer.writeSwitchBack();
            }
        }
        writer.writeEnd();
    }

    void verifyPage(String expected) {
        writer.writeStart();
        writer.writeVerifyPage(expected);
        writer.writeEnd();
    }

    void ident(WebElement webElement, String target, String element, String name, String id, String value) {
        driver.switchTo().defaultContent();
        if (!target.contentEquals(Const.PAGE)) {
            traverse(target, false);
        }

        String method[] = elementFinder(webElement);

        if (method[0] == null) {
            method[0] = "not found!";
        } else {
            method[0] = "By." + method[0];
        }

        String message = "<html><b>Element:</b> " + element
                + "<br><b>Name:</b> " + name
                + "<br><b>ID:</b> " + id
                + "<br><b>Value:</b> " + value
                + "<br><b>Location:</b> " + target
                + "<br><b>Find Method:</b> " + method[0]
                + "<br><b>Outer HTML:</b>"
                + "<br></html>" + webElement.getAttribute("outerHTML");

        try {
            String style = webElement.getAttribute("style");
            //System.out.println(webElement + style);
            js.executeScript("arguments[0].setAttribute('style', " + Const.HIGHLIGHT + ");", webElement);
            //highlight until the message box is cleared
            ui.elementIdent(message);

            js.executeScript("arguments[0].setAttribute('style', 'arguments[1]');", webElement, style);
            driver.switchTo().defaultContent();
        } catch (TimeoutException | StaleElementReferenceException e) {
            ui.rescan();
        } catch (ElementNotVisibleException e) {
        } catch (WebDriverException e) {
            ui.abort();
        }

    }

    void switchWin(String handle, String title) {
        try {
            writer.writeStart();
            writer.writeSwitchWin(title);
            writer.writeEnd();
            driver.switchTo().window(handle);
        } catch (TimeoutException | StaleElementReferenceException | ElementNotVisibleException e) {
            //do nothing, we're rescanning anyway
        } catch (WebDriverException e) {
            ui.abort();
        }
        ui.rescan();
    }

    private String[] elementFinder(WebElement webElement) {
        //uniquely elementFind the webElement by the most descriptive attribute
        //
        //By.linkText
        //By.name
        //By.id
        //By.cssSelector - alt
        //By.cssSelector - href
        //By.cssSelector - src
        //By.cssSelector - title
        //By.cssSelector - value
        //By.className
        //By.cssSelector - class
        //By.cssSelector - type
        List<WebElement> elements;
        String selector;
        String[] result = {null, null};

        //linktext
        if (webElement.getText() != null) {
            elements = driver.findElements(By.linkText(webElement.getText()));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found linktext:" + elements.get(0).getText());
                result[0] = "linkText";
                result[1] = elements.get(0).getText();
                return result;
            }
        }

        //name
        elements = driver.findElements(By.name(webElement.getAttribute("name")));
        if ((elements.size() == 1) && elements.contains(webElement)) {
            //System.out.println("found frames:" + elements.get(0).getAttribute("frames"));
            result[0] = "name";
            result[1] = elements.get(0).getAttribute("name");
            return result;
        }

        //id
        if (!webElement.getAttribute("id").isEmpty()) {  //prevent FF crash
            elements = driver.findElements(By.id(webElement.getAttribute("id")));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found id:" + elements.get(0).getAttribute("id"));
                result[0] = "id";
                result[1] = elements.get(0).getAttribute("id");
                return result;
            }
        }

        //alt
        try {
            selector = "[alt='" + webElement.getAttribute("alt") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[alt='" + elements.get(0).getAttribute("alt") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        String root = js.executeScript("return document.domain;").toString();

        //href
        try {
            selector = "[href='" + webElement.getAttribute("href") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[href='" + elements.get(0).getAttribute("href") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //href, no path
        try {
            selector = "[href='" + webElement.getAttribute("href").replaceAll("http.*" + root + "/", "") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[href='" + elements.get(0).getAttribute("href").replaceAll("http.*" + root + "/", "") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //href, relative path
        try {
            selector = "[href='" + webElement.getAttribute("href").replaceAll("http.*" + root, "") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[href='" + elements.get(0).getAttribute("href").replaceAll("http.*" + root, "") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //src
        try {
            selector = "[src='" + webElement.getAttribute("src") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[src='" + elements.get(0).getAttribute("src") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //src, no path
        try {
            selector = "[src='" + webElement.getAttribute("src").replaceAll("http.*" + root + "/", "") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[src='" + elements.get(0).getAttribute("src").replaceAll("http.*" + root + "/", "") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //src, relative path
        try {
            selector = "[src='" + webElement.getAttribute("src").replaceAll("http.*" + root, "") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[src='" + elements.get(0).getAttribute("src").replaceAll("http.*" + root, "") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //title
        try {
            selector = "[title='" + webElement.getAttribute("title") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[title='" + elements.get(0).getAttribute("title") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //value
        try {
            selector = "[value='" + webElement.getAttribute("value") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[value='" + elements.get(0).getAttribute("value") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //classname
        try {
            elements = driver.findElements(By.className(webElement.getAttribute("className")));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found className:" + elements.get(0).getAttribute("className"));
                result[0] = "className";
                result[1] = elements.get(0).getAttribute("className");
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        try {
            selector = "." + webElement.getAttribute("className").replace(" ", ".");
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = elements.get(0).getAttribute("className").replace(" ", ".");
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //class
        try {
            selector = "[class='" + webElement.getAttribute("class") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[class='" + elements.get(0).getAttribute("class") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //type
        try {
            selector = "[type='" + webElement.getAttribute("type") + "']";
            elements = driver.findElements(By.cssSelector(selector));
            if ((elements.size() == 1) && elements.contains(webElement)) {
                //System.out.println("found cssSelector:" + selector);
                result[0] = "cssSelector";
                result[1] = "[type='" + elements.get(0).getAttribute("type") + "']";
                return result;
            }
        } catch (InvalidSelectorException | NullPointerException e) {
        }

        //implement other methods?
        return result;
    }

}
