$package("com.kidscademy");

com.kidscademy.FeaturesMeta = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        /**
         * Optional excludes list, default to empty. Contains database ID for features meta that should 
         * be excluded from loading. These features meta already existing on user interface and that 
         * should not be reloaded from server.
         * @type {Array<Number>}
         */
        this._excludes = [];

        this._tableView = this.getByCssClass("list-view");

        this._searchInput = this.getByName("search-input");
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
    }

    /**
     * 
     * @param {Array<Number>} excludes 
     * @see #this._excludes
     */
    setExcludes(excludes) {
        this._excludes = excludes;
    }

    load() {
        var search = this._searchInput.getValue();
        if (search != null) {
            search = search.trim();
            if (search.length === 0) {
                search = null;
            }
        }
        AtlasService.getFeaturesMeta(search, this._excludes, featuresMeta => this._tableView.setObject(featuresMeta));
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
        return "com.kidscademy.FeaturesMeta";
    }
};
