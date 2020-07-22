$package("com.kidscademy.collection");

com.kidscademy.collection.LinkSourcesControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._linksListView = this.getByCssClass("link-sources");
        this._linksListView.on("click", this._onLinksListViewClick, this);
        this._linksListView.on("dragstart", this._onDragStart, this);
        this._linksListView.on("dragover", this._onDragOver, this);
        this._linksListView.on("drop", this._onDrop, this);

        this._selectedLinkView = null;

        this._editor = this.getByCssClass("editor");
        this._linkForm = this.getByCssClass("link-form");
        this._candidatesListView = this.getByCssClass("candidates");
        this._candidatesListView.on("click", this._onCandidatesClick, this);

        this._itemSelect = WinMain.page.getByClass(com.kidscademy.ItemSelect);
        this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
        this._actions.showOnly("add", "clone", "remove-all");
    }

    // --------------------------------------------------------------------------------------------
    // CONTROL INTERFACE

    setValue(links) {
        this._linksListView.setObject(links);
        return this;
    }

    getValue() {
        return this._linksListView.getChildren().map(linkView => linkView.getUserData());
    }

    isValid() {
        return this.hasCssClass("optional") || this._linksListView.getChildrenCount() > 0;
    }

    // --------------------------------------------------------------------------------------------
    // ACTION HANDLERS

    _onAdd() {
        this._editor.select("candidates");
        const excludeIds = this._linksListView.getChildren().map(linkView => linkView.getUserData().externalSource.id);
        AtlasService.getExternalSourceCandidates(excludeIds, sources => this._candidatesListView.setObject(sources));
        this._actions.show("close").hide("remove-all");
    }

    _onClone() {
        AtlasService.getCollections(collections => this._itemSelect.load(collections));
        this._itemSelect.open(collection => {
            AtlasService.getCollectionLinkSources(collection.id, links => this.setValue(links));
        });
    }

    _onBrowse() {
        const link = this._selectedLinkView.getUserData();
        this._browse(link.externalSource.home);
    }

    _onRemove() {
        this._selectedLinkView.remove();
        this._onClose();
    }

    _onDone() {
        if (this._editor.isSelected("link-form")) {
            // editor is opened for source link apis selection
            const link = this._linkForm.getObject();
            this._selectedLinkView.setObject(link);
        }
        this._onClose();
    }

    _onRemoveAll() {
        js.ua.System.confirm("@string/confirm-all-link-sources-remove", ok => {
            if (ok) {
                this._linksListView.removeChildren();
            }
        });
    }

    _onClose() {
        this._editor.hide();
        this._actions.showOnly("add", "clone", "remove-all");
    }

    // --------------------------------------------------------------------------------------------
    // DRAG AND DROP

    _onDragStart(ev) {
        const li = ev.target.getParentByTag("li");
        ev.setData({
            index: li.getChildIndex()
        });
    }

    _onDragOver(ev) {
        ev.prevent();
    }

    _onDrop(ev) {
        ev.prevent();
        const data = ev.getData();
        const sourceElement = this._linksListView.getByIndex(data.index);
        const targetElement = ev.target.getParentByTag("li");

        if (targetElement == null) {
            return;
        }
        if (targetElement === sourceElement) {
            return;
        }

        if (ev.ctrlKey) {
            const siblingElement = targetElement.getNextSibling();
            if (siblingElement == null) {
                targetElement.getParent().addChild(sourceElement);
            }
            else {
                siblingElement.insertBefore(sourceElement);
            }
        }
        else {
            targetElement.insertBefore(sourceElement);
        }
    }

    // --------------------------------------------------------------------------------------------

    _onLinksListViewClick(ev) {
        this._selectedLinkView = ev.target.getParentByTag("li");
        if (this._selectedLinkView == null) {
            return;
        }
        const link = this._selectedLinkView.getUserData();
        if (ev.ctrlKey) {
            this._browse(link.externalSource.home);
            return;
        }

        this._editor.select("link-form");
        this._actions.show("browse", "done", "remove", "close").hide("remove-all");

        const apisSelect = this._linkForm._getControl("apis");
        apisSelect.load(link.externalSource.apis, () => this._linkForm.setObject(link));
    }

    _onCandidatesClick(ev) {
        const externalSourceView = ev.target.getParentByTag("li");
        if (externalSourceView != null) {
            const externalSource = externalSourceView.getUserData();
            const link = {
                atlasCollection: { id: WinMain.page.getCollectionId() },
                externalSource: externalSource,
                apis: externalSource.apis,
                display: externalSource.display,
                iconSrc: externalSource.iconSrc
            }
            this._linksListView.addObject(link);
            externalSourceView.remove();
        }
    }

    _browse(homeURL) {
        window.open(homeURL);
    }

    toString() {
        return "com.kidscademy.collection.FeaturesMetaControl";
    }
};
