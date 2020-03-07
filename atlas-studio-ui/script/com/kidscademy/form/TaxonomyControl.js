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
		 * The class of taxonomy defines classification type and is used to select the user interface 
		 * layout used by this control. Current supported values are 'MUSICAL_INSTRUMENT' and 'BIOLOGICAL'. 
		 * 
		 * Musical instrument taxonomy has only one classification criterion named 'Family' and accepts
		 * values from an enumeration.
		 * 
		 * Biological taxonomy is the scientific classification for animals and plants.
		 * @type {String}
		 */
		this._taxonomyClass = null;

		/**
		 * Atlas object taxonomy is an array of taxon objects.
		 * @type {Array}
		 */
		this._taxonomy = null;

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
		 * Current taxon value editor. It is initialized at this control creation, see {@link #onStart()}.
		 * A taxon is a classification criterion from atlas object {@link #_taxonomy}.
		 * @type {js.dom.Element}
		 */
		this._taxonValueEditor = null;

		/**
		 * The name of currently editing atlas object taxon. It is not changeable and is displayed on user
		 * interface as a label.
		 * @type {js.dom.Element}
		 */
		this._taxonNameLabel = null;

		/**
		 * User interface control that allows for taxon value input. Depending on taxon meta-defintion this control
		 * can be a free text input or a select.
		 * @type {@ js.dom.Control}
		 */
		this._taxonValueControl = null;

		/** Variant of {@link #_taxonValueControl} used when taxon value is free text. */
		this._taxonValueInput = null;

		/** Variant of {@link #_taxonValueControl} used when taxon value is selected from a set of predefined values. */
		this._taxonValueSelect = null;

		/**
		 * This control actions handler.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this).hideAll();
	}

	/**
	 * Control value setter. It gets atlas object taxonomy that is an array of taxon objects.
	 * @param {Array} taxonomy atlas object taxonomy.
	 * @return {com.kidscademy.form.TaxonomyControl} this object pointer.
	 */
	setValue(taxonomy) {
		this._taxonomy = taxonomy;
		this._taxonomyView.setObject(taxonomy);
		this._updateActions();
		return this;
	}

	/**
	 * Handler called by atlas object form just after object loaded. Note that this callback is 
	 * invoked after {@link #setValue(Object)}.
	 */
	onStart() {
		this._taxonValueEditor = this.getByCss(".editor.taxon-value");
		this._taxonNameLabel = this._taxonValueEditor.getByName("name");

		this._taxonValueInput = this._taxonValueEditor.getByName("value-input");
		this._taxonValueInput.on("keydown", this._onTaxonValueKey, this);
		this._taxonValueSelect = this._taxonValueEditor.getByName("value-select");
	}

	/**
	 * Get atlas object taxonomy update from user interface. Taxonomy is an array of taxon objects.
	 * @returns {Array} atlas object taxonomy.
	 */
	getValue() {
		return this._taxonomy;
	}

	/**
	 * Test if taxonomy values are filled.
	 * @returns {Boolean} return true if taxonomy all values are filled.
	 */
	isValid() {
		for (let i = 0; i < this._taxonomy.length; ++i) {
			if (!this._taxonomy[i].value) {
				return false;
			}
		}
		return true;
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

	_onTaxonValueKey(ev) {
		switch (ev.key) {
			case js.event.Key.ENTER:
				this._onDone();
				break;

			case js.event.Key.ESCAPE:
				this._onClose();
				break;
		}
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onAdd() {
		const taxonomyMeta = this._formPage.getCollection().taxonomyMeta;
		const taxonomy = taxonomyMeta.map(taxonMeta => { return { name: taxonMeta.name, value: null } });

		this.setValue(taxonomy);
		if (this._taxonomy.length == 1) {
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
			if (++this._taxonEditIndex === this._taxonomy.length) {
				this._taxonEditIndex = -1;
				this._onClose();
				return;
			}
			this._updateEditControls();
			return;
		}

		this._updateTaxonomyView();
		this._onClose();
	}

	_onRemove() {
		this._fireEvent("input");
		this._taxonomy[this._taxonEditIndex].value = null;
		this._taxonomyView.setObject(this._taxonomy);
		this._onClose();
	}

	_onRemoveAll() {
		this._fireEvent("input");
		this.setValue(null);
		this._onClose();
	}

	_onClose() {
		this._batchEdit = false;
		this._taxonValueEditor.hide();
		this._updateActions();
	}

	// --------------------------------------------------------------------------------------------

	_openEditor() {
		this._taxonValueEditor.show();
		//this._taxonValueControl.reset().focus();

		this._updateEditControls();
		this._actions.show("done", "remove", "close");
	}

	_updateActions() {
		const taxonomySize = this._taxonomy === null ? 0 : this._taxonomy.length;
		if (taxonomySize === 0) {
			this._actions.showOnly("add");
		}
		else {
			this._actions.showOnly("remove-all");
		}

		if (taxonomySize > 1) {
			this._actions.show("batch-edit");
		}

		if ((this._taxonomy == null || this._taxonomy.length === 0) && this._taxonomyClass === "BIOLOGICAL") {
			this._actions.show("load");
		}
	}

	/**
	 * Update editor controls with taxon object at current {@link #_taxonEditIndex} from  {@link #_taxonomy} array.
	 */
	_updateEditControls() {
		this._taxonNameLabel.setValue(this._taxonomy[this._taxonEditIndex].name);

		const taxonMeta = this._formPage.getCollection().taxonomyMeta[this._taxonEditIndex];
		if (taxonMeta.values == null) {
			this._taxonValueSelect.hide();
			this._taxonValueControl = this._taxonValueInput.show();
		}
		else {
			this._taxonValueInput.hide();
			this._taxonValueSelect.setOptions(taxonMeta.values.split(','));
			this._taxonValueControl = this._taxonValueSelect.show();
		}

		this._taxonValueControl.setValue(this._taxonomy[this._taxonEditIndex].value);
	}

	/**
	 * Store taxon value from editor on {@link #_taxonomy} array and reload taxonomy view.
	 */
	_updateTaxonomyView() {
		this._taxonomy[this._taxonEditIndex].value = this._taxonValueControl.getValue();
		this._taxonomyView.setObject(this._taxonomy);
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
