$package("com.kidscademy.workspace");

com.kidscademy.workspace.CollectionsList = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        /**
         * Parent workspace page.
         * @type {com.kidscademy.page.WorkspacePage}
         */
        this._page = null;

        this.on("contextmenu", this._onContextMenu, this);

        this._listView = this.getByCssClass("list-view");
        this._listView.on("click", this._onListClick, this);

        this._listType = new com.kidscademy.CssFlags(this._listView, "icons", "cards");
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._contextMenu = this.getByClass(com.kidscademy.ContextMenu).bind(this);
    }

    onCreate(page) {
        this._page = page;
        this._collectionForm = this._page.getByClass(com.kidscademy.workspace.CollectionForm);

        this._listType.set(page.getPageAttr("collection-list-type"));
        this._actions.fire("load-items");
    }

    onDestroy(page) {
        page.setPageAttr("collection-list-type", this._listType.get());
    }

    addObject(collection) {
        this._listView.addObject(collection);
    }

    // --------------------------------------------------------------------------------------------
    // FILTER ACTION HANDLERS

    _onLoadItems() {
        AtlasService.getCollections(collections => this._listView.setObject(collections).show());
    }

    _onResetFilter() {

    }

    _onIconsView() {
        this._listType.set("icons");
    }

    _onCardsView() {
        this._listType.set("cards");
    }

    // --------------------------------------------------------------------------------------------
    // CONTEXT MENU HANDLERS

    _onCreateCollection() {
        AtlasService.createAtlasCollection(collection => {
            this._page.selectView("collection-form");
            this._collectionForm.open(collection, collection => {
                this._page.selectView("collections-list");
                if (collection != null) {
                    AtlasService.saveAtlasCollection(collection, collection => this._listView.addObject(collection));
                }
            });
        });
    }

    _onEditCollection(collectionView) {
        this._page.selectView("collection-form");
        this._collectionForm.open(collectionView.getUserData(), collection => {
            this._page.selectView("collections-list");
            if (collection != null) {
                AtlasService.saveAtlasCollection(collection, collection => collectionView.setObject(collection));
            }
        });
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
        this._openCollectionPage(collectionView);
    }

    // --------------------------------------------------------------------------------------------

    _onListClick(ev) {
        const li = ev.target.getParentByTag("li");
        if (li != null) {
            this._openCollectionPage(li);
        }
    }

    _openCollectionPage(collectionView) {
        const collection = collectionView.getUserData();
        // store selected collection on global context to make it accessible to all descendant pages
        this._page.setContextAttr("collection", collection);
        WinMain.assign("@link/collection", { id: collection.id });
    }

    _onContextMenu(ev) {
        ev.halt();
        this._contextMenu.open(ev.target.getParentByTag("li"));
    }

    toString() {
        return "com.kidscademy.workspace.CollectionsList";
    }
};
