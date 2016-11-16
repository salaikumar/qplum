package me.salai.practice.qplum.driver;

import me.salai.practice.qplum.dc.DataCollector;

import java.io.IOException;

/**
 * This is the driver class
 * It has the Main method that does the processing for the data part
 */
public class Driver {
    public static void main(String[] args) throws IOException {
        // Create a DataCollector Instance
        DataCollector dc = new DataCollector("/home/salai/works/java/QPlum/stockdata","stockdata.txt");
        dc.watch();
    }
}
