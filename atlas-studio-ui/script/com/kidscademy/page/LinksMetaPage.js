$package("com.kidscademy.page");

com.kidscademy.page.LinksMetaPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/links-meta");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("links-meta", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("create-link-meta", this._onCreateLinkMeta, this);

        this._contextMenu = this.getByCssClass("context-menu");
        this._contextMenu.on("edit-link-meta", this._onEditLinkMeta, this);
        this._contextMenu.on("remove-link-meta", this._onRemoveLinkMeta, this);
        this._contextMenu.on("display-references", this._onDisplayReferences, this);

        this._linksMetaView = this.getByClass(com.kidscademy.ListView);
        this._linksMetaView.setContextMenu(this._contextMenu);

        AtlasService.getLinksMeta(linksMeta => this._linksMetaView.setObject(linksMeta));
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

    // --------------------------------------------------------------------------------------------
    
    _onCreateLinkMeta() {
        WinMain.assign("@link/link-meta-form");
    }

    _onEditLinkMeta(linkMetaView) {
        const linkMeta = linkMetaView.getUserData();
        WinMain.assign("@link/link-meta-form", { link: linkMeta.id });
    }

    _onRemoveLinkMeta(linkMetaView) {
        js.ua.System.confirm("@string/confirm-link-meta-remove", ok => {
            if (ok) {
                const linkMeta = linkMetaView.getUserData();
                AtlasService.removeLinkMeta(linkMeta.id, () => linkMetaView.remove());
            }
        });
    }

    _onDisplayReferences(linkMetaView) {
        const linkMeta = linkMetaView.getUserData();
        WinMain.assign("@link/link-references", { link: linkMeta.id });
    }

    toString() {
        return "com.kidscademy.page.LinksMetaPage";
    }
};

WinMain.createPage(com.kidscademy.page.LinksMetaPage);
