$package("com.kidscademy");

com.kidscademy.TextColumn = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._availableHeight = this.getParent().style.getHeight();
	}

	setObject(atlasObject) {
		var paragraphMargin = 0
		for (; ;) {
			const paragraphElement = atlasObject.paragraphs.getFirstChild();
			if (paragraphElement == null) {
				break;
			}
			if (paragraphMargin === 0) {
				paragraphMargin = paragraphElement.style.getMargin().bottom;
			}
			const paragraphHeight = paragraphElement.style.getHeight();
			const columnHeight = this.style.getHeight();
			if (columnHeight + paragraphHeight + paragraphMargin > this._availableHeight) {
				break;
			}
			this.addChild(paragraphElement);
		}
		if (this.getChildrenCount() === 0) {
			this.getParent().hide();
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.TextColumn";
	}
};