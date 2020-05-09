$package("com.kidscademy.page");

com.kidscademy.page.CollectionsPage = class extends com.kidscademy.Page {
    constructor() {
        super();

        this._sidebar.setTitle("@string/collections");
        this._sidebar.on("collections", this._onCollections, this);
        this._sidebar.on("links-meta", this._onLinksMeta, this);
        this._sidebar.on("features-meta", this._onFeaturesMeta, this);
        this._sidebar.on("create-collection", this._onCreateCollection, this);

        this._collectionsMenu = this.getByCssClass("collections-menu");
        this._collectionsMenu.on("edit-collection", this._onEditCollection, this);
        this._collectionsMenu.on("remove-collection", this._onRemoveCollection, this);
        this._collectionsMenu.on("manage-objects", this._onManageObjects, this);

        this._collectionsView = this.getByCssClass("collections-view");
        this._collectionsView.setContextMenu(this._collectionsMenu);

        this._draftsMenu = this.getByCssClass("drafts-menu");
        // drafts context menu handlers are implemented by com.kidscademy.ObjectsContextMenu mixin
        this._draftsMenu.on("edit-object", this._onEditObject, this);
        this._draftsMenu.on("preview-object", this._onPreviewObject, this);

        this._draftsSection = this.getByCssClass("drafts-section");
        this._draftsView = this.getByCssClass("drafts-view");
        this._draftsView.setContextMenu(this._draftsMenu);
        this._collectionsView.bindSlaveList(this._draftsView);

        this._objectsMenu = this.getByCssClass("objects-menu");
        // objects context menu handlers, less open-collection, are implemented by com.kidscademy.ObjectsContextMenu mixin
        this._objectsMenu.on("edit-object", this._onEditObject, this);
        this._objectsMenu.on("preview-object", this._onPreviewObject, this);
        this._objectsMenu.on("open-collection", this._onOpenCollection, this);

        this._objectsView = this.getByCssClass("objects-view");
        this._objectsView.setContextMenu(this._objectsMenu);
        this._collectionsView.bindSlaveList(this._objectsView);

        this._itemSelect = this.getByClass(com.kidscademy.ItemSelect);

        this._collectionsView.resetTimestamp();
        AtlasService.getCollections(collections => this._collectionsView.setObject(collections));
        AtlasService.getRecentUsedAtlasObjects(objects => this._objectsView.setObject(objects).show());

        const DRAFT_ATTR_REX = /^atlas-object-draft-(\d+)$/;
        this.findContextAttr(DRAFT_ATTR_REX, object => this._draftsView.addObject(object));
        this._draftsSection.show(!this._draftsView.isEmpty());
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

    _onCreateCollection() {
        WinMain.assign("@link/collection-form");
    }

    _onEditCollection(collectionView) {
        const collection = collectionView.getUserData();
        WinMain.assign("@link/collection-form", { collection: collection.id });
    }

    _onRemoveCollection(collectionView) {
        js.ua.System.confirm("@string/confirm-collection-remove", ok => {
            if (ok) {
                const collection = collectionView.getUserData();
                AtlasService.removeAtlasCollection(collection.id, () => collectionView.remove());
            }
        });
    }

    _onManageObjects(collectionView) {
        const collection = collectionView.getUserData();
        WinMain.assign("@link/collection", { collection: collection.id });
    }

    _onOpenCollection(objectView) {
        const object = objectView.getUserData();
        WinMain.assign("@link/collection", { collection: object.collection.id });
    }

    toString() {
        return "com.kidscademy.page.CollectionsPage";
    }
};

Object.assign(com.kidscademy.page.CollectionsPage.prototype, com.kidscademy.ObjectsContextMenu);
WinMain.createPage(com.kidscademy.page.CollectionsPage);
