$package("com.kidscademy");

/**
 * SysMessage class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.SysMessage = class extends js.dom.Element {
	/**
	 * Construct an instance of SysMessage class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this._messageView = this.getByCssClass("message");

		/**
		 * Track current message level in order to remove it when switch to another level.
		 * 
		 * @type String
		 */
		this._currentLevel = null;

		this.getByCssClass("close").on("click", this.close, this);
	}

	success(message) {
		this.open(message, "success");
	}

	info(message) {
		this.open(message, "info");
	}

	warning(message) {
		this.open(message, "warning");
	}

	danger(message) {
		this.open(message, "danger");
	}

	open(message, level) {
		if (this._currentLevel != null) {
			this.removeCssClass(this._currentLevel);
		}
		this._currentLevel = level;
		this.addCssClass(level);
		this.addCssClass("visible");
		this._messageView.setText(message);
	}

	close() {
		this.removeCssClass("visible");
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.SysMessage";
	}
};
