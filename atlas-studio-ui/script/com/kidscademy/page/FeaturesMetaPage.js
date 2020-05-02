$package("com.kidscademy.page");

com.kidscademy.page.FeaturesMetaPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/features-meta");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("links-meta", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("create-feature-meta", this._onCreateFeatureMeta, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit-feature-meta", this._onEditFeatureMeta, this);
        this._contextMenu.on("remove-feature-meta", this._onRemoveFeatureMeta, this);

        this._tableView = this.getByCssClass("table-view");
        this._tableView.on("click", this._onClick, this);
        this._tableView.on("contextmenu", this._onContextMenu, this);

        AtlasService.getFeaturesMeta(featuresMeta => this._tableView.setObject(featuresMeta));
    }

    _onCollections() {
        WinMain.assign("@link/collections");
    }

    _onLinksMeta() {
        WinMain.assign("@link/links-meta");
    }

    _onFeaturesMeta() {
        WinMain.assign("@link/features-meta");
    }

    _onCreateFeatureMeta() {
        WinMain.assign("@link/feature-meta-form");
    }

    _onEditFeatureMeta(featureMetaView) {
        const featureMeta = featureMetaView.getUserData();
        WinMain.assign("@link/feature-meta-form", { feature: featureMeta.id });
    }

    _onRemoveFeatureMeta(featureMetaView) {
        js.ua.System.confirm("@string/confirm-feature-meta-remove", ok => {
            if (ok) {
                const featureMeta = featureMetaView.getUserData();
                AtlasService.removeFeatureMeta(featureMeta.id, () => featureMetaView.remove());
            }
        });
    }

    // --------------------------------------------------------------------------------------------

    _onClick(ev) {
        const featureView = ev.target.getParentByCssClass("feature-view");
        if (featureView != null) {
            this._onEditFeatureMeta(featureView);
        }
    }

    _onContextMenu(ev) {
        const featureView = ev.target.getParentByCssClass("feature-view");
        if (featureView != null) {
            ev.halt();
            this._contextMenu.open(ev.target.getParentByCssClass("feature-view"));
        }
    }

    toString() {
        return "com.kidscademy.page.FeaturesMetaPage";
    }
};

WinMain.createPage(com.kidscademy.page.FeaturesMetaPage);
