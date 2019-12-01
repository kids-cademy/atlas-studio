$package("com.kidscademy.form");

com.kidscademy.form.IdentityFieldSet = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this._nameView = this.getByName("name");
		this._nameView.on("paste", this._onNamePaste, this);

		this._displayView = this.getByName("display");
		this.getByName("copy-to-clipboard").on("click", this._onDisplayCopy, this);
	}

	_onNamePaste(ev) {
		function toHyphenCase (str) {
			if (!str) {
				return '';
			}
			// to simplify tokenizer logic first group found is prefixed with dash
			// substr(1) skip the first dash
			return str.replace(/(?:\s*(\S+))/g, function ($0, $1) {
				return '-' + $1.toLowerCase();
			}).substr(1);
		}
	
		ev.prevent();
		this._nameView.setValue(toHyphenCase(ev.getData()));
	}

	_onDisplayCopy() {
		this._displayView.copyToClipboard();
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
