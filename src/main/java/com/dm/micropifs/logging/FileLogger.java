package com.dm.micropifs.logging;

import com.dm.micropifs.MicroController;

import java.io.IOException;
import java.util.logging.*;

public class FileLogger {

    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;

    static public Logger getLogger() {

        // get the global logger to configure it
        Logger logger = Logger.getLogger(MicroController.class.getName());

        logger.setLevel(Level.INFO);

        try {
            fileTxt = new FileHandler("G:/Logging.txt");
            fileHTML = new FileHandler("G:/Logging.html");

        } catch (Exception e){}

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        // create an HTML formatter
        formatterHTML = new HTMLFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);

        return logger;
    }


}
