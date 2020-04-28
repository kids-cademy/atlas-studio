$package("com.kidscademy.util");

com.kidscademy.util.Strings = {
	/**
	 * Convert plain text into HTML formatted text. Current implementation just uses line break as separator for
	 * paragraphs. Also paragraphs are trimmed both sides.
	 * 
	 * @param String plain text.
	 * @return String HTML formatted text.
	 */
	text2html: function (text) {
		var html = "";
		text.split(/\n/g).forEach(function (paragraph) {
			paragraph = paragraph.trim();
			if (paragraph) {
				html += ("<p>" + paragraph + "</p>");
			}
		});
		return html;
	},

	BASEDOMAIN_REX: /^(?:http|https|ftp|file):\/\/(?:[^.]+\.)*([^.]+\.[^:/]+).*$/,

	basedomain(url) {
		$assert(url, "com.kidscademy.util.Strings#basedomain", "Null or undefined URL argument.");
		var match = url.match(this.BASEDOMAIN_REX);
		return match != null ? match[1] : null;
	}
};

Strings = com.kidscademy.util.Strings;
