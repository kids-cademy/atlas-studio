$package("com.kidscademy.page");

/**
 * Reader page class.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.page.ReaderPage = class extends com.kidscademy.page.Page {
	/**
	 * Construct an instance of reader page.
	 */
	constructor() {
		super();
		this._objectView = WinMain.doc.getByTag("body");

		const object = this.getContextAttr("preview-object");

		function list(object) {
			var list = [];
			for (var name in object) {
				list.push({
					name: name,
					value: object[name]
				});
			}
			return list;
		}
		// ECMA Script has no builtin support for dictionaries and emulation with object properties is limited
		// so, for presentation purposes, we need to convert atlas object dictionaries into lists
		object.facts = list(object.facts);

		this._objectView.setObject(object);
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.page.ReaderPage";
	}
};

WinMain.createPage(com.kidscademy.page.ReaderPage);
