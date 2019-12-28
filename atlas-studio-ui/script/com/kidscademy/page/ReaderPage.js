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
		this._objectView = WinMain.doc.getByTag("body");

		const object = this.getContextAttr("preview-object");

		function list(object) {
			var list = [];
			for (var name in object) {
				list.push({
					name: name,
					value: object[name]
				});
			}
			return list;
		}
		// ECMA Script has no builtin support for dictionaries and emulation with object properties is limited
		// so, for presentation purposes, we need to convert atlas object dictionaries into lists
		object.facts = list(object.facts);

		this._objectView.setObject(object);

		this._doc = WinMain.doc;
		this._panorama = this._doc.getByCss(".h-linear");
		this._doc.on("mousedown", this._onMouseDown, this);
	}

	_onMouseDown(ev) {
		if (this._lock) {
			return;
		}
		ev.prevent();

		this._startPageX = ev.pageX;
		this._startPageY = ev.pageY;
		this._startLeft = parseInt(this._panorama.style.get('left'));

		this._doc.on("mousemove", this._onMouseMove, this);
		this._doc.on("mouseup", this._onMouseUp, this);

		// minimum left position is determined heuristically
		// it is not very clear why panorama width, that is, this.style.getWidth() is changing while dragging
		this._minLeft = WinMain.getWidth() - this._panorama.style.getWidth() - 30;
	}

	_onMouseMove(ev) {
		const deltaPageX = ev.pageX - this._startPageX;
		this._left = this._startLeft + deltaPageX;

		if (this._left > 0) {
			this._left = 0;
		}
		if (this._left < this._minLeft) {
			this._left = this._minLeft;
		}

		console.log(this._left);
		this._panorama.style.set("left", this._left + "px");
	}

	_onMouseUp(ev) {
		this._doc.un('mousemove', this._onMouseMove);
		this._doc.un('mouseup', this._onMouseUp);
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
