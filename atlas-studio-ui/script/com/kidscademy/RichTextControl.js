$package("com.kidscademy");

com.kidscademy.RichTextControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);
    }

	setValue(text) {
		if (text == null) {
			this.reset();
			return;
		}

		const parser = new DOMParser();
		const document = new js.dom.Document(parser.parseFromString(text, "text/xml"));

		const htm2md = new com.kidscademy.Htm2Md(document.getRoot());
		this._setValue(htm2md.converter());
	}

	getValue() {
		const text = this._getValue();
		if (!text) {
			return null;
		}
		const md2htm = new com.kidscademy.Md2Htm(text);
		return `<${this.getName()}>${md2htm.convert()}</${this.getName()}>`;
	}

    toString() {
        return "com.kidscademy.RichTextControl";
    }
};
