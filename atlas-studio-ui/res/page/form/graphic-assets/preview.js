WinMain.on("load", function() {
	var graphicAssets = WinMain.doc.getByClass(com.kidscademy.atlas.GraphicAssets);

	graphicAssets.onCreate({
		getObject : function() {
			return {
				name : "test",
				pictureSrc : "/media/atlas/instrument/test/picture.jpg",
				iconSrc : "/media/atlas/instrument/test/icon.jpg",
				thumbnailSrc : "/media/atlas/instrument/test/thumbnail.png"
			};
		}
	});

	graphicAssets.onStart();
});