$package("com.kidscademy");

/**
 * Checkbox CAPTCHA has a hidden standard challenge sentence and image set and a checkbox that reveal them.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.CheckboxCaptcha = class extends js.widget.Captcha {
	/**
	 * Construct an instance of CAPTCHA class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._checkbox = this.getByClass(com.kidscademy.Checkbox);
		this._checkbox.on("click", this._onCheckboxClick, this);

		this._bodyView = this.getByCssClass("body");

		this.getByCssClass("help").on("click", this._onHelp, this);
	}

	isValid() {
		if (!this._checkbox.checked()) {
			js.ua.System.alert("Please check you are a humman.");
			return false;
		}
		return true;
	}

	reset() {
		super.reset();
		this._checkbox.reset();
		this._bodyView.hide();
	}

	_onCheckboxClick(ev) {
		ev.halt();
		this._loadChallenge();
		this._checkbox.uncheck();
		this._bodyView.show();
	}

	_onResponseVerified(challenge) {
		super._onResponseVerified(challenge);
		if (this._valid) {
			this._checkbox.check();
			this._bodyView.hide();
		}
	}

	_onHelp(ev) {
		js.ua.System.alert("Help not yet implemented.");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.CheckboxCaptcha";
	}
};

$preload(com.kidscademy.CheckboxCaptcha);
