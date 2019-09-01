$package("com.kidscademy");

/**
 * TextArea class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.TextArea = class extends js.dom.Control {
	/**
	 * Construct an instance of TextArea class.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this.on("keyup", this._onKeyUp, this);
		if (this.style.isVisible()) {
			this._onKeyUp(null);
		}
	}

	show() {
		super.show();
		this._onKeyUp(null);
	}

	reset() {
		super.reset();
		this._onKeyUp(null);
	}

	_onKeyUp(ev) {
		$assert(this.style.isVisible(), "com.kidscademy.TextArea#_onKeyUp", "Attempt to auto-resize on invisible text area.");
		this.style.setHeight(1);
		this.style.setHeight(this._node.scrollHeight);
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.TextArea";
	}
};

$preload(com.kidscademy.TextArea);