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
        }
    }

    _onAdd() {
        this._currentRow = null;
        this._metaEditor.reset().show();
    }

    _onClone() {
        AtlasService.getCollections(collections => this._itemSelect.load(collections));
        this._itemSelect.open(collection => {
            AtlasService.getCollectionDescriptionMeta(collection.id, descriptionMeta => this._tableView.setObject(descriptionMeta));
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

    _onClose() {
        this._currentRow = null;
        this._metaEditor.hide();
    }

    toString() {
        return "com.kidscademy.collection.DescriptionMetaControl";
    }
};
