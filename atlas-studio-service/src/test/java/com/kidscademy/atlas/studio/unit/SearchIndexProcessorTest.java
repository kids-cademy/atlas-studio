package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
import com.kidscademy.atlas.studio.search.DirectIndex;
import com.kidscademy.atlas.studio.search.KeywordIndex;
import com.kidscademy.atlas.studio.search.SearchIndexProcessor;

@RunWith(MockitoJUnitRunner.class)
public class SearchIndexProcessorTest {
    @Mock
    private ExportObject object0;
    @Mock
    private ExportObject object1;
    @Mock
    private ExportObject object2;

    private SearchIndexProcessor processor;

    private List<ExportItem> items = new ArrayList<>();

    @Before
    public void beforeTest() throws NoSuchMethodException {
	items.add(item(0, "accordion"));
	items.add(item(1, "bandoneon"));
	items.add(item(2, "melodica"));

	processor = new SearchIndexProcessor();
    }

    @Test
    public void createDirectIndex() throws IOException {
	when(object0.getIndex()).thenReturn(1);
	when(object0.getDisplay()).thenReturn("Accordion");

	DirectIndex<Integer> index = processor.createDirectIndex(object0);

	assertThat(index, notNullValue());
	assertThat(index.size(), equalTo(1));
    }

    @Test
    public void updateSearchIndex() throws IOException {
	when(object0.getIndex()).thenReturn(1);
	when(object0.getDisplay()).thenReturn("Accordion");

	when(object1.getIndex()).thenReturn(2);
	when(object1.getDisplay()).thenReturn("Bandoneon");
	when(object1.getDescription()).thenReturn(Arrays.asList("Bandoneon is related to accordion."));

	when(object2.getIndex()).thenReturn(3);
	when(object2.getDisplay()).thenReturn("Melodica");
	when(object2.getAliases()).thenReturn(Arrays.asList("accordion", "bandoneon"));

	processor.createDirectIndex(object0);
	processor.createDirectIndex(object1);
	processor.createDirectIndex(object2);

	List<KeywordIndex<Integer>> index = processor.updateSearchIndex();

	assertThat(index, notNullValue());
	assertThat(index, hasSize(4));
    }

    private static ExportItem item(int index, String name) {
	ExportItem item = new ExportItem();
	item.setIndex(index);
	item.setName(name);
	return item;
    }
}
