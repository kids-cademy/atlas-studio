WinMain.on("load", () => {
	const text = "<section>\
<h1>heading</h1>\
<table>\
		<tr>\
			<th>column #1</th>\
			<th>column #2</th>\
		</tr>\
		<tr>\
			<td>data #1</td>\
			<td>data #2</td>\
		</tr>\
		<tr>\
			<td>data prime #1</td>\
			<td>data prime #2</td>\
		</tr>\
</table>\
<p>paragraph</p>\
</section>";
	
	const richText = WinMain.doc.getByClass(com.kidscademy.RichTextControl);
	richText.setValue(text);
	
	const preview = WinMain.doc.getByCssClass("preview");
	preview.setValue(richText.getValue());
});
