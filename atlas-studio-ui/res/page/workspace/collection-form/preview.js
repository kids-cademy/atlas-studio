WinMain.on("load", () => {
	const collection = {
		id: 3,
		name: "wild-birds",
		display: "Wild Birds",
		definition: "Wild birds from all ecosystems. This collection does not include domesticated birds.",
		iconName: "wild-birds.png",
		iconSrc: "/media/collection/wild-birds.png",
		flags: {
			audioSample: true,
			conservationStatus: true,
			endDate: true		
		},
		taxonomyMeta: [
			{ name: "kingdom", values: "" },
			{ name: "phylum", values: "" },
			{ name: "class", values: "" },
			{ name: "order", values: "" },
			{ name: "family", values: "" },
			{ name: "genus", values: "" },
			{ name: "species", values: "" }
		]	​​​​
	};
	
	const collectionForm = WinMain.doc.getByClass(com.kidscademy.workspace.CollectionForm);
	collectionForm.setCollection(collection);
	
});