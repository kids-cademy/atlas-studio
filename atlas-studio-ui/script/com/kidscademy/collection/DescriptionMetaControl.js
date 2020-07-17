$package("com.kidscademy.collection");

com.kidscademy.collection.DescriptionMetaControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._tableView = this.getByCssClass("view");
        this._tableView.on("click", this._onViewClick, this);

        this._metaEditor = this.getByCssClass("editor");

        this._currentRow = null;

        this._itemSelect = WinMain.page.getByClass(com.kidscademy.ItemSelect);
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._actions.showOnly("add", "clone", "remove-all");
    }

    setValue(descriptionMeta) {
        this._tableView.setObject(descriptionMeta);
        return this;
    }

    getValue() {
        return this._tableView.getChildren().map(row => row.getUserData());
    }

    isValid() {
        return this.hasCssClass("optional") || this._tableView.getChildrenCount() > 0;
    }

    _onViewClick(ev) {
        this._currentRow = ev.target.getParentByTag("tr");
        if (this._currentRow != null) {
            this._metaEditor.setObject(this._currentRow.getUserData()).show();
            this._actions.show("remove", "done", "close").hide("remove-all");
        }
    }

    _onAdd() {
        this._currentRow = null;
        this._metaEditor.reset().show();
        this._actions.show("done", "close").hide("remove-all");
    }

    _onClone() {
        AtlasService.getCollections(collections => this._itemSelect.load(collections));
        this._itemSelect.open(collection => {
            AtlasService.getCollectionDescriptionsMeta(collection.id, descriptionsMeta => {
                descriptionsMeta.forEach(descriptionMeta => descriptionMeta.id = 0);
                this._tableView.setObject(descriptionsMeta)
            });
        });
    }

    _onDone() {
        const descriptionMeta = this._metaEditor.getObject();

        if (this._currentRow == null) {
            this._tableView.addObject(descriptionMeta);
            this._onClose();
            return;
        }

        this._currentRow.setObject(descriptionMeta);
        this._onClose();
    }

    _onRemove() {
        this._currentRow.remove();
        this._onClose();
    }

    _onRemoveAll() {
		js.ua.System.confirm("@string/confirm-all-description-meta-remove", ok => {
			if (ok) {
                this._tableView.removeChildren();
			}
		});
    }

    _onClose() {
        this._currentRow = null;
        this._metaEditor.hide();
        this._actions.showOnly("add", "clone", "remove-all");
    }

    toString() {
        return "com.kidscademy.collection.DescriptionMetaControl";
    }
};
