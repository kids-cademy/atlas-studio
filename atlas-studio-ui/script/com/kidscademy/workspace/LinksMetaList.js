$package("com.kidscademy.workspace");

com.kidscademy.workspace.LinksMetaList = class extends js.dom.Element {
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
        this._linkMetaForm = this._page.getByClass(com.kidscademy.workspace.LinkMetaForm);

        this._listType.set(page.getPageAttr("link-meta-list-type"));
        this._actions.fire("load-items");
    }

    onDestroy(page) {
        page.setPageAttr("link-meta-list-type", this._listType.get());
    }

    addObject(linkMeta) {
        this._listView.addObject(linkMeta);
    }

    // --------------------------------------------------------------------------------------------
    // FILTER ACTION HANDLERS

    _onLoadItems() {
        AtlasService.getLinksMeta(linksMeta => this._listView.setObject(linksMeta).show());
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

    _onCreateLinkMeta() {
        AtlasService.createLinkMeta(linkMeta => {
            this._page.selectView("link-meta-form");
            this._linkMetaForm.open(linkMeta, linkMeta => {
                this._page.selectView("links-meta-list");
                if (linkMeta != null) {
                    AtlasService.saveLinkMeta(linkMeta, linkMeta => this._listView.addObject(linkMeta));
                }
            });
        });
    }

    _onEditLinkMeta(linkMetaView) {
        this._page.selectView("link-meta-form");
        this._linkMetaForm.open(linkMetaView.getUserData(), linkMeta => {
            this._page.selectView("links-meta-list");
            if (linkMeta != null) {
                AtlasService.saveLinkMeta(linkMeta, linkMeta => linkMetaView.setObject(linkMeta));
            }
        });
    }

    _onRemoveLinkMeta(linkMetaView) {
        js.ua.System.confirm("@string/confirm-link-meta-remove", ok => {
            if (ok) {
                const linkMeta = linkMetaView.getUserData();
                AtlasService.removeLinkMeta(linkMeta.id, () => linkMetaView.remove());
            }
        });
    }

    // --------------------------------------------------------------------------------------------

    _onListClick(ev) {
        const li = ev.target.getParentByTag("li");
        if (li != null) {
            this._onEditLinkMeta(li);
        }
    }

    _onContextMenu(ev) {
        ev.halt();
        this._contextMenu.open(ev.target.getParentByTag("li"));
    }

    toString() {
        return "com.kidscademy.workspace.LinksMetaList";
    }
};
