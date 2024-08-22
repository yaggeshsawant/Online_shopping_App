package com.madss.grocery;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogFileService {

    public static String changeFormate(String date, String currentFormate,
                                       String reqFormate) {

        try {
            // we may write simple date format on place of dateFormate
            DateFormat srcDf = new SimpleDateFormat(currentFormate);
            // parse the date string into Date object
            Date dat = srcDf.parse(date);
            DateFormat destDf = new SimpleDateFormat(reqFormate);
            // format the date into another format
            date = destDf.format(dat);
            //System.out.println("Converted date is : " + date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
