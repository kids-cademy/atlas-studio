WinMain.on("load", () => {
	const object1 = {
		images : {
			contextual : {
				src : "/media/atlas/aquatic-mammals/balaena-mysticetus/contextual.jpg",
			    caption: "<p>Australian sea lion males are enormously large, reaching a huge size.</p><p>Sometimes they can be three times bigger than females.</p>"
			}
		},
		samplePath: "/media/atlas/aquatic-mammals/balaena-mysticetus/sample.mp3",
		sampleTitle: "Bowhead whale song",
		waveformSrc:"/media/atlas/aquatic-mammals/balaena-mysticetus/waveform.png"
	};

	const object2 = {
		images : {
			contextual : {
				src : "/media/atlas/aquatic-mammals/balaena-mysticetus/contextual.jpg",
			    caption: "<p>Australian sea lion males are enormously large, reaching a huge size.</p><p>Sometimes they can be three times bigger than females.</p>"
			}
		},
		samplePath: null,
		sampleTitle: "Bowhead whale song",
		waveformSrc:"/media/atlas/aquatic-mammals/balaena-mysticetus/waveform.png"
	};

	const object3 = {
		images : {
			contextual : {
				src : "/media/atlas/aquatic-mammals/balaena-mysticetus/contextual.jpg",
			    caption: null
			}
		},
		samplePath: null,
		sampleTitle: "Bowhead whale song",
		waveformSrc:"/media/atlas/aquatic-mammals/balaena-mysticetus/waveform.png"
	};

	var objectView = WinMain.doc.getByCss(".object-view");
	objectView.setObject(object1);
	
	WinMain.doc.getByCssClass("o1").on("click", ev => objectView.setObject(object1));
	WinMain.doc.getByCssClass("o2").on("click", ev => objectView.setObject(object2));
	WinMain.doc.getByCssClass("o3").on("click", ev => objectView.setObject(object3));
});