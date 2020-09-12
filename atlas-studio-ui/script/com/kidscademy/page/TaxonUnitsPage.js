$package("com.kidscademy.page");

com.kidscademy.page.TaxonUnitsPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/taxon-units");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("taxon-units", this._onTaxonUnits, this);
        this._sidebar.on("create-taxon-unit", this._onCreateTaxonUnit, this);
        this._sidebar.on("translate-taxon-units", this._onTranslateTaxonUnits, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit", this._onEditTaxonUnit, this);
        this._contextMenu.on("remove", this._onRemoveTaxonUnit, this);

        this._tableView = this.getByCssClass("taxon-units-list");
        this._tableView.on("click", this._onClick, this);
        this._tableView.on("contextmenu", this._onContextMenu, this);

        this._searchInput = this.getByName("search-input");
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._load();
    }

    _load() {
        this._tableView.load();
    }

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

    _onCreateTaxonUnit() {
        WinMain.assign("@link/taxon-unit-form");
    }

    _onTranslateTaxonUnits() {
        WinMain.assign("@link/taxon-units-translator");
    }

    _onEditTaxonUnit(taxonUnitView) {
        const taxonUnit = taxonUnitView.getUserData();
        WinMain.assign("@link/taxon-unit-form", { taxon: taxonUnit.id });
    }

    _onRemoveTaxonUnit(taxonUnitView) {
        js.ua.System.confirm("@string/confirm-taxon-unit-remove", ok => {
            if (ok) {
                const taxonUnit = taxonUnitView.getUserData();
                AtlasService.removeTaxonUnit(taxonUnit.id, () => taxonUnitView.remove());
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

    _onClick(ev) {
        const taxonUnitView = ev.target.getParentByCssClass("taxon-unit-view");
        if (taxonUnitView != null) {
            if (this._contextMenu != null) {
                const action = this._contextMenu.getDefaultAction(ev);
                if (action != null) {
                    action.listener.call(action.scope, taxonUnitView);
                }
            }
        }
    }

    _onContextMenu(ev) {
        const taxonUnitView = ev.target.getParentByCssClass("taxon-unit-view");
        if (taxonUnitView != null) {
            ev.halt();
            this._contextMenu.open(taxonUnitView);
        }
    }

    toString() {
        return "com.kidscademy.page.TaxonUnitsPage";
    }
};

WinMain.createPage(com.kidscademy.page.TaxonUnitsPage);
