$package("com.kidscademy.workspace");

com.kidscademy.workspace.FeaturesMetaControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._featuresListView = this.getByCssClass("features-list");
        this._featuresListView.on("click", this._onFeaturesClick, this);

        this._editor = this.getByCssClass("editor");
        this._candidatesListView = this.getByCssClass("candidates-list");
        this._candidatesListView.on("click", this._onCandidatesClick, this);

        this._currentRow = null;

        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
    }

    setValue(featuresMeta) {
        this._featuresListView.setObject(featuresMeta);
        return this;
    }

    getValue() {
        return this._featuresListView.getChildren().map(row => row.getUserData());
    }

    isValid() {
        return this._featuresListView.getChildrenCount() > 0;
    }

    _onFeaturesClick(ev) {
        if (this._currentRow != null) {
            this._currentRow.removeCssClass("selected");
        }

        const row = ev.target.getParentByTag("tr");
        if (this._currentRow == row) {
            this._currentRow = null;
            return;
        }

        this._currentRow = row;
        if (this._currentRow == null) {
            return;
        }
        this._currentRow.addCssClass("selected");
    }


    _onCandidatesClick(ev) {
        const row = ev.target.getParentByTag("tr");
        if(row != null) {
            this._featuresListView.addChild(row);
        }
    }

    // --------------------------------------------------------------------------------------------
    // ACTION HANDLERS

    _onAdd() {
        this._editor.show();
        AtlasService.getFeaturesMetaCandidates(this.getValue(), candidates => this._candidatesListView.setObject(candidates));
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
        this._currentRow = null;
        this._onClose();
    }

    _onClose() {
        //this._currentRow = null;
        this._editor.hide();
    }

    // --------------------------------------------------------------------------------------------

    toString() {
        return "com.kidscademy.workspace.FeaturesMetaControl";
    }
};
