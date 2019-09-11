$package("com.kidscademy.reader");

/**
 * Reader related objects view.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.reader.RelatedView = class extends js.dom.Element {
	/**
	 * Construct reader related objects view.
	 * 
	 * @param js.dom.Document ownerDoc element owner document,
	 * @param Node node native {@link Node} instance.
	 * @assert assertions imposed by {@link js.dom.Element#Element(js.dom.Document, Node)}.
	 */
	constructor(ownerDoc, node) {
		super(ownerDoc, node);
		this._listView = this.getByCssClass("list");
	}

	setObject(names) {
		const collection = WinMain.page.getContextAttr("collection");
		AtlasService.getRelatedAtlasObjects(collection.id, names, objects => {
			this._listView.setObject(objects);
		});
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.reader.RelatedView";
	}
};
