$package("com.kidscademy.page");

com.kidscademy.page.LinkReferencesPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.showObject();
        this._sidebar.on("links-meta", this._onLinksMeta, this);

        const linkMetaId = Number(WinMain.url.parameters.link);
        AtlasService.getLinkMeta(linkMetaId, this._onLinkMetaLoaded, this);
    }

    _onLinkMetaLoaded(linkMeta) {
        this._sidebar.setObject(linkMeta);
        this._sidebar.setTitle("@string/link-references");
    }

    _onLinksMeta() {
        WinMain.assign("@link/links-meta");
    }

    toString() {
        return "com.kidscademy.page.LinkReferencesPage";
    }
};

WinMain.createPage(com.kidscademy.page.LinkReferencesPage);
