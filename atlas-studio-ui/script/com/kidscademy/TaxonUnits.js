$package("com.kidscademy");

com.kidscademy.TaxonUnits = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._excludes = [];

        this._tableView = this.getByCssClass("list-view");

        this._searchInput = this.getByName("search-input");
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
    }

    setExcludes(excludes) {
        this._excludes = excludes;
    }

    load() {
        const search = this._searchInput.getValue();
        AtlasService.getTaxonUnits(search, this._excludes, TaxonUnits => this._tableView.setObject(TaxonUnits));
        this.show();
    }

    getItems() {
        return this._tableView.getChildren().map(row => row);
    }

    // --------------------------------------------------------------------------------------------
    // ACTION HANDLERS

    _onSearch() {
        this.load();
    }

    _onReset() {
        this._searchInput.reset();
        this.load();
    }

    // --------------------------------------------------------------------------------------------

    toString() {
        return "com.kidscademy.TaxonUnits";
    }
};
