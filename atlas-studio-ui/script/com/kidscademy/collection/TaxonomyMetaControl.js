$package("com.kidscademy.collection");

com.kidscademy.collection.TaxonomyMetaControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._taxonsMetaView = this.getByCssClass("taxons-meta");
        this._taxonsMetaView.on("click", this._onViewClick, this);

        this._taxonUnitsView = this.getByCssClass("taxon-units");
        this._taxonUnitsView.on("click", this._onTaxonUnitsClick, this);

        this._taxonEditor = this.getByCssClass("taxon-form");
        this._taxonNameInput = this._taxonEditor.getByCssClass("taxon-name");
        this._taxonDisplayInput = this._taxonEditor.getByCssClass("taxon-display");
        this._taxonValuesInput = this._taxonEditor.getByCssClass("taxon-values");

        this._currentRow = null;

        this._itemSelect = WinMain.page.getByClass(com.kidscademy.ItemSelect);
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._actions.showOnly("add", "clone", "remove-all");
    }

    setValue(taxonomyMeta) {
        this._taxonsMetaView.setObject(taxonomyMeta);
        this._updateCandidatesExcludes();
        return this;
    }

    getValue() {
        return this._taxonsMetaView.getChildren().map(row => row.getUserData());
    }

    isValid() {
        return this.hasCssClass("optional") || this._taxonsMetaView.getChildrenCount() > 0;
    }

    _onViewClick(ev) {
        this._currentRow = ev.target.getParentByTag("tr");
        if (this._currentRow == null) {
            return;
        }

        const taxon = this._currentRow.getUserData();
        this._taxonNameInput.setValue(taxon.unit.name).focus();
        this._taxonDisplayInput.setValue(taxon.unit.display);
        this._taxonValuesInput.setValue(taxon.values);
        this._taxonEditor.show();
        this._taxonUnitsView.hide();
        this._actions.show("remove", "done", "close").hide("remove-all");
    }

    _onTaxonUnitsClick(ev) {
        const row = ev.target.getParentByTag("tr");
        if (row != null) {
            this._taxonsMetaView.addObject({
                id: 0,
                unit: row.getUserData(),
                values: null
            });
            this._updateCandidatesExcludes();
            row.remove();
        }
    }

    _onAdd() {
        this._currentRow = null;
        this._taxonEditor.hide();
        this._taxonUnitsView.load();
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

            this._taxonsMetaView.addObject(taxonMeta);
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
        this._updateCandidatesExcludes();
        this._onClose();
    }

    _onRemoveAll() {
        js.ua.System.confirm("@string/confirm-all-taxons-meta-remove", ok => {
            if (ok) {
                this._taxonsMetaView.removeChildren();
                this._updateCandidatesExcludes();
            }
        });
    }


    _onClose() {
        this._currentRow = null;
        this._taxonEditor.hide();
        this._taxonUnitsView.hide();
        this._actions.showOnly("add", "clone", "remove-all");
    }

    _updateCandidatesExcludes() {
        const excludes = this._taxonsMetaView.getChildren().map(taxonMetaView => taxonMetaView.getUserData().unit.id);
        this._taxonUnitsView.setExcludes(excludes);
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
