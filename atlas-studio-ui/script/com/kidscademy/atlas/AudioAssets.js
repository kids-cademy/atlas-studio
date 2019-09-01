$package("com.kidscademy.atlas");

com.kidscademy.atlas.AudioAssets = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Parent form page.
		 * 
		 * @type {com.kidscademy.atlas.FormPage}
		 */
		this._formPage = null;

		/**
		 * Input control for sample title.
		 * 
		 * @type {js.dom.Control}
		 */
		this._sampleTitleInput = this.getByName("sample-title");

		this._audioPlayer = this.getByClass(com.kidscademy.atlas.AudioPlayer);
		this._audioPlayer.on("stop", this._onPlayerStop, this);
		this._playAction = this.getByName("play");

		this._sampleInfoView = this.getByCssClass("sample-info");

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this.getByCssClass("sample-file").on("change", this._onSampleUpload, this);
	}

	onCreate(formPage) {
		this._formPage = formPage;
		this._audioPlayer.onCreate(formPage);
	}

	onStart() {
		const object = this._formPage.getObject();
		this._audioPlayer.setObject(object);
		this._sampleInfoView.setObject(object.sampleInfo);
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onUpload() { }

	_onSampleUpload(ev) {
		this._audioPlayer.resetObject();
		const object = this._formPage.getObject();
		if (!object.name) {
			js.ua.System.alert("Missing object name.");
			return;
		}
		this._sampleInfoView.resetObject();

		const data = new FormData();
		data.append("dtype", object.dtype);
		data.append("name", object.name);
		data.append("file", ev.target._node.files[0]);

		const xhr = new js.net.XHR();
		xhr.on("load", this._update, this);
		xhr.open("POST", "rest/upload-audio-sample");
		xhr.send(data);
	}

	_onCut() {
		const object = this._formPage.getUIObject();
		const start = this._audioPlayer.getSelectionStart();

		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();

		AtlasService.cutAudioSample(object, start, this._update, this);
	}

	_onMono() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.convertAudioSampleToMono(this._formPage.getUIObject(), this._update, this);
	}

	_onNormalize() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.normalizeAudioSample(this._formPage.getUIObject(), this._update, this);
	}

	_onTrim() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.trimAudioSampleSilence(this._formPage.getUIObject(), this._update, this);
	}

	_onFadeIn() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.fadeInAudioSample(this._formPage.getUIObject(), this._update, this);
	}

	_onFadeOut() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.fadeOutAudioSample(this._formPage.getUIObject(), this._update, this);
	}

	_onPlay() {
		if (!this._audioPlayer.isPlaying()) {
			this._audioPlayer.play();
			this._playAction.setSrc("@image/action/stop");
		}
		else {
			this._audioPlayer.stop();
			this._playAction.setSrc("@image/action/play");
		}
	}

	_onUndo() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.undoAudioSampleProcessing(this._formPage.getUIObject(), this._update, this);
	}

	_onClear() {
		if (this._audioPlayer.resetSelection()) {
			return;
		}

		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.roolbackAudioSampleProcessing(this._formPage.getUIObject(), this._update, this);

	}

	_onRemove() {
		const object = this._formPage.getUIObject();
		if (!object.name) {
			return;
		}
		object.sampleInfo = null;
		AtlasService.removeAudioSample(object, () => {
			this._audioPlayer.resetObject(false);
			this._sampleInfoView.resetObject();
		});
	}

	// --------------------------------------------------------------------------------------------

	_update(info) {
		this._audioPlayer.setObject(info);
		this._sampleInfoView.setObject(info);
	}

	_onPlayerStop() {
		this._playAction.setSrc("@image/action/play");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.AudioAssets";
	}
};
