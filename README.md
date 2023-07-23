Wisard
======

Web Internal Structure Action RecorDer - a webdriver based tool for creating Selenium tests

Building Wisard
===============

Clone or download the source code and either:

Open the 'wisard' folder in NetBeans or your favourite IDE, and build it.

Build a package from the command line with:

    mvn package 


Running Wisard
==============

Once built, either double-click 'Wisard-1.6.jar' or start from the command line with:

    java -jar target/Wisard-1.6.jar
    

Then enter a web URL and click the find icon

Using Wisard
============
Once a browser has opened and displayed your web page, all the useful elements of the page are displayed in left hand pane of the Wisard window. Wisard will try to momentarily highlight any element you click on. Right clicking an element allows you to interact with it by clicking it or verifying it's value, etc.

As you set and verify element values, the right hand pane is updated with jUnit test cases. Alternately, Java statements can be generated to automate a web task. These can be copied and pasted into a file/your favourite IDE and then compiled and executed.

Wisard can set and verify input elements, verify and click links and anchors, and verify image elements.

Wisard can open and record scripts in 'Firefox', 'Chrome', 'Internet Explorer', 'Edge' and 'Safari', drivers are auto-downloaded by Selenium Manager, or can be manually overridden by setting the 'driver' path setting in the 'Settings' window, and  downloading the correct drivers for your OS.


Screenshots and more can be found here : http://objectivetester.github.io/wisard
