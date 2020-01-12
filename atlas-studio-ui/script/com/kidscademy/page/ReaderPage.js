$package("com.kidscademy.page");

/**
 * Reader page class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.ReaderPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of reader page.
	 */
	constructor() {
		super();

		this._doc = WinMain.doc;
		this._panorama = this._doc.getByCss(".h-linear");
		this._doc.on("mousedown", this._onMouseDown, this);

		this._objectView = this._doc.getByTag("body");
		this._paragraphsCache = this._doc.getByCss(".paragraphs-cache");

		/**
		 * While scrolling horizontally to left, panorama view left poisition is decresing toward negative values
		 * This property store the minimum value that left position can have provided all content was scrolled. This
		 * value is initialized on the fly by mouse down handler in order to ensure all views are initialized.
		 * 
		 * @type Number
		 */
		this._minLeft = 0;

		const objectId = Number(WinMain.url.parameters.id);
		AtlasService.getExportObject(objectId, object => {
			this._paragraphsCache.setHTML(object.description);
			object.paragraphs = this._paragraphsCache;
			this._objectView.setObject(object);
		});
	}

	_onMouseDown(ev) {
		if (this._lock) {
			return;
		}
		ev.prevent();
		this._mouseMoved = false;
		this._scrollx = true;

		this._startPageX = ev.pageX;
		this._startPageY = ev.pageY;
		this._startLeft = parseInt(this._panorama.style.get('left'));
		this._startTop = parseInt(this._panorama.style.get('top'));

		this._doc.on("mousemove", this._onMouseMove, this);
		this._doc.on("mouseup", this._onMouseUp, this);

		if (this._minLeft === 0) {
			// minimum left position is determined heuristically
			this._minLeft = WinMain.getWidth() - this._panorama.style.getWidth() - 30;
		}

		const event = new CustomEvent("mouse-down", { bubbles: true });
		document.dispatchEvent(event);
	}

	_onMouseMove(ev) {
		const deltaPageX = ev.pageX - this._startPageX;
		const deltaPageY = ev.pageY - this._startPageY;
		const absDeltaPageX = Math.abs(deltaPageX);
		const absDeltaPageY = Math.abs(deltaPageY);

		if (absDeltaPageX < 10 && absDeltaPageY < 10) {
			return;
		}
		this._mouseMoved = true;

		if (this._scrolly || !this._scrollx || absDeltaPageX < absDeltaPageY) {
			this._scrolly = true;
			const event = new CustomEvent("mouse-move", { bubbles: true, detail: deltaPageY });
			document.dispatchEvent(event);
			return;
		}
		this._scrollx = true;

		this._left = this._startLeft + deltaPageX;
		if (this._left > 0) {
			this._left = 0;
		}
		if (this._left < this._minLeft) {
			this._left = this._minLeft;
		}

		this._panorama.style.set("left", this._left + "px");
	}

	_onMouseUp(ev) {
		if (!this._mouseMoved) {
			const event = new CustomEvent("mouse-click", { bubbles: true, detail: ev.target });
			document.dispatchEvent(event);
		}
		this._mouseMoved = false;
		this._doc.un('mousemove', this._onMouseMove);
		this._doc.un('mouseup', this._onMouseUp);
		this._scrolly = false;
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.ReaderPage";
	}
};

WinMain.createPage(com.kidscademy.page.ReaderPage);
