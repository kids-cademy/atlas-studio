package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.export.ExportItem;
import com.kidscademy.atlas.studio.export.ExportObject;
import com.kidscademy.atlas.studio.export.ExportRelatedObject;
import com.kidscademy.atlas.studio.model.AtlasCollection;
import com.kidscademy.atlas.studio.model.AtlasObject;
import com.kidscademy.atlas.studio.model.Fact;
import com.kidscademy.atlas.studio.model.MediaSRC;
import com.kidscademy.atlas.studio.model.Region;
import com.kidscademy.atlas.studio.model.Taxon;
import com.kidscademy.atlas.studio.model.TaxonMeta;
import com.kidscademy.atlas.studio.model.Theme;
import com.kidscademy.atlas.studio.search.DirectIndex;
import com.kidscademy.atlas.studio.search.ObjectFields;
import com.kidscademy.atlas.studio.search.ObjectIndexer;

@RunWith(MockitoJUnitRunner.class)
public class ObjectIndexerTest
{
  @Mock
  private AtlasCollection atlasCollection;
  @Mock
  private AtlasObject atlasObject;
  @Mock
  private ExportItem exportItem;

  private ObjectIndexer<ExportObject, Integer> indexer;

  @Before
  public void beforeTest() throws NoSuchMethodException {
    when(atlasObject.getCollection()).thenReturn(atlasCollection);
    when(atlasCollection.getTheme()).thenReturn(Theme.CLASSIC);

    ObjectFields<ExportObject> fields = new ObjectFields<>(ExportObject.class);
    fields.addField("description"); // 1
    fields.addField("facts"); // 2
    fields.addField("definition"); // 4
    fields.addField("sampleTitle"); // 8
    fields.addField("spreading"); // 16
    fields.addField("conservation"); // 32
    fields.addField("taxonomy"); // 64
    fields.addField("related"); // 128
    fields.addField("aliases"); // 256
    fields.addField("display"); // 512

    indexer = new ObjectIndexer<>(fields);
  }

  @Test
  public void scanObject() throws IOException {
    List<Fact> facts = new ArrayList<>();
    facts.add(new Fact("Used in pop music", "The accordion is used in pop music."));

    when(atlasObject.getName()).thenReturn("accordion");
    when(atlasObject.getWaveformSrc()).thenReturn(new MediaSRC());

    when(atlasObject.getDescription()).thenReturn("<text><p>The accordion is a musical instrument.</p></text>"); // 1
    when(atlasObject.getFacts()).thenReturn(facts); // 2
    when(atlasObject.getDefinition()).thenReturn("A portable wind instrument."); // 4
    when(atlasObject.getSampleTitle()).thenReturn("Vittorio Monti"); // 8
    when(atlasObject.getSpreading()).thenReturn(Arrays.asList(new Region("Canada"))); // 16
    // musical instrument has no conservation status // 32
    when(atlasObject.getTaxonomy()).thenReturn(Arrays.asList(new Taxon(new TaxonMeta("family"), "KEYBOARD"))); // 64
    when(exportItem.getDisplay()).thenReturn("Concertina"); // 128
    when(atlasObject.getAliases()).thenReturn(Arrays.asList("Akkordeon", "Fisarmonica")); // 256
    when(atlasObject.getDisplay()).thenReturn("Accordion"); // 512

    ExportObject object = new ExportObject(atlasObject);
    object.addRelated(new ExportRelatedObject(exportItem));

    DirectIndex<Integer> index = indexer.scanObject(object, 1964);
    assertThat(index, notNullValue());
    assertThat(index.getObjectKey(), equalTo(1964));
    assertThat(index.size(), equalTo(16));

    assertThat(index.getRelevance("accordion"), equalTo(512));
    assertThat(index.getRelevance("akkordeon"), equalTo(256));
    assertThat(index.getRelevance("fisarmonica"), equalTo(256));
    assertThat(index.getRelevance("concertina"), equalTo(128));
    assertThat(index.getRelevance("family"), equalTo(64));
    assertThat(index.getRelevance("keyboard"), equalTo(64));
    assertThat(index.getRelevance("canada"), equalTo(16));
    assertThat(index.getRelevance("vittorio"), equalTo(8));
    assertThat(index.getRelevance("monti"), equalTo(8));
    assertThat(index.getRelevance("portable"), equalTo(4));
    assertThat(index.getRelevance("wind"), equalTo(4));
    assertThat(index.getRelevance("instrument"), equalTo(4));
    assertThat(index.getRelevance("used"), equalTo(2));
    assertThat(index.getRelevance("pop"), equalTo(2));
    assertThat(index.getRelevance("music"), equalTo(2));
    assertThat(index.getRelevance("musical"), equalTo(1));
  }

  @Test
  public void scanObject_CompoundKey() throws IOException {
    class Key implements Comparable<Key>
    {
      private int libraryID = 1;
      private int objectID = 1964;

      @Override
      public int compareTo(Key o) {
        return 0;
      }
    }

    when(atlasObject.getName()).thenReturn("accordion");
    when(atlasObject.getWaveformSrc()).thenReturn(new MediaSRC());

    ObjectIndexer<ExportObject, Key> indexer = new ObjectIndexer<>(new ObjectFields<>(ExportObject.class));
    ExportObject object = new ExportObject(atlasObject);
    DirectIndex<Key> index = indexer.scanObject(object, new Key());

    assertThat(index, notNullValue());
    assertThat(index.getObjectKey().libraryID, equalTo(1));
    assertThat(index.getObjectKey().objectID, equalTo(1964));
  }

  @Test(expected = IllegalArgumentException.class)
  public void scanObject_NullObject() throws IOException {
    indexer.scanObject(null, 1964);
  }

  @Test(expected = IllegalArgumentException.class)
  public void scanObject_NullKey() throws IOException {
    ExportObject object = new ExportObject(atlasObject);
    indexer.scanObject(object, null);
  }
}
