$package("com.kidscademy.page");

com.kidscademy.page.ExternalSourcesPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/external-sources");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("external-sources", this._onExternalSources, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("create-external-source", this._onCreateExternalSource, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit-external-source", this._onEditExternalSource, this);
        this._contextMenu.on("remove-external-source", this._onRemoveExternalSource, this);
        this._contextMenu.on("display-references", this._onDisplayReferences, this);

        this._externalSourcesView = this.getByClass(com.kidscademy.ListView);
        this._externalSourcesView.setContextMenu(this._contextMenu);

        AtlasService.getExternalSources(externalSources => this._externalSourcesView.setObject(externalSources));
    }

    _onCollections() {
        WinMain.assign("@link/collections");
    }

    _onExternalSources() {
        WinMain.assign("@link/external-sources");
    }

    _onFeaturesMeta() {
        WinMain.assign("@link/features-meta");
    }
    
    _onCreateExternalSource() {
        WinMain.assign("@link/external-source-form");
    }

    _onEditExternalSource(externalSourceView) {
        const externalSource = externalSourceView.getUserData();
        WinMain.assign("@link/external-source-form", { link: externalSource.id });
    }

    _onRemoveExternalSource(externalSourceView) {
        js.ua.System.confirm("@string/confirm-external-source-remove", ok => {
            if (ok) {
                const externalSource = externalSourceView.getUserData();
                AtlasService.removeExternalSource(externalSource.id, () => externalSourceView.remove());
            }
        });
    }

    _onDisplayReferences(externalSourceView) {
        const externalSource = externalSourceView.getUserData();
        WinMain.assign("@link/link-references", { link: externalSource.id });
    }

    toString() {
        return "com.kidscademy.page.ExternalSourcesPage";
    }
};

WinMain.createPage(com.kidscademy.page.ExternalSourcesPage);
