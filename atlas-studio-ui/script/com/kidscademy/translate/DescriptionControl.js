$package("com.kidscademy.translate");

com.kidscademy.translate.DescriptionControl = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._textEditors = this.getByCssClass("text-editors");
        this._tabsView = this.getByCssClass("tabs-view")
        this._tabsView.on("click", this._onTabsClick, this);
    }

    setValue(description) {
        if (description == null) {
            return;
        }

        const parser = new DOMParser();
        const document = new js.dom.Document(parser.parseFromString(description, "text/xml"));

        const textSections = [];
        const tabs = [];
        const sectionElements = document.findByTag("section");
        for (let i = 0; i < sectionElements.size(); ++i) {
            const htm2md = new com.kidscademy.Htm2Md(sectionElements.item(i));
            textSections.push(htm2md.converter());
            tabs.push(htm2md.sectionName());
        }

        this._textEditors.setObject(textSections);
        this._tabsView.setObject(tabs);
        this._updateEditorIndex(0);
    }

    getValue() {
        const textEditors = this._textEditors.getChildren();
        const tabsView = this._tabsView.getChildren();

        var text = "";
        text += "<text>";
        for (let i = 0; i < textEditors.size(); ++i) {
            text += `<section name="${tabsView.item(i).getText()}">`;
            const markdown = textEditors.item(i).getValue();
            if (markdown != null) {
                const md2htm = new com.kidscademy.Md2Htm(markdown);
                text += md2htm.convert();
            }
            text += "</section>";
        }
        text += "</text>";
        return text;
    }

    isValid() {
        return true;
    }

    _onTabsClick(ev) {
        const tabElement = ev.target.getParentByCssClass("tab");
        if (tabElement != null) {
            this._updateEditorIndex(tabElement.getChildIndex());
            this._getActiveTextEditor().focus();
        }
    }

	_updateEditorIndex(activeEditorIndex) {
		const editors = this._textEditors.getChildren();
		for (let i = 0; i < editors.size(); ++i) {
			editors.item(i).addCssClass("hidden", i !== activeEditorIndex);
		}

		const tabs = this._tabsView.getChildren();
		for (let i = 0; i < tabs.size(); ++i) {
			tabs.item(i).show().addCssClass("active", i === activeEditorIndex);
		}
    }
    
	_getActiveTextEditor() {
		return this._textEditors.getByIndex(this._getActiveTabIndex());
	}

	_getActiveTabView() {
		return this._tabsView.getByCssClass("active");
	}

	_getActiveTabIndex() {
		return this._getActiveTabView().getChildIndex();
	}

    toString() {
        return "com.kidscademy.translate.DescriptionControl";
    }
};
