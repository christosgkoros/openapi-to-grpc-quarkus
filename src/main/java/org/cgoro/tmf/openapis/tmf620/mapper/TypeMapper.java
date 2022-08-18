package org.cgoro.tmf.openapis.tmf620.mapper;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class TypeMapper {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public URI map(String uri) {
        try {
            return URI.create(uri);
        } catch (Exception e) {
            logger.warning("URI mapping failed: " + uri);
            return null;
        }
    }

    public Date dateMap(String date) {
        try {
            return new SimpleDateFormat().parse(date);
        } catch (ParseException e) {
            logger.warning("Date mapping failed: " + date);
            return null;
        }
    }
}
