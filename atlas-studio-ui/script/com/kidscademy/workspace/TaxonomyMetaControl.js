$package("com.kidscademy.workspace");

com.kidscademy.workspace.TaxonomyMetaControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._taxonomyMetaView = this.getByCssClass("taxonomy-view");
        this._taxonomyMetaView.on("click", this._onViewClick, this);

        this._taxonEditor = this.getByCssClass("taxon-editor");
        this._taxonNameInput = this.getByCssClass("taxon-name");
        this._taxonValuesInput = this.getByCssClass("taxon-values");

        this._currentRow = null;

        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
    }

    setValue(taxonomyMeta) {
        this._taxonomyMetaView.setObject(taxonomyMeta);
        return this;
    }

    getValue() {
        const taxonomyMeta = [];
        this._taxonomyMetaView.getChildren().forEach(row => taxonomyMeta.push(row.getUserData()));
        return taxonomyMeta;
    }

    isValid() {
        return this._taxonomyMetaView.getChildrenCount() > 0;
    }

    _onViewClick(ev) {
        this._currentRow = ev.target.getParentByTag("tr");
        if (this._currentRow == null) {
            return;
        }

        const taxon = this._currentRow.getUserData();
        this._taxonNameInput.setValue(taxon.name).focus();
        this._taxonValuesInput.setValue(taxon.values);
        this._taxonEditor.show();
    }

    _onAdd() {
        this._currentRow = null;
        this._taxonEditor.show();
        this._taxonNameInput.reset();
        this._taxonValuesInput.reset();
    }

    _onDone() {
        const taxon = {
            name: this._taxonNameInput.getValue(),
            values: this._taxonValuesInput.getValue()
        };

        if (this._currentRow == null) {
            this._taxonomyMetaView.addObject(taxon);
            this._onClose();
            return;
        }

        this._currentRow.setObject(taxon);
        this._onClose();
    }

    _onRemove() {
        this._currentRow.remove();
        this._onClose();
    }

    _onClose() {
        this._currentRow = null;
        this._taxonEditor.hide();
    }

    toString() {
        return "com.kidscademy.workspace.TaxonomyMetaControl";
    }
};
