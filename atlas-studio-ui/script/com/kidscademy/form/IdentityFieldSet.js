$package("com.kidscademy.form");

com.kidscademy.form.IdentityFieldSet = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._nameView = this.getByName("display");
		this.getByName("copy-to-clipboard").on("click", this._onCopyToClipboard, this);
	}

	_onCopyToClipboard() {
		this._nameView.copyToClipboard();
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.IdentityFieldSet";
	}
};

$preload(com.kidscademy.form.IdentityFieldSet);
