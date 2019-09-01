$package("com.kidscademy.atlas");

/**
 * Audio player with waveform preview and progress indicator.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.atlas.AudioPlayer = class extends js.dom.Element {
	/**
	 * Construct audio player instance.
	 * 
	 * @param {js.dom.Document} ownerDoc - element owner document,
	 * @param {Node} node - native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Parent form page initialized by {@link #onCreate(com.kidscademy.atlas.FormPage)}.
		 * @type {com.kidscademy.atlas.FormPage}
		 */
		this._formPage = null;

		/**
		 * Hidden input that stores root-relative media SRC for audio sample. It is scanned by parent form for validity and value.
		 * <p>
		 * This input is entangled with waveform graph and its validity state is reflected on waveform displayed state.
		 * @type {com.kidscademy.HiddenControl}
		 */
		this._sampleSrcInput = this.getByName("sample-src");

		/**
		 * Audio media player provided by browser. Current implementation does not use <code>timeupdate</code> Audio event for
		 * progress update because of not satisfactory UI update rate; it uses instead periodic time, see {@link #_timer}.
		 * @type {Audio}
		 */
		this._player = new Audio();

		/**
		 * Progress bar update period, in milliseconds.
		 * @type {number}
		 */
		this._PROGRESS_UPDATE_PERIOD = 20;

		/**
		 * System timer for progress indicator updates. Alternative for Audio event <code>timeupdate</code>.
		 * @type {Object}
		 */
		this._timer = null;

		/**
		 * Image element for audio sample waveform preview.
		 * @type {js.dom.Image}
		 */
		this._waveformImage = this.getByName("waveform-src");
		this._waveformImage.on("error", this._onMissingWaveform, this);

		/**
		 * Cache this class element width to use as waveform witdh reference, in pixels.
		 * @type {number}
		 */
		this._waveformWidth = this.style.getWidth();

		/**
		 * Waveform left position relative to page, in pixels.
		 * @type {number} 
		 */
		this._waveformLeft = this.style.getPageLeft();

		/**
		 * Progress indicator.
		 * @type {js.dom.Element}
		 */
		this._progressIndicator = this.getByCssClass("progress");

		/**
		 * Media player progress offset, default to zero. This value is set by user when click on waveform and is
		 * used to compute media player and progress position, on playing restart. It is a percent from entire sample duration
		 * so its value is in range [0..1).
		 * @type {number}
		 */
		this._progressOffset = 0;

		/**
		 * 
		 */
		this._processing = this.getByCssClass("processing");

		/**
		 * Custom events. Current implementation supports only <code>stop</code> event, triggered when media player is stopped,
		 * either explicitly by invoking {@link #stop()} or on end of stream.
		 * @type {js.event.CustomEvents}
		 */
		this._events = this.getCustomEvents();
		this._events.register("stop");

		this.on("click", this._onClick, this);
	}

	/**
	 * Hook invoked by this class parent when is about to be displayed.
	 * 
	 * @param {com.kidscademy.atlas.FormPage} formPage - parent form page.
	 */
	onCreate(formPage) {
		this._formPage = formPage;
	}

	/**
	 * Initialize instance state from given atlas object. Atlas object should have <code>sampleSrc</code> and <code>waveformSrc</code> properties.
	 * 
	 * @param {Object} object - atlas object. 
	 */
	setObject(object) {
		this._processing.hide();
		this._updatePlayer(object.sampleSrc);
		this._waveformImage.setValue(object.waveformSrc)
	}

	/** 
	 * Reset this instance state. After this method is executed this object is not longer useable. One should call {@link #setObject(Object)} 
	 * to initialize it. 
	 * 
	 * @param {boolean} [processing=true] - display processing indication, optional with default to true.
	 */
	resetObject(processing = true) {
		this._progressOffset = 0;
		this._updatePlayer(null);
		this._player.src = 'data:audio/wav;base64,UklGRiQAAABXQVZFZm10IBAAAAABAAEAVFYAAFRWAAABAAgAZGF0YQAAAAA=';
		if (processing) {
			this._processing.show();
		}
		this._waveformImage.reset()
	}

	/**
	 * Start playing, if media player is not already playing. If media player is already playing this method silently does nothing.
	 */
	play() {
		if (!this._player.paused) {
			return;
		}
		this._player.play();
		this._timer = window.setInterval(() => {
			if (this._player.paused) {
				this.stop();
			}
			else {
				window.requestAnimationFrame(this._updateProgress.bind(this));
			}
		}, this._PROGRESS_UPDATE_PERIOD);
	}

	/**
	 * Stop playing but keep media player initialized. This method fires <code>stop</code> event.
	 */
	stop() {
		window.clearInterval(this._timer);
		this._player.pause();
		this._player.currentTime = this._progressOffset * (this._player.duration | 0);
		this._updateProgress();
		this._events.fire("stop");
	}

	/**
	 * Return true if this player is playing.
	 * 
	 * @returns {boolean} true if this player is playing.
	 */
	isPlaying() {
		return !this._player.paused;
	}

	getSelectionStart() {
		return this._progressOffset * this._player.duration;
	}

	/**
	 * Attempt to reset selection and return true if operation was actually performed.
	 * 
	 * @returns {boolean} if reset selection was actualy performed.
	 */
	resetSelection() {
		if (this._progressOffset > 0) {
			this._progressOffset = 0;
			this._player.currentTime = 0;
			this._updateProgress();
			return true;
		}
		return false;
	}

	/**
	 * Event listener for waveform image loading error. This event may be triggered if waveform is missing from server. Takes
	 * care to create a new one.
	 * 
	 * @param {js.event.Event} ev - error event. 
	 */
	_onMissingWaveform(ev) {
		const object = this._formPage.getUIObject();
		if (object.name) {
			// allows only one request for waveform generation
			this._waveformImage.un("error", this._onMissingWaveform);
			AtlasService.generateWaveform(object, waveformSrc => this._waveformImage.setValue(waveformSrc));
		}
	}

	/**
	 * Event listener for image mouse click.
	 * 
	 * @param {js.dom.Event} ev - mouse event. 
	 */
	_onClick(ev) {
		const progressWidth = ev.pageX - this._waveformLeft;
		this._progressOffset = progressWidth / this._waveformWidth;
		this._player.currentTime = this._progressOffset * (this._player.duration | 0);
		this._updateProgress();
	}

	_updateProgress() {
		const percent = this._player.currentTime / this._player.duration;
		this._progressIndicator.style.setWidth(this._waveformWidth * percent);
	}

	/**
	 * Update audio player media source and associated sample SRC input. If given sample SRC is null this method just reset sample 
	 * SRC input value and stop the player.
	 * 
	 * @param {string} sampleSrc - root-relative SRC for audio sample.
	 * @see #_sampleSrcInput
	 */
	_updatePlayer(sampleSrc) {
		this._sampleSrcInput.setValue("");
		this.stop();
		if (sampleSrc) {
			this._sampleSrcInput.setValue(sampleSrc);
			this._player.src = sampleSrc + '?' + Date.now();
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @returns {string} this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.AudioPlayer";
	}
};
