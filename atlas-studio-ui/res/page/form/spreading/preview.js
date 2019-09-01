WinMain.on("load", function() {
	var spreadingControl = WinMain.doc.getByClass(com.kidscademy.atlas.SpreadingControl);
	spreadingControl.setValue([ {
		id : 11,
		name : "Switzerland",
		area : "ALL"
	} ]);
});
