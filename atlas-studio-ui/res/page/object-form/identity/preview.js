WinMain.on("load", function() {
	const form = WinMain.doc.getByTag("form");
	form.setObject({
		rank : 1234,
		name : "test",
		display : "Test Instrument",
		aliases : [ "Sun-Fin-Chin", "Bayan", "Trekspill", "Fisarmonica" ],
		category : "PERCUSSION",
		date : {
			value : 1964,
			mask : 1
		}
	})
});
