WinMain.on("load", () => {
	const taxonUnits = WinMain.doc.getByCssClass("taxon-units");
	taxonUnits.load();
});
