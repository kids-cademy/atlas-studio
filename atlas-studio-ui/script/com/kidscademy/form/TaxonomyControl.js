$package("com.kidscademy.form");

/**
 * User interface control used to manage atlas object taxonomy. User interface layout of this control
 * adapts to taxonomy class defined by atlas collection.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.form.TaxonomyControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * Atlas object - in fact its lite version, atlas item, for which this taxonomy is applied.
		 * @type {Object}
		 */
		this._atlasItem = null;

		/**
		 * Taxonomy meta reference on parent atlas collection.
		 * @type {com.kidscademy.form.TaxonomyControl.Meta}
		 */
		this._meta = null;

		/**
		 * Taxon meta editor.
		 * @type {com.kidscademy.form.TaxonomyControl.MetaEditor}
		 */
		this._metaEditor = this.getByCss(".editor.meta");

		/**
		 * Taxon value editor.
		 * @type {com.kidscademy.form.TaxonomyControl.ValueEditor}
		 */
		this._valueEditor = this.getByCss(".editor.value");

		/**
		 * Array index for taxon object currently on edit. This index identify taxon object from atlas object {@link #_taxonomy}.
		 * @type {Number}
		 */
		this._taxonEditIndex = -1;

		/**
		 * Batch edit mode is active. In this mode all taxon values are edited in sequence starting with first till all complete.
		 * Moving to next taxon is triggered by enter key or click on 'done' action.
		 * @type {Boolean}
		 */
		this._batchEdit = false;

		/**
		 * This view displays current value(s) of the atlas object taxonomy.
		 * @type {js.dom.Element}
		 */
		this._taxonomyView = this.getByCssClass("taxonomy-view");
		this._taxonomyView.on("click", this._onClick, this);

		/**
		 * This control actions handler.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
	}

	/**
	 * Handler called by atlas object form just after object loaded. Note that this callback is 
	 * invoked after {@link #setValue(Object)}.
	 */
	onCreate(formPage) {
		super.onCreate(formPage);
		this._meta = new com.kidscademy.form.TaxonomyControl.Meta(formPage.getCollection().taxonomyMeta);
		this._metaEditor.bind(this._meta);
	}

	/**
	 * Control value setter. It gets atlas object taxonomy that is an array of taxon objects.
	 * @param {Array} taxonomy atlas object taxonomy.
	 * @return {com.kidscademy.form.TaxonomyControl} this object pointer.
	 */
	setValue(taxonomy) {
		this._taxonomyView.setObject(taxonomy);
		this._updateActions();
		return this;
	}

	/**
	 * Get atlas object taxonomy update from user interface. Taxonomy is an array of taxon objects.
	 * @returns {Array} atlas object taxonomy.
	 */
	getValue() {
		return this._taxonomyView.getChildren().map(taxonView => taxonView.getUserData());
	}

	/**
	 * Test if taxonomy values are filled.
	 * @returns {Boolean} return true if taxonomy all values are filled.
	 */
	isValid() {
		var valid = true;
		this._taxonomyView.getChildren().forEach(taxonView => {
			if (valid && !taxonView.getByCssClass("value").getText()) {
				valid = false;
			}
		});
		return valid;
	}

	/**
	 * Click event handler uses event delegation pattern to detect taxon object selected by user. This handler
	 * opens taxon value editor and initialize name and value controls.
	 * @param {js.event.Event} ev mouse click event. 
	 */
	_onClick(ev) {
		const item = ev.target.getParentByCssClass("item");
		if (item) {
			this._taxonEditIndex = item.getChildIndex();
			this._openEditor();
			this._actions.hide("batch-edit");
		}
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onImport() {
		this._actions.showOnly("close");
		this._fireEvent("input");
		this._formPage.importFromLink("taxonomy", taxonomy => this.setValue(taxonomy));
	}

	/**
	 * Handle 'ADD' action. If current taxonomy is empty load default from collection taxonomy meta. If current taxonomy 
	 * is already loaded append a new taxon.
	 */
	_onAdd() {
		if (this._taxonomyView.hasChildren()) {
			// taxon name select options should display only not already used names
			const names = this._taxonomyView.getChildren().map(taxonView => taxonView.getUserData().name);
			this._metaEditor.open(names);
			this._actions.showOnly("done", "close");
			return;
		}

		this.setValue(this._meta.getTaxonomyTemplate());
		if (this._taxonomyView.getChildrenCount() == 1) {
			this._taxonEditIndex = 0;
			this._openEditor();
		}
	}

	_onLoad() {
		const objectName = this._formPage.getObject().name;
		if (!objectName) {
			js.ua.System.alert("@string/alert-object-no-name");
			return;
		}
		AtlasService.loadAtlasObjectTaxonomy(objectName, this.setValue, this);
	}

	_onBatchEdit() {
		this._taxonEditIndex = 0;
		this._batchEdit = true;
		this._openEditor();
		this._actions.hide("remove");
	}

	_onDone() {
		if (this._batchEdit) {
			this._updateTaxonomyView();
			if (++this._taxonEditIndex === this._taxonomyView.getChildrenCount()) {
				this._taxonEditIndex = -1;
				this._onClose();
				return;
			}
			this._updateEditControls();
			return;
		}

		if (this._metaEditor.isVisible()) {
			this._metaEditor.hide();

			// new taxon to add on current loaded taxonomy
			const taxon = { name: this._metaEditor.getValue(), value: null };
			const taxonomy = this.getValue();

			// insert newly taxon before the first with greater index
			// if all current taxon have smaller indices append newly taxon to the end
			// taxon index is the position in array with defined taxons, as described by meta taxonomy

			for (let i = 0; ; ++i) {
				if (i === taxonomy.length) {
					taxonomy.push(taxon);
					break;
				}
				if (this._meta.getTaxonIndex(taxonomy[i]) > this._meta.getTaxonIndex(taxon)) {
					taxonomy.splice(i, 0, taxon);
					break;
				}
			}

			this.setValue(taxonomy);
			this._onClose();
			return;
		}

		this._updateTaxonomyView();
		this._onClose();
	}

	_onRemove() {
		this._taxonomyView.getByIndex(this._taxonEditIndex).remove();
		this._onClose();
	}

	_onRemoveAll() {
		this._fireEvent("input");
		this.setValue(null);
		this._onClose();
	}

	_onClose() {
		this._batchEdit = false;
		this._taxonEditIndex = -1;
		this._metaEditor.hide();
		this._valueEditor.hide();
		this._updateActions();
	}

	// --------------------------------------------------------------------------------------------

	_openEditor() {
		this._valueEditor.show();
		if (this._taxonEditIndex !== -1) {
			this._updateEditControls();
		}
		this._actions.show("done", "remove", "close");
	}

	_updateActions() {
		const taxonomySize = this._taxonomyView.getChildrenCount();
		if (taxonomySize === 0) {
			this._actions.showOnly("import", "add");
		}
		else {
			this._actions.showOnly("remove-all");
		}
		if (taxonomySize > 1) {
			this._actions.show("batch-edit");
			if (taxonomySize < this._meta.getTaxonomySize()) {
				this._actions.show("add");
			}
		}
		if (this._metaEditor.isVisible()) {
			this._actions.show("done", "close");
		}
	}

	/**
	 * Update editor controls with taxon object at current {@link #_taxonEditIndex} from  {@link #_taxonomy} array.
	 */
	_updateEditControls() {
		$assert(this._taxonEditIndex !== -1, "com.kidscademy.form.TaxonomyControl#_updateEditControls", "Illegal state. No taxon view selected for edit.");
		const taxon = this._taxonomyView.getByIndex(this._taxonEditIndex).getUserData();
		const taxonMeta = this._meta.getTaxonMeta(taxon.meta.name);
		this._valueEditor.setObject(taxonMeta, taxon);
	}

	/**
	 * Store taxon value from editor on selected taxon from taxonomy view.
	 */
	_updateTaxonomyView() {
		$assert(this._taxonEditIndex !== -1, "com.kidscademy.form.TaxonomyControl#_updateEditControls", "Illegal state. No taxon view selected for edit.");
		const taxonView = this._taxonomyView.getByIndex(this._taxonEditIndex);
		const taxon = taxonView.getUserData();
		taxon.value = this._valueEditor.getValue();
		taxonView.setObject(taxon);
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.TaxonomyControl";
	}
};

$preload(com.kidscademy.form.TaxonomyControl);

com.kidscademy.form.TaxonomyControl.Meta = class {
	constructor(taxonomyMeta) {
		this._taxonomyMeta = taxonomyMeta;
	}

	getTaxonomySize() {
		return this._taxonomyMeta.length;
	}

	getTaxonIndex(taxon) {
		for (let i = 0; i < this._taxonomyMeta.length; ++i) {
			if (this._taxonomyMeta[i].name === taxon.name) {
				return i;
			}
		}
		return Infinity;
	}

	getTaxonomyTemplate() {
		return this._taxonomyMeta.map(taxonMeta => { return { meta: taxonMeta, value: null } });
	}

	getTaxonNames() {
		return this._taxonomyMeta.map(taxonMeta => taxonMeta.name);
	}

	getTaxonMeta(taxonName) {
		const taxonMeta = this._taxonomyMeta.find(taxonMeta => taxonMeta.name === taxonName);
		$assert(typeof taxonMeta !== "undefined", "com.kidscademy.form.TaxonomyControl.Meta#getTaxonMeta", "Taxon name |%s| not found", taxonName);
		return taxonMeta;
	}
};

com.kidscademy.form.TaxonomyControl.ValueEditor = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		/**
		 * The name of currently editing atlas object taxon. It is not changeable and is displayed on user
		 * interface as a label.
		 * @type {js.dom.Element}
		 */
		this._nameLabel = this.getByTag("label");

		/** Variant of {@link #_valueControl} used when taxon value is free text. */
		this._valueInput = this.getByTag("input");
		this._valueInput.on("paste", this._onPaste, this);

		/** Variant of {@link #_valueControl} used when taxon value is selected from a set of predefined values. */
		this._valueSelect = this.getByTag("select");

		/**
		 * User interface control that allows for taxon value input. Depending on taxon meta-defintion this control
		 * can be a free text input or a select.
		 * @type {js.dom.Control}
		 */
		this._valueControl = null;
	}

	setObject(taxonMeta, taxon) {
		$assert(typeof taxonMeta.values !== "undefined" && taxonMeta.values !== "", "com.kidscademy.form.TaxonomyControl.ValueEditor#setObject", "Undefined or empty taxon meta values.");
		if (taxonMeta.values == null) {
			this._valueSelect.hide();
			this._valueControl = this._valueInput.show();
		}
		else {
			this._valueInput.hide();
			this._valueSelect.setOptions(taxonMeta.values.split(','));
			this._valueControl = this._valueSelect.show();
		}

		this._nameLabel.setValue(taxon.meta.display);
		this._valueControl.setValue(taxon.value);
		this._valueControl.focus();
	}

	getValue() {
		return this._valueControl.getValue();
	}

	_onPaste(ev) {
		ev.halt();
		var value = ev.getData().trim();
		value = value[0].toUpperCase() + value.slice(1).toLowerCase();
		this._valueInput.setValue(value);
	}

	toString() {
		return "com.kidscademy.form.TaxonomyControl.ValueEditor";
	}
};

com.kidscademy.form.TaxonomyControl.MetaEditor = class extends js.dom.Element {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._meta = null;

		this._select = this.getByTag("select");
	}

	bind(meta) {
		this._meta = meta;
	}

	/**
	 * Open this meta editor and load available taxon names.
	 * 
	 * @param {Array} currentNames names list for taxon already used.
	 * @return {Boolean} false if all registered taxons are used - in which case this editor is not displayed, 
	 * true if this meta editor is opened successfully.
	 */
	open(currentNames) {
		const options = this._meta.getTaxonNames().filter(name => currentNames.indexOf(name) === -1);
		this._select.setOptions(options);
		if (options.length === 0) {
			return false;
		}
		this.show();
		return true;
	}

	getValue() {
		return this._select.getValue();
	}

	toString() {
		return "com.kidscademy.form.TaxonomyControl.MetaEditor";
	}
};
