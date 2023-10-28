package ru.otus.java.pro.serialization.serializer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import ru.otus.java.pro.serialization.csvstrategy.CsvParsingStrategy;
import ru.otus.java.pro.serialization.serializer.exception.DeserializeException;
import ru.otus.java.pro.serialization.serializer.exception.SerializeException;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

@RequiredArgsConstructor
public class CsvSerializer implements Serializer {
    private final CsvParsingStrategy strategy;

    @Override
    public void serialize(String filePath, Object object) {
        try (var writer = new FileWriter(filePath);
             var csvWriter = new CSVWriter(writer)) {
            String[] headers = strategy.getHeaders();
            csvWriter.writeNext(headers);
            List<String[]> values = strategy.getValues();
            values.forEach(it -> csvWriter.writeNext(it));
        } catch (Exception e) {
            throw new SerializeException(e);
        }
    }

    @Override
    public <T> T deserialize(String filePath, Class<T> clazz) {
        try (var reader = new FileReader(filePath);
             var csvReader = new CSVReader(reader)) {
            List<String[]> strings = csvReader.readAll();
            strings.remove(0);
            return (T) strategy.createObject(strings);
        } catch (Exception e) {
            throw new DeserializeException(e);
        }
    }

    @Override
    public FileFormat getFileFormat() {
        return FileFormat.CSV;
    }
}
