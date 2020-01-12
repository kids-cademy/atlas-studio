$package("com.kidscademy.reader");

com.kidscademy.reader.FactsView = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this._listView = this.getByCssClass("list");

		this._top = 0;

		this._minTop = 0;

		document.addEventListener("mouse-click", this._onMouseClick.bind(this), false);
		document.addEventListener("mouse-down", this._onMouseDown.bind(this), false);
		document.addEventListener("mouse-move", this._onMouseMove.bind(this), false);
	}

	_onMouseClick(ev) {
		const item = ev.detail.getParentByCssClass("item");
		if (item != null) {
			item.toggleCssClass("opened");
			this._minTop = this.style.getHeight() - this._listView.style.getHeight();
			if (this._top < this._minTop) {
				this._top = this._minTop;
			}
			this._listView.style.setTop(this._top);
		}
	}

	_onMouseDown(event) {
		this._startTop = parseInt(this._listView.style.get("top"));
		if (this._minTop === 0) {
			this._minTop = this.style.getHeight() - this._listView.style.getHeight();
		}
	}

	_onMouseMove(event) {
		// ev.detail is custom event data that in our case is deltaPageY
		this._top = this._startTop + event.detail;
		if (this._top > 0) {
			this._top = 0;
		}
		if (this._top < this._minTop) {
			this._top = this._minTop;
		}
		this._listView.style.setTop(this._top);
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.reader.FactsView";
	}
};
