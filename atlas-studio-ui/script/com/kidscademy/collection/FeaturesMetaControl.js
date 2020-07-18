$package("com.kidscademy.collection");

com.kidscademy.collection.FeaturesMetaControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._featuresListView = this.getByCss(".features .list-view");
        this._featuresListView.on("click", this._onFeaturesClick, this);

        this._candidatesListView = this.getByCssClass("editor");
        this._candidatesListView.on("click", this._onCandidatesClick, this);

        this._itemSelect = WinMain.page.getByClass(com.kidscademy.ItemSelect);
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._actions.showOnly("add", "clone", "remove-all");
    }

    /**
     * Initialize internall features meta list. If provided argument is null clear the list.
     * 
     * @param {Array} featuresMeta features meta list, null accepted.
     * @returns {com.kidscademy.collection.FeaturesMetaControl} this object.
     */
    setValue(featuresMeta) {
        this._featuresListView.setObject(featuresMeta);
        this._updateCandidatesExcludes();
        return this;
    }

    /**
     * Returns features meta containing by this control internall list. If the list is empty returns null.
     * 
     * @returns {Array} features meta list or null.
     */
    getValue() {
        if (!this._featuresListView.hasChildren()) {
            return null;
        }
        return this._featuresListView.getChildren().map(row => row.getUserData());
    }

    isValid() {
        return this.hasCssClass("optional") || this._featuresListView.hasChildren();
    }

    _onFeaturesClick(ev) {
        const row = ev.target.getParentByTag("tr");
        if (row != null) {
            row.toggleCssClass("selected");
            const featuresSelected = this._getSelectedFeatures().length > 0;
            this._actions.show(featuresSelected, "remove").show(!featuresSelected, "remove-all");
        }
    }

    _onCandidatesClick(ev) {
        const row = ev.target.getParentByTag("tr");
        if (row != null) {
            this._featuresListView.addChild(row);
            this._updateCandidatesExcludes();
        }
    }

    // --------------------------------------------------------------------------------------------
    // ACTION HANDLERS

    _onAdd() {
        this._candidatesListView.load();
        this._actions.show("add-all", "close").hide("remove-all");
    }

    _onAddAll() {
        this._candidatesListView.getItems().forEach(row => this._featuresListView.addChild(row));
        this._updateCandidatesExcludes();
        this._onClose();
    }

    _onClone() {
        AtlasService.getCollections(collections => this._itemSelect.load(collections));
        this._itemSelect.open(collection => {
            AtlasService.getCollectionFeaturesMeta(collection.id, featuresMeta => this.setValue(featuresMeta));
        });
    }

    _onRemove() {
        this._getSelectedFeatures().forEach(row => row.remove());
        this._updateCandidatesExcludes();
        this._onClose();
    }

    _onRemoveAll() {
        js.ua.System.confirm("@string/confirm-all-features-meta-remove", ok => {
            if (ok) {
                this._featuresListView.removeChildren();
            }
        });
    }

    _onClose() {
        this._candidatesListView.hide();
        this._actions.showOnly("add", "clone", "remove-all");
    }

    // --------------------------------------------------------------------------------------------

    _updateCandidatesExcludes() {
        const excludes = this._featuresListView.getChildren().map(row => row.getUserData().id);
        this._candidatesListView.setExcludes(excludes);
    }

    _getSelectedFeatures() {
        const rows = [];
        this._featuresListView.getChildren().forEach(row => {
            if (row.hasCssClass("selected")) {
                rows.push(row);
            }
        });
        return rows;
    }

    toString() {
        return "com.kidscademy.collection.FeaturesMetaControl";
    }
};
