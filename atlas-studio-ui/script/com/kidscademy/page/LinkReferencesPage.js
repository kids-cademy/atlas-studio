$package("com.kidscademy.page");

com.kidscademy.page.LinkReferencesPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.showObject();
        this._sidebar.on("external-sources", this._onExternalSources, this);

        const externalSourceId = Number(WinMain.url.parameters.link);
        AtlasService.getExternalSource(externalSourceId, this._onExternalSourceLoaded, this);
    }

    _onExternalSourceLoaded(externalSource) {
        this._sidebar.setObject(externalSource);
        this._sidebar.setTitle("@string/link-references");
    }

    _onExternalSources() {
        WinMain.assign("@link/external-sources");
    }

    toString() {
        return "com.kidscademy.page.LinkReferencesPage";
    }
};

WinMain.createPage(com.kidscademy.page.LinkReferencesPage);
