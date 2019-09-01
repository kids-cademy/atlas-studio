package com.kidscademy.atlas.studio.tool;

import java.net.URL;

import com.kidscademy.atlas.studio.model.InstrumentCategory;
import com.kidscademy.atlas.studio.model.HDate;

public class Descriptor {
    public String name;
    public int rank;
    public InstrumentCategory category;
    public HDate date;
    public Country[] spreading;
    public String[] related;
    public ExternalSource[] sources;
    public URL wikipediaArticle;
}
