$package("com.kidscademy.form");

com.kidscademy.form.AudioAssets = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Input control for sample title.
		 * 
		 * @type {js.dom.Control}
		 */
		this._sampleTitleInput = this.getByName("sample-title");

		this._audioPlayer = this.getByClass(com.kidscademy.form.AudioPlayer);
		this._audioPlayer.on("stop", this._onPlayerStop, this);
		this._playAction = this.getByName("play");

		this._sampleInfoView = this.getByCssClass("sample-info");

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this.getByCssClass("sample-file").on("change", this._onSampleUpload, this);
	}

	onCreate(formPage) {
		super.onCreate(formPage);

		const object = formPage.getObject();
		this._audioPlayer.onCreate(formPage);
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
		data.append("atlas-object-id", object.id);
		data.append("file", ev.target._node.files[0]);

		const xhr = new js.net.XHR();
		xhr.on("load", this._update, this);
		xhr.open("POST", "rest/upload-audio-sample");
		xhr.send(data);
	}

	_onCut() {
		const atlasItem = this._formPage.getAtlasItem();
		const start = this._audioPlayer.getSelectionStart();

		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();

		AtlasService.cutAudioSample(atlasItem, start, this._update, this);
	}

	_onMono() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.convertAudioSampleToMono(this._formPage.getAtlasItem(), this._update, this);
	}

	_onNormalize() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.normalizeAudioSample(this._formPage.getAtlasItem(), this._update, this);
	}

	_onTrim() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.trimAudioSampleSilence(this._formPage.getAtlasItem(), this._update, this);
	}

	_onFadeIn() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.fadeInAudioSample(this._formPage.getAtlasItem(), this._update, this);
	}

	_onFadeOut() {
		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.fadeOutAudioSample(this._formPage.getAtlasItem(), this._update, this);
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
		AtlasService.undoAudioSampleProcessing(this._formPage.getAtlasItem(), this._update, this);
	}

	_onClear() {
		if (this._audioPlayer.resetSelection()) {
			return;
		}

		this._audioPlayer.resetObject();
		this._sampleInfoView.resetObject();
		AtlasService.roolbackAudioSampleProcessing(this._formPage.getAtlasItem(), this._update, this);

	}

	_onRemoveAll() {
		const atlasItem = this._formPage.getAtlasItem();
		if (!atlasItem.name) {
			return;
		}
		js.ua.System.confirm("@string/confirm-audio-remove", ok => {
			if (ok) {
				this._fireEvent("input");
				atlasItem.sampleInfo = null;
				AtlasService.removeAudioSample(atlasItem, () => {
					this._audioPlayer.resetObject(false);
					this._sampleInfoView.resetObject();
				});
			}
		});
	}

	// --------------------------------------------------------------------------------------------

	_update(info) {
		this._fireEvent("input");
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
		return "com.kidscademy.form.AudioAssets";
	}
};
