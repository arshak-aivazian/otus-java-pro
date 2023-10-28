package ru.otus.java.pro.serialization.serializer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlSerializer extends AbstractJacksonSerializer {

    public XmlSerializer() {
        super(new XmlMapper());
    }

    @Override
    public FileFormat getFileFormat() {
        return FileFormat.XML;
    }
}
