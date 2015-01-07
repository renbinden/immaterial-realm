package io.github.alyphen.immaterial_realm.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;

class FileWriterHandler extends Handler {

    private FileWriter writer;
    private Date lastLogDate;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) return;
        Calendar now = null;
        Calendar lastLogCalendar = null;
        if (lastLogDate != null) {
            now = Calendar.getInstance();
            now.setTimeInMillis(record.getMillis());
            lastLogCalendar = Calendar.getInstance();
            lastLogCalendar.setTime(lastLogDate);
        }
        if (lastLogDate == null || now.get(DAY_OF_YEAR) == lastLogCalendar.get(DAY_OF_YEAR) && now.get(YEAR) == lastLogCalendar.get(YEAR)) {
            try {
                writer = new FileWriter(new File("./" + dateFormat.format(new Date()) + ".log"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try {
            writer.write("[" + timeFormat.format(new Date(record.getMillis())) + "] (" + record.getLevel() + ") " + record.getMessage());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        lastLogDate = new Date(record.getMillis());
        flush();
    }

    @Override
    public void flush() {
        if (writer != null) try {
            writer.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void close() throws SecurityException {
        try {
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
