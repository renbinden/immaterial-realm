package io.github.alyphen.immaterial_realm.common.log;

import io.github.alyphen.immaterial_realm.common.ImmaterialRealm;

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
import static java.util.logging.Level.SEVERE;

public class FileWriterHandler extends Handler {

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
                writer = new FileWriter(new File("./logs/" + dateFormat.format(new Date()) + ".log"));
            } catch (IOException exception) {
                ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to open log file for writing", exception);
            }
        }
        try {
            writer.write("[" + timeFormat.format(new Date(record.getMillis())) + "] (" + record.getLevel() + ") " + record.getMessage());
        } catch (IOException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to write line to log file", exception);
        }
        lastLogDate = new Date(record.getMillis());
        flush();
    }

    @Override
    public void flush() {
        if (writer != null) try {
            writer.flush();
        } catch (IOException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to flush file writer", exception);
        }
    }

    @Override
    public void close() throws SecurityException {
        try {
            writer.close();
        } catch (IOException exception) {
            ImmaterialRealm.getInstance().getLogger().log(SEVERE, "Failed to close file writer", exception);
        }
    }

}
