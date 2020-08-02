$package("com.kidscademy.form");

com.kidscademy.form.DescriptionControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._descriptionMeta = null;

		this._textEditors = this.getByCssClass("text-editors");
		this._tabsView = this.getByCssClass("tabs-view")
		this._tabsView.on("click", this._onTabsClick, this);
		this._tabsView.on("dragstart", this._onTabsDragStart, this);
		this._tabsView.on("dragover", this._onTabsDragOver, this);
		this._tabsView.on("drop", this._onTabsDrop, this);
		this._updateEditorIndex(-1);

		this._editorControls = this.getByCssClass("editor-controls");
		this._sectionNameInput = this.getByName("section-name");

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
		this._operation = null;
	}

	/**
	 * Handler called by atlas object form just after object loaded. Note that this callback is 
	 * invoked after {@link #setValue(Object)}.
	 */
	onCreate(formPage) {
		super.onCreate(formPage);
		this._descriptionMeta = formPage.getCollection().descriptionMeta;
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(description) {
		if (description == null) {
			this._textarea.reset();
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

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	/**
	 * Import description from a provider link. 
	 * 
	 * Get from parent page all links that provides description. Alert if there is none. 
	 * If there are more than one link display them and let user choose one.
	 */
	_onImport() {
		this._formPage.importFromLink("description", description => {
			for (let section in description) {
				let textEditor = this._getTextEditorByName(section);
				if (textEditor == null) {
					textEditor = this._addTextEditor(section);
				}
				textEditor.setValue(description[section]);
				this._updateEditorIndex(textEditor.getChildIndex());
			}
		});
	}

	_onAddAllSections() {
		const sectionNames = this._descriptionMeta.map(meta => meta.name);
		sectionNames.forEach(sectionName => this._addTextEditor(sectionName));
		this._updateEditorIndex(0);
	}

	_onAddSection() {
		this._operation = "ADD";
		this._editorControls.show();
		this._sectionNameInput.focus();
	}

	_onRenameSection() {
		this._operation = "RENAME";
		this._editorControls.show();
		this._sectionNameInput.setValue(this._getActiveTabView().getText()).focus();
	}

	_onDoneSection() {
		if (!this._sectionNameInput.isValid()) {
			return;
		}
		switch (this._operation) {
			case "ADD":
				const textEditor = this._addTextEditor(this._sectionNameInput.getValue());
				this._updateEditorIndex(textEditor.getChildIndex());
				break;

			case "RENAME":
				this._getActiveTabView().setText(this._sectionNameInput.getValue());
				break;
		}
		this._sectionNameInput.reset();
		this._editorControls.hide();
	}

	_onFormatLines() {
		const textEditor = this._textEditors.getByIndex(this._getActiveTabIndex());
		AtlasService.formatLines(textEditor.getValue(), "\r\n\r\n", text => textEditor.setValue(text));
	}

	_onCopyToClipboard() {
		const textEditor = this._textEditors.getByIndex(this._getActiveTabIndex());
		textEditor.copyToClipboard();
	}

	_onRemoveSection() {
		const activeTab = this._getActiveTabView();
		const editorIndex = activeTab.getChildIndex();
		const textEditor = this._textEditors.getByIndex(editorIndex);
		if (textEditor.getValue() != null) {
			js.ua.System.alert("@string/alert-not-empty-section");
			return;
		}

		textEditor.remove();
		activeTab.remove();
		this._updateEditorIndex(0);
	}

	_onClose() {
		this._editorControls.hide();
		this._linksSelect.close();
	}

	// --------------------------------------------------------------------------------------------
	//

	_onTabsClick(ev) {
		const tabElement = ev.target.getParentByCssClass("tab");
		if (tabElement != null) {
			this._updateEditorIndex(tabElement.getChildIndex());
			this._getActiveTextEditor().focus();
		}
	}

	// --------------------------------------------------------------------------------------------
	// TABS DRAG AND DROP

	_onTabsDragStart(ev) {
		ev.setData({ index: ev.target.getChildIndex() });
	}

	_onTabsDragOver(ev) {
		ev.prevent();
	}

	_onTabsDrop(ev) {
		ev.prevent();

		const sourceData = ev.getData();
		const sourceIndex = sourceData.index;
		const targetIndex = ev.target.getChildIndex();

		function move(view, targetIndex, sourceIndex, ctrlKey) {
			const sourceElement = view.getByIndex(sourceIndex);
			const targetElement = view.getByIndex(targetIndex);

			if (!ctrlKey) {
				targetElement.insertBefore(sourceElement);
				return;
			}

			const siblingElement = targetElement.getNextSibling();
			if (siblingElement == null) {
				targetElement.getParent().addChild(sourceElement);
			}
			else {
				siblingElement.insertBefore(sourceElement);
			}
		}

		move(this._tabsView, targetIndex, sourceIndex, ev.ctrlKey);
		move(this._textEditors, targetIndex, sourceIndex, ev.ctrlKey);
	}

	// --------------------------------------------------------------------------------------------
	// HELPERS

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

	_addTextEditor(sectionName) {
		this._tabsView.addObject(sectionName);
		this._textEditors.addObject("");
		return this._textEditors.getLastChild();
	}

	_getActiveTextEditor() {
		return this._textEditors.getByIndex(this._getActiveTabIndex());
	}

	_getTextEditorByName(tabName) {
		const elist = this._tabsView.getChildren();
		for (let i = 0; i < elist.size(); ++i) {
			const tabView = elist.item(i);
			if (tabView.getText() === tabName) {
				return tabView;
			}
		}
		return null;
	}

	_getActiveTabView() {
		return this._tabsView.getByCssClass("active");
	}

	_getActiveTabIndex() {
		return this._getActiveTabView().getChildIndex();
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.DescriptionControl";
	}
};
