WinMain.on("load", function() {
	var audioAssets = WinMain.doc.getByClass(com.kidscademy.form.AudioAssets);

	const formPage = {
		getObject : function() {
			return {
				name : "test",
				sampleTitle : "Sample Title",
				sampleSrc : "/media/form/instrument/test/sample.mp3",
				waveformSrc : "/media/form/instrument/test/waveform.png"
			};
		}
	};
	audioAssets.onCreate(formPage);

	const sampleInfo = {
		codec : "MP3 (MPEG audio layer 3)",
		fileSize : 468980,
		duration : 30017,
		channels : 2,
		sampleRate : 44100,
		bitRate : 128000
	};

	audioAssets.onStart();
});