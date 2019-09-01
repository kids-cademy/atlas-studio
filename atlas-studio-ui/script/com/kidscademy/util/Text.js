$package("com.kidscademy.util");

com.kidscademy.util.Text = {
	/**
	 * Convert plain text into HTML formatted text. Current implementation just uses line break as separator for
	 * paragraphs. Also paragraphs are trimmed both sides.
	 * 
	 * @param String plain text.
	 * @return String HTML formatted text.
	 */
	text2html : function(text) {
		var html = "";
		text.split(/\n/g).forEach(function(paragraph) {
			paragraph = paragraph.trim();
			if (paragraph) {
				html += ("<p>" + paragraph + "</p>");
			}
		});
		return html;
	}
};