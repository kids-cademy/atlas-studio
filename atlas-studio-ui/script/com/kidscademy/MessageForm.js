$package("com.kidscademy");

/**
 * Suggestion form takes care to save on server normalized suggestion message.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.MessageForm = class extends com.kidscademy.Form {
	/**
	 * Construct an instance of MessageForm class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Underlying form.
		 * 
		 * @type js.dom.Form
		 */
		this._form = this.getByTag("form");

		/**
		 * Captcha used to verify 'do not like' submit.
		 * 
		 * @type com.kidscademy.CheckboxCaptcha
		 */
		this._captcha = this.getByClass(com.kidscademy.CheckboxCaptcha);

		this._events = this.getCustomEvents();
		this._events.register("submitted");
		this._events.register("canceled");

		this.getByCss(".submit").on("click", this._onSubmit, this);
		this.getByCss(".cancel").on("click", this._onCancel, this);
	}

	reset() {
		this._form.reset();
	}

	/**
	 * Get form data and if message present save suggestion to server. Takes care to normalize suggestion message, see
	 * {@link #_normalize(String)}.
	 * 
	 * @param js.event.Event ev mouse click event.
	 */
	_onSubmit(ev) {
		if (!this._form.isValid()) {
			return;
		}
		const message = this._form.getObject();
		const text = this._normalize(message.text);
		if (text) {
			ServiceController.sendDeveloperMessage("com.kidscademy", null, null, text, message.senderEmail, () => {
				this._form.reset();
				this._events.fire("submitted");
			});
		}
	}

	/**
	 * Cancel form reset both message and email address, if any.
	 * 
	 * @param js.event.Event ev mouse click event.
	 */
	_onCancel(ev) {
		this._form.reset();
		this._events.fire("canceled");
	}

	/**
	 * Convert plain text into HTML formatted text. Current implementation just uses line break as separator for
	 * paragraphs. Also paragraphs are trimmed both sides.
	 * 
	 * @param String plain text.
	 * @return String HTML formatted text.
	 */
	_normalize(text) {
		var html = "";
		text.split(/\n/g).forEach((paragraph) => {
			paragraph = paragraph.trim();
			if (paragraph) {
				html += (`<p>${paragraph}</p>`);
			}
		});
		return html;
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.MessageForm";
	}
};

$preload(com.kidscademy.MessageForm);
