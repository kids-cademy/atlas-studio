$package("com.kidscademy.workspace");

com.kidscademy.workspace.FeaturesMetaList = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._page = null;

        this._featureMetaForm = null;

        this._tableView = this.getByCssClass("table-view");
        this._tableView.on("click", this._onClick, this);

        AtlasService.getFeatureMeta(featuresMeta => this._tableView.setObject(featuresMeta));

        this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);
        this.on("contextmenu", this._onContextMenu, this);
    }

    onCreate(page) {
        this._page = page;
        this._featureMetaForm = this._page.getByClass(com.kidscademy.workspace.FeatureMetaForm);
    }

    onDestroy(page) {

    }

    // --------------------------------------------------------------------------------------------
    // CONTEXT MENU HANDLERS

    _onCreateFeature() {
        AtlasService.createFeatureMeta(featureMeta => {
            this._page.selectView("feature-meta-form");
            this._featureMetaForm.open(featureMeta, featureMeta => {
                this._page.selectView("features-meta-list");
                if (featureMeta != null) {
                    AtlasService.saveFeatureMeta(featureMeta, featureMeta => this._tableView.addObject(featureMeta));
                }
            });
        });
    }

    _onEditFeature(featureMetaView) {
        this._page.selectView("feature-meta-form");
        this._featureMetaForm.open(featureMetaView.getUserData(), featureMeta => {
            this._page.selectView("features-meta-list");
            if (featureMeta != null) {
                AtlasService.saveFeatureMeta(featureMeta, featureMeta => featureMetaView.setObject(featureMeta));
            }
        });
    }

    _onRemoveFeature(featureMetaView) {
        js.ua.System.confirm("@string/confirm-feature-meta-remove", ok => {
            if (ok) {
                const featureMeta = featureMetaView.getUserData();
                AtlasService.removeFeatureMeta(featureMeta.id, () => featureMetaView.remove());
            }
        });
    }

    // --------------------------------------------------------------------------------------------

    _onClick(ev) {
        const featureMetaView = ev.target.getParentByCssClass("feature-view");
        if (featureMetaView != null) {
            this._onEditFeature(featureMetaView);
        }
    }

    _onContextMenu(ev) {
        ev.halt();
        this._contextMenu.open(ev.target.getParentByCssClass("feature-view"));
    }

    toString() {
        return "com.kidscademy.workspace.FeaturesMetaList";
    }
};
