$package("com.kidscademy");

com.kidscademy.TableView = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._tbody = this.getByTag("tbody");

		this._tbody.on("dragstart", this._onDragStart, this);
		this._tbody.on("dragover", this._onDragOver, this);
		this._tbody.on("drop", this._onDrop, this);
    }

	// --------------------------------------------------------------------------------------------
	// DRAG AND DROP

	_onDragStart(ev) {
		const row = ev.target.getParentByTag("tr");
		ev.setData({
			index: row.getChildIndex()
		});
	}

	_onDragOver(ev) {
		ev.prevent();
	}

	_onDrop(ev) {
		ev.prevent();
		const data = ev.getData();
		const sourceRow = this._tbody.getByIndex(data.index);
		const targetRow = ev.target.getParentByTag("tr");

		if (targetRow == null) {
			return;
		}
		if (targetRow === sourceRow) {
			return;
		}

		if (ev.ctrlKey) {
			const siblingRow = targetRow.getNextSibling();
			if (siblingRow == null) {
				targetRow.getParent().addChild(sourceRow);
			}
			else {
				siblingRow.insertBefore(sourceRow);
			}
		}
		else {
			targetRow.insertBefore(sourceRow);
		}
	}

	// --------------------------------------------------------------------------------------------

    toString() {
        return "com.kidscademy.TableView";
    }
};
