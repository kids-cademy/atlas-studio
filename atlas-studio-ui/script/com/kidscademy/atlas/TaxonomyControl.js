$package("com.kidscademy.atlas");

/**
 * User interface control used to manage atlas object taxonomy. User interface layout of this control
 * adapts to taxonomy class defined by atlas collection.
 * 
 * @author Iulian Rotaru
 */
com.kidscademy.atlas.TaxonomyControl = class extends js.dom.Control {
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
		 * User interface control that allows for taxon value input. Depending on taxonomy class this control
		 * can be a free text input or a select.
		 * @type {@ js.dom.Control}
		 */
		this._taxonValueControl = null;

		/**
		 * This control actions handler.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
	}

	/**
	 * Handler called by atlas object form just after object loaded.
	 * @param {com.kidscademy.page.FormPage} formPage 
	 */
	onStart(formPage) {
		this._atlasItem = formPage.getAtlasItem();
		this._taxonomyClass = formPage.getCollection().taxonomyClass;

		switch (this._taxonomyClass) {
			case "MUSICAL_INSTRUMENT":
				const familyTaxonomy = com.kidscademy.atlas.TaxonomyControl.MusicalInstrumentTaxonomy;
				this._taxonomyTemplate = familyTaxonomy.template;

				this._taxonValueEditor = this.getByCss(".editor.taxon-value.family");
				this._taxonValueControl = this._taxonValueEditor.getByName("value");

				var options = familyTaxonomy.values;
				options.splice(0, 0, "");
				this._taxonValueControl.setOptions(options);
				break;

			case "BIOLOGICAL":
				this._taxonomyTemplate = com.kidscademy.atlas.TaxonomyControl.BiologicalTaxonomy.template;

				this._taxonValueEditor = this.getByCss(".editor.taxon-value.biological");
				this._taxonValueControl = this._taxonValueEditor.getByName("value");
				this._taxonValueControl.on("keydown", this._onTaxonValueKey, this);
				break;
		}

		this._taxonNameLabel = this._taxonValueEditor.getByName("name");
	}

	/**
	 * Control value setter. It gets atlas object taxonomy that is an array of taxon objects.
	 * @param {Array} taxonomy atlas object taxonomy.
	 * @return {com.kidscademy.atlas.TaxonomyControl} this object pointer.
	 */
	setValue(taxonomy) {
		this._taxonomy = taxonomy;
		this._taxonomyView.setObject(taxonomy);

		if (this._taxonomy.length === 0) {
			this._actions.showOnly("add", "load");
		}
		else {
			this._actions.showOnly("remove-all");
		}
		if (this._taxonomy.length > 1) {
			this._actions.show("batch-edit");
		}
		return this;
	}

	/**
	 * Get atlas object taxonomy update from user interface. Taxonomy is an array of taxon objects.
	 * @returns {Array} atlas object taxonomy.
	 */
	getValue() {
		return this._taxonomy;
	}

	/**
	 * For now taxonomy control is always valid.
	 * @returns {Boolean} always return true.
	 */
	isValid() {
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
			this._currentAction = this._EDIT_TAXON_VALUE;
			this._taxonEditIndex = item.getChildIndex();

			this._updateEditControls();
			this._taxonValueEditor.show();
			this._taxonValueControl.focus();

			this._actions.show("done", "remove", "close");
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
		this.setValue(this._taxonomyTemplate);
	}

	_onLoad() {
		AtlasService.loadAtlasObjectTaxonomy(this._atlasItem.name, this.setValue, this);
	}

	_onBatchEdit() {
		this._taxonEditIndex = 0;
		this._batchEdit = true;
		this._updateEditControls();
		this._taxonValueEditor.show();
		this._taxonValueControl.focus();
		this._actions.show("done", "close");
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
		this._taxonomy[this._taxonEditIndex].value = null;
		this._taxonomyView.setObject(this._taxonomy);
		this._onClose();
	}

	_onRemoveAll() {
		this._taxonomy = null;
		this._taxonomyView.removeChildren();
		this._actions.hide("remove-all");
		this._actions.show("add");
	}

	_onClose() {
		this._batchEdit = false;
		this._taxonValueEditor.hide();
		this._actions.hide("done", "remove", "close");
	}

	/**
	 * Update editor controls with taxon object at current {@link #_taxonEditIndex} from  {@link #_taxonomy} array.
	 */
	_updateEditControls() {
		this._taxonNameLabel.setValue(this._taxonomy[this._taxonEditIndex].name);
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
		return "com.kidscademy.atlas.TaxonomyControl";
	}
};

com.kidscademy.atlas.TaxonomyControl.MusicalInstrumentTaxonomy = {
	template: [
		{ name: "Family", value: null }
	],
	type: "ENUM",
	values: [
		// A keyboard instrument is a musical instrument played using a keyboard.
		"KEYBOARD",
		// Instrument is sounded by being struck or scraped by a beater.
		"PERCUSSION",
		// Produce sound by directing a focused stream of air across the edge of a hole in a cylindrical tube.
		"WOODWIND",
		// Produce sound from vibrating strings transmitted to the body of the instrument.
		"STRINGS"
	]
};

com.kidscademy.atlas.TaxonomyControl.BiologicalTaxonomy = {
	template: [
		{ name: "Kingdom", value: null },
		{ name: "Phylum", value: null },
		{ name: "Class", value: null },
		{ name: "Order", value: null },
		{ name: "Family", value: null },
		{ name: "Genus", value: null },
		{ name: "Species", value: null }
	],
	type: "STRING",
	values: null
};

$preload(com.kidscademy.atlas.TaxonomyControl);
