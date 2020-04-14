WinMain.on("load", function() {
	var graphicAssets = WinMain.doc.getByClass(com.kidscademy.form.GraphicAssets);

	graphicAssets.onCreate({
		getObject : function() {
			return {
				name : "test",
				pictureSrc : "/media/form/instrument/test/picture.jpg",
				iconSrc : "/media/form/instrument/test/icon.jpg",
				thumbnailSrc : "/media/form/instrument/test/thumbnail.png"
			};
		}
	});

	graphicAssets.onStart();
});