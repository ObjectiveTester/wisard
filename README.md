wisard
======

Web Internal Structure Action RecorDer - a webdriver based tool for creating Selenium tests

Building wisard
===============
Open the 'wisard' folder in NetBeans, right click on the project and select 'Properties' -> 'Libraries' and configure the path to the 'selenium-server-standalone' JAR from http://docs.seleniumhq.org/download/

Then right click on the project and 'Build'. The project includes a custom 'manifest.mf' with a hardwired 'Class-Path' to the 'selenium-server-standalone' JAR - you'll have to update this as well if you use anything other than 2.53.0.

Running wisard
==============



Copy 'Wisard.jar' into the same directory as 'selenium-server-standalone-2.53.0.jar' and then either double-click 'Wisard.jar' or start from the command line with:

    java -jar Wisard.jar
    

Then enter a web URL and click 'Inspect'

Using wisard
============
Once a browser has opened and displayed your web page, all the useful elements of the page are displayed in the 'Elements' tab of the Wisard window. Wisard will try to momentarily highlight any element you click on. Right clicking an element allows you to interact with it by clicking it or verifying it's value, etc.

As you set and verify element values, the 'Generated Code' tab is updated with jUnit test cases. Alternately, Java statements can be generated to automate a web task. These can be copied and pasted into a file/your favourite IDE and then compiled and executed.

wisard can set and verify input elements, verify and click links and anchors, and verify image elements.

By correctly setting the 'driver' settings on the 'Settings' tab, wisard can also record scripts in 'Chrome', 'Internet Explorer' and 'Safari' - download the correct drivers from http://docs.seleniumhq.org/download/


Screenshots and more can be found here : http://testingsteve.github.io/wisard
