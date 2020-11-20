@rmdir /S /Q binding
@"%JAVA_HOME%\bin\xjc" -d ..\..\..\..\..\ -p com.nadrasoft.passman.presistance.xml.binding Main.xsd
@pause