$package("com.kidscademy");

com.kidscademy.ListView = class extends js.dom.Element {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        /**
         * Optional reference to external context menu, related to this list items. It is diplayed on right click or long tap on a list item. 
         * This reference is null if not set in which case right click or long tap event is delegated to default action.
         * @type {com.kidscademy.ContextMenu}
         */
        this._contextMenu = null;

        this._timestamp = 0;
        this._loadingInfoView = this.getByCssClass("loading-info");

        this._listControl = this.getByCssClass("list-control");
        this._listControl.setLayout("icons");
        this._listControl.on("click", this._onListClick, this);

        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

        this.on("contextmenu", this._onContextMenu, this);
    }

    setContextMenu(contextMenu) {
        this._contextMenu = contextMenu;
    }

    resetTimestamp() {
        this._timestamp = Date.now();
    }

    setObject(objects) {
        if (this._timestamp !== 0) {
            this._loadingInfoView.setObject({
                objectsCount: objects.length,
                ellapsedTime: Date.now() - this._timestamp
            }).show();
            this._timestamp = 0;
        }
        else {
            this._loadingInfoView.hide();
        }

        this._listControl.setObject(objects).show();
        return this;
    }

    // --------------------------------------------------------------------------------------------
    // FILTER ACTION HANDLERS

    _onLoadItems() {
    }

    _onResetFilter() {
    }

    _onIconsView() {
        this._listControl.setLayout("icons");
    }

    _onTilesView() {
        this._listControl.setLayout("tiles");
    }

    _onDetailsView() {
        this._listControl.setLayout("details");
    }

    _onCardsView() {
        this._listControl.setLayout("cards");
    }

    // --------------------------------------------------------------------------------------------

    _onListClick(ev) {
        const item = ev.target.getParentByTag("li");
        if (item != null) {
            if (this._contextMenu != null) {
                const action = this._contextMenu.getDefaultAction();
                if (action != null) {
                    action.listener.call(action.scope, item);
                }
            }
        }
    }

    _onContextMenu(ev) {
        if (this._contextMenu != null) {
            ev.halt();
            this._contextMenu.open(ev.target.getParentByTag("li"));
        }
    }

    toString() {
        return "com.kidscademy.ListView";
    }
};
