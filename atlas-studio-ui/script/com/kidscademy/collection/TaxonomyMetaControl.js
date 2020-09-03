$package("com.kidscademy.collection");

com.kidscademy.collection.TaxonomyMetaControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._taxonomyMetaView = this.getByCssClass("taxonomy-view");
        this._taxonomyMetaView.on("click", this._onViewClick, this);

        this._taxonEditor = this.getByCssClass("editor");
        this._taxonNameInput = this._taxonEditor.getByCssClass("taxon-name");
        this._taxonDisplayInput = this._taxonEditor.getByCssClass("taxon-display");
        this._taxonValuesInput = this._taxonEditor.getByCssClass("taxon-values");

        this._currentRow = null;

        this._itemSelect = WinMain.page.getByClass(com.kidscademy.ItemSelect);
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._actions.showOnly("add", "clone", "translate", "remove-all");
    }

    setValue(taxonomyMeta) {
        this._taxonomyMetaView.setObject(taxonomyMeta);
        return this;
    }

    getValue() {
        return this._taxonomyMetaView.getChildren().map(row => row.getUserData());
    }

    isValid() {
        return this.hasCssClass("optional") || this._taxonomyMetaView.getChildrenCount() > 0;
    }

    _onViewClick(ev) {
        this._currentRow = ev.target.getParentByTag("tr");
        if (this._currentRow == null) {
            return;
        }

        const taxon = this._currentRow.getUserData();
        this._taxonNameInput.setValue(taxon.name).focus();
        this._taxonDisplayInput.setValue(taxon.display);
        this._taxonValuesInput.setValue(taxon.values);
        this._taxonEditor.show();
        this._actions.show("remove", "done", "close").hide("remove-all");
    }

    _onAdd() {
        this._currentRow = null;
        this._taxonEditor.show();
        this._taxonNameInput.reset();
        this._taxonDisplayInput.reset();
        this._taxonValuesInput.reset();
        this._actions.show("done", "close").hide("remove-all");
    }

    _onClone() {
        AtlasService.getCollections(collections => this._itemSelect.load(collections));
        this._itemSelect.open(collection => {
            AtlasService.getCollectionTaxonomyMeta(collection.id, taxonomyMeta => this.setValue(taxonomyMeta));
        });
    }

    _onDone() {
        if (this._currentRow == null) {
            const taxonMeta = {
                name: this._taxonNameInput.getValue(),
                display: this._taxonDisplayInput.getValue(),
                values: this._normalizeValues(this._taxonValuesInput.getValue())
            };

            this._taxonomyMetaView.addObject(taxonMeta);
            this._onClose();
            return;
        }

        const taxonMeta = this._currentRow.getUserData();
        taxonMeta.name = this._taxonNameInput.getValue();
        taxonMeta.display = this._taxonDisplayInput.getValue();
        taxonMeta.values = this._normalizeValues(this._taxonValuesInput.getValue());

        this._currentRow.setObject(taxonMeta);
        this._onClose();
    }

    _onRemove() {
        this._currentRow.remove();
        this._onClose();
    }

    _onRemoveAll() {
        js.ua.System.confirm("@string/confirm-all-taxons-meta-remove", ok => {
            if (ok) {
                this._taxonomyMetaView.removeChildren();
            }
        });
    }


    _onClose() {
        this._currentRow = null;
        this._taxonEditor.hide();
        this._actions.showOnly("add", "clone", "remove-all");
    }

    /**
     * Taxonomy meta value should not be empty. It can be null or not empty. It also cannot have only white spaces.
     * @param {String} value 
     */
    _normalizeValues(value) {
        if (value) {
            value = value.trim();
        }
        return value ? value : null;
    }

    toString() {
        return "com.kidscademy.collection.TaxonomyMetaControl";
    }
};
