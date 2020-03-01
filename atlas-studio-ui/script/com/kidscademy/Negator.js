$package("com.kidscademy");

com.kidscademy.Negator = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this.on("click", this._onClick, this);
	}

	getValue() {
		return this.hasCssClass("negated").toString();
	}

	_onClick(ev) {
		this.toggleCssClass("negated");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.Negator";
	}
};

$preload(com.kidscademy.Negator);
