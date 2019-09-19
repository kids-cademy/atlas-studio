$package("com.kidscademy.form");

com.kidscademy.form.TextControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
	}

	setValue(text) {
		if (text == null) {
			this.reset();
			return;
		}
		this._setValue(text.replace(/<p>/g, "").replace(/<\/p>/g, "\n\n"));
	}

	getValue() {
		const text = this._getValue();
		if (!text) {
			return null;
		}
		return "<p>" + text.trim().replace(/\n\n/g, "</p><p>") + "</p>";
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.TextControl";
	}
};

$preload(com.kidscademy.form.TextControl);
