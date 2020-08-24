$package("com.kidscademy.page");

com.kidscademy.page.FeaturesMetaPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/features-meta");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("create-feature-meta", this._onCreate, this);
        this._sidebar.on("translate-features", this._onTranslateFeatures, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit", this._onEdit, this);
        this._contextMenu.on("remove", this._onRemove, this);

        this._tableView = this.getByCssClass("features-list");
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

    _onCreate() {
        WinMain.assign("@link/feature-meta-form");
    }

    _onTranslateFeatures() {
        WinMain.assign("@link/features-translator");
    }

    _onEdit(featureMetaView) {
        const featureMeta = featureMetaView.getUserData();
        WinMain.assign("@link/feature-meta-form", { feature: featureMeta.id });
    }

    _onRemove(featureMetaView) {
        js.ua.System.confirm("@string/confirm-feature-meta-remove", ok => {
            if (ok) {
                const featureMeta = featureMetaView.getUserData();
                AtlasService.removeFeatureMeta(featureMeta.id, () => featureMetaView.remove());
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
        const featureView = ev.target.getParentByCssClass("feature-view");
        if (featureView != null) {
            if (this._contextMenu != null) {
                const action = this._contextMenu.getDefaultAction(ev);
                if (action != null) {
                    action.listener.call(action.scope, featureView);
                }
            }
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
