package com.fiftyfour.persistence

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat

class FileTopicMetricStore implements TopicMetricStore {

    FileTopicMetricStore() {
        def rootDir = new File('db');

        if (!rootDir.exists()) {
            try {
                rootDir.mkdir();
            }
            catch (SecurityException e) {
                println "Got Exception while creating database"
                e.printStackTrace();
            }
        }
    }

    @Override
    void Save(String topicName, TopicMetric metric) {

        def file = getFileFor(topicName);
        def writer = getOutputStream(file);

        try {
            def metricBytes = serializeMetric(metric);
            writer.write(metricBytes);
        }
        finally {
            writer.close();
        }
    }

    @Override
    List<TopicMetric> GetRange(String topicName, DateTime start, DateTime end) {
        List<TopicMetric> metrics = []
        getFileFor(topicName).eachLine { line ->
            def metric = deserializeMetric(line)
            if (isInRange(metric.timeStamp, start, end)) {
                metrics.push(metric)
            }
        }
        return metrics
    }

    private boolean isInRange(DateTime instance, DateTime start, DateTime end) {
        return (instance.isEqual(start)
               || instance.isEqual(end)
               || (instance.isAfter(start)
                   && instance.isBefore(end)))
    }

    private File getFileFor(String fileName) {
        return new File("db/${fileName}.txt");
    }

    private OutputStream getOutputStream(File file) {
        def fileOutput = new FileOutputStream(file, true);
        def bufferSize = 512;
        return new BufferedOutputStream(fileOutput, bufferSize);
    }

    private static DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();

    private byte[] serializeMetric(TopicMetric metric) {
        def metricString = dateTimeFormatter.print(metric.timeStamp);
        metricString += "," + getFixedWidth(metric.messageIn)
        metricString += "," + getFixedWidth(metric.messageOut)
        metricString += "\n"
        return metricString.bytes;
    }

    private TopicMetric deserializeMetric(String line) {
        def (timeStampStr, msgInStr, msgOutStr) = line.tokenize(',');
        return new TopicMetric(timeStamp: dateTimeFormatter.parseDateTime(timeStampStr),
                               messageIn: Long.parseLong(msgInStr, 16),
                               messageOut: Long.parseLong(msgOutStr, 16));
    }

    private String getFixedWidth(int value) {
        return String.format("%08X", value);
    }
}
