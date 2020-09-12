$package("com.kidscademy.page");

com.kidscademy.page.TaxonUnitsTranslatorPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/taxon-unit-translator");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("taxon-units", this._onTaxonUnits, this);
        this._sidebar.on("translate-all", this._onTranslateAll, this);
        this._sidebar.on("save-changes", this._onSaveChanges, this);
        this._sidebar.on("cancel-changes", this._onCancelChanges, this);

        this._tableView = this.getByCssClass("list-view");

        this._searchInput = this.getByName("search-input");
        this._actions = this.getByClass(com.kidscademy.Actions).key(this._searchInput).bind(this);
        this._actions.on("language", "change", this._load, this);
        this._load();

        this.on("input", ev => {
            if (ev.target.hasAttr("contenteditable")) {
                ev.target.getParentByTag("tr").addCssClass("edited");
            }
        });
    }

    _load({ alert = true } = {}) {
        if (alert) {
            const edited = this._tableView.findByCssClass("edited");
            if (!edited.isEmpty()) {
                js.ua.System.alert("@string/alert-edited-translations");
                return;
            }
        }

        var search = this._searchInput.getValue();
        if (search != null) {
            search = search.trim();
            if (search.length === 0) {
                search = null;
            }
        }
        AtlasService.getTaxonUnitTranslations(search, this._language(), this._onTranslationsLoaded, this);
    }

    _onTranslationsLoaded(translations) {
        this._tableView.setObject(translations);
        this._searchInput.focus();
    }

    // --------------------------------------------------------------------------------------------
    // SIDEBAR HANDLERS

    _onCollections() {
        WinMain.assign("@link/collections");
    }

    _onLinksMeta() {
        WinMain.assign("@link/external-sources");
    }

    _onFeaturesMeta() {
        WinMain.assign("@link/features-meta");
    }

    _onTaxonUnits() {
        WinMain.assign("@link/taxon-units");
    }

    _onTranslateAll() {
        const ids = this._tableView.getListData(feature => {
            if (!feature.translation) {
                return feature.id;
            }
        });
        if (ids.length === 0) {
            js.ua.System.alert("@string/alert-nothing-to-translate");
            this._searchInput.focus();
            return;
        }
        js.ua.System.confirm($format("@string/confirm-translate-all-taxon-units", ids.length), ok => {
            if (ok) {
                AtlasService.translateAllTaxonUnitsDisplay(ids, this._language(), this._load, this);
            }
        });
    }

    _onSaveChanges() {
        const taxonUnits = this._tableView.findByCss(":scope > .edited").map(row => {
            const taxonUnit = row.getUserData();
            taxonUnit.translation = row.getByCssClass("translation").getText();
            return taxonUnit;
        });
        if(taxonUnits.length === 0) {
            this._searchInput.focus();
            return;
        }
        AtlasService.saveTaxonUnitTranslations(taxonUnits, this._language(), () => {
            this._tableView.getChildren().removeCssClass("edited");
        });
    }

    _onCancelChanges() {
        const size = this._tableView.findByCss(":scope > .edited").size();
        if (size === 0) {
            this._searchInput.focus();
            return;
        }
        js.ua.System.confirm($format("@string/confirm-cancel-changes", size), ok => {
            if (ok) {
                this._load({ alert: false });
            }
        });
    }

    // --------------------------------------------------------------------------------------------
    // ACTION HANDLERS

    _onSearch() {
        this._load();
    }

    _onReset() {
        this._searchInput.reset();
        this._load();
    }

    // --------------------------------------------------------------------------------------------

    _language() {
        return this._actions.getValue("language");
    }

    toString() {
        return "com.kidscademy.page.TaxonUnitsTranslatorPage";
    }
};

WinMain.createPage(com.kidscademy.page.TaxonUnitsTranslatorPage);
