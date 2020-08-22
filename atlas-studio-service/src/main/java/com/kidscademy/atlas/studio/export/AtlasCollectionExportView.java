package com.kidscademy.atlas.studio.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.kidscademy.atlas.studio.dao.AtlasDao;
import com.kidscademy.atlas.studio.model.AtlasObject;

import js.tiny.container.http.ContentType;
import js.tiny.container.mvc.View;

public class AtlasCollectionExportView implements View
{
  private final AtlasDao atlasDao;
  private final List<ExportItem> items;
  private final List<String> languages;

  public AtlasCollectionExportView(AtlasDao atlasDao, List<String> languages) {
    this.atlasDao = atlasDao;
    this.items = atlasDao.getAllExportItems();
    this.languages = languages;
  }

  public AtlasCollectionExportView(AtlasDao atlasDao, int collectionId, AtlasObject.State state, List<String> languages) {
    this.atlasDao = atlasDao;
    if(state == AtlasObject.State.NONE) {
      this.items = atlasDao.getCollectionExportItems(collectionId);
    }
    else {
      this.items = atlasDao.getCollectionExportItemsByState(collectionId, state);
    }
    this.languages = languages;
  }

  @Override
  public View setModel(Object model) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void serialize(HttpServletResponse httpResponse) throws IOException {
    ExportTarget target = new ZipExportTarget();
    Exporter exporter = new Exporter(atlasDao, target, items, languages);

    httpResponse.setContentType(ContentType.TEXT_PLAIN.getValue());
    exporter.serialize(httpResponse.getOutputStream());
  }
}
