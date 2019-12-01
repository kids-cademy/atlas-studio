package com.kidscademy.atlas.studio.tool;

import static com.kidscademy.atlas.studio.tool.AbstractToolProcess.buildCommand;

import java.io.File;
import java.io.IOException;

import js.util.Files;

public class TextConverterImpl implements TextConverter {
    private final ToolProcess pandoc;

    public TextConverterImpl() {
	this.pandoc = new PandocProcess();
    }

    @Override
    public void convertMdToHtm(File mdFile, File htmFile) throws IOException {
	exec("-f markdown_strict -o ${htmFile} ${mdFile}", htmFile, mdFile);
    }

    @Override
    public void convertMdToStandaloneHtm(File mdFile, File htmFile) throws IOException {
	exec("-f markdown_strict -s --metadata title=\"${title}\" -o ${htmFile} ${mdFile}", Files.basename(htmFile),
		htmFile, mdFile);
    }

    // --------------------------------------------------------------------------------------------

    private void exec(String format, Object... args) throws IOException {
	pandoc.exec(buildCommand(format, args));
    }
}
