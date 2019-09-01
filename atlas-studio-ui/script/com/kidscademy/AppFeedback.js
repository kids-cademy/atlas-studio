$package("com.kidscademy");

/**
 * AppFeedback class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.AppFeedback = class extends js.dom.Element {
	/**
	 * Construct an instance of AppFeedback class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Feedback form.
		 * 
		 * @type js.dom.Form
		 */
		this._form = this.getByTag("form");

		/**
		 * Application ID as registered into kinds (a)cademy servers.
		 * 
		 * @type String
		 */
		this._appID = null;

		this.getByCss("button.submit").on("click", this._onSubmit, this);
		this.getByCss("button.cancel").on("click", this._onCancel, this);
	}

	setAppID(appID) {
		this._appID = appID;
	}

	_onSubmit(ev) {
		if (!this._form.isValid()) {
			return;
		}

		const message = this._form.getObject();
		const text = com.kidscademy.util.Text.text2html(message.text);
		if (text) {
			com.kidscademy.ServiceController.sendDeveloperMessage(this._appID, null, null, text, message.senderEmail, () => this._form.reset());
		}
	}

	_onCancel(ev) {
		this._form.reset();
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.AppFeedback";
	}
};
