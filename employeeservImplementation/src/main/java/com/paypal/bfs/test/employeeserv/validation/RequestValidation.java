package com.paypal.bfs.test.employeeserv.validation;

import com.paypal.bfs.test.employeeserv.api.model.Address;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class RequestValidation {

    public static boolean validateFirstName(String firstName) {
        return !StringUtils.isBlank(firstName) && firstName.length() >= 1 && firstName.length() <= 255;
    }

    public static boolean validateLasName(String lastName) {
        return !StringUtils.isBlank(lastName) && lastName.length() >= 1 && lastName.length() <= 255;
    }

    public static boolean validateAddress(Address address) {
        return !StringUtils.isBlank(address.getLine1()) && !StringUtils.isBlank(address.getCity()) && !StringUtils.isBlank(address.getState())
                && !StringUtils.isBlank(address.getCountry()) && !StringUtils.isBlank(address.getZipCode());
    }

    public static boolean validateJavaDate(String strDate) {
        /* Check if date is 'null' */
        if (strDate.trim().equals("")) {
            return false;
        }
        /* Date is not 'null' */
        else {
            /*
             * Set preferred date format,
             * For example dd-MM-yyyy etc.*/
            SimpleDateFormat sdfrmt = new SimpleDateFormat("dd-MM-yyyy");
            sdfrmt.setLenient(false);
            /* Create Date object
             * parse the string into date
             */
            try {
                Date javaDate = sdfrmt.parse(strDate);
                log.info(strDate + " is valid date format1");
                return matchesDatePattern(strDate);
            } catch (ParseException e) {
                log.info(strDate + " is Invalid Date format");
                return false;
            }
        }
    }


    private static boolean matchesDatePattern(String dateString) {
        return dateString.matches("^\\d+\\-\\d+\\-\\d+");
    }

}
