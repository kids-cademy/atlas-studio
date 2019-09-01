package com.kidscademy.atlas.studio.www;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface Wikipedia {
    @Path("page/summary/{title}")
    WikipediaPageSummary getPageSummary(@PathParam("title") String title);
}
