package com.kidscademy.atlas.studio.it;

import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.dao.AtlasDaoImpl;
import com.kidscademy.atlas.studio.export.AtlasCollectionExportView;
import com.kidscademy.atlas.studio.model.AtlasObject;

import js.tiny.container.mvc.View;
import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;

@RunWith(MockitoJUnitRunner.class)
public class ExportTest
{
  @Mock
  private HttpServletResponse httpResponse;

  private final AtlasDao atlasDao;

  public ExportTest() {
    TransactionFactory factory = new TransactionFactoryImpl("import");
    this.atlasDao = factory.newInstance(AtlasDaoImpl.class);
  }

  @Test
  public void exportAtlasCollection() throws IOException {
    when(httpResponse.getOutputStream()).thenReturn(new HttpResponseStream("fixture/atlas.zip"));

    View export = new AtlasCollectionExportView(atlasDao, 2, AtlasObject.State.PUBLISHED, Arrays.asList("EN"));
    export.serialize(httpResponse);
  }

  private static class HttpResponseStream extends ServletOutputStream
  {
    private OutputStream stream;

    public HttpResponseStream(String path) throws FileNotFoundException {
      this.stream = new FileOutputStream(path);
    }

    @Override
    public boolean isReady() {
      return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
      stream.write(b);
    }
  }
}
