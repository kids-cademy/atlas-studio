$package("com.kidscademy.page");

com.kidscademy.page.TaxonUnitForm = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/feature-meta");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("taxon-units", this._onTaxonUnits, this);

        this._taxonUnit = null;
        this._form = this.getByTag("form");

        const taxonId = Number(WinMain.url.parameters.taxon);
        if (taxonId) {
            AtlasService.getTaxonUnit(taxonId, this._onTaxonUnitLoaded, this);
        }
        else {
            AtlasService.createTaxonUnit(this._onTaxonUnitLoaded, this);
        }

        this.getByName("save").on("click", this._onSave, this);
        this.getByName("cancel").on("click", this._onCancel, this);
    }

    _onTaxonUnitLoaded(taxonUnit) {
        this._taxonUnit = taxonUnit;
        this._form.setObject(taxonUnit);
    }

    _onSave(ev) {
        if (this._form.isValid()) {
            AtlasService.saveTaxonUnit(this._form.getObject(this._taxonUnit), () => WinMain.back());
        }
    }

    _onCancel(ev) {
        WinMain.back();
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

    toString() {
        return "com.kidscademy.page.TaxonUnitForm";
    }
};

WinMain.createPage(com.kidscademy.page.TaxonUnitForm);
