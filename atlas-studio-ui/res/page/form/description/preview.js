WinMain.on("load", function() {
	const form = WinMain.doc.getByTag("form");
	form.setObject({
		description : "@string/sample-text"
	})
});
