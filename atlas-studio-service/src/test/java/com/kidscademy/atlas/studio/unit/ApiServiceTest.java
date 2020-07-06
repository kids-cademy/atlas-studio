package com.kidscademy.atlas.studio.unit;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.ApiService;
import com.kidscademy.atlas.studio.impl.ApiServiceImpl;
import com.kidscademy.atlas.studio.model.ApiDescriptor;

import js.tiny.container.core.AppContext;

@RunWith(MockitoJUnitRunner.class)
public class ApiServiceTest {
    @Mock
    private AppContext context;

    private ApiService service;

    @Before
    public void beforeTest() {
	service = new ApiServiceImpl(context);
    }

    @Test
    public void getAvailableApis() {
	List<ApiDescriptor> apis = service.getAvailableApis();
	assertThat(apis, notNullValue());
	assertThat(apis, hasSize(5));
	
	assertThat(apis.get(0).getName(), equalTo("definition"));
	assertThat(apis.get(1).getName(), equalTo("description"));
	assertThat(apis.get(2).getName(), equalTo("facts"));
	assertThat(apis.get(3).getName(), equalTo("features"));
	assertThat(apis.get(4).getName(), equalTo("taxonomy"));

	assertThat(apis.get(0).getDescription(), startsWith("Definition is a brief description"));
	assertThat(apis.get(1).getDescription(), startsWith("Description is organized on named sections"));
	assertThat(apis.get(2).getDescription(), startsWith("A fact is a paragraph describing"));
	assertThat(apis.get(3).getDescription(), startsWith("A feature is a named characteristic"));
	assertThat(apis.get(4).getDescription(), startsWith("Object classification."));
    }
}
