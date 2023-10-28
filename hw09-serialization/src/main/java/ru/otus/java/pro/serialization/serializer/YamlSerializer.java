package ru.otus.java.pro.serialization.serializer;

import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class YamlSerializer extends AbstractJacksonSerializer {

    public YamlSerializer() {
        super(new YAMLMapper());
    }

    @Override
    public FileFormat getFileFormat() {
        return FileFormat.YAML;
    }
}
