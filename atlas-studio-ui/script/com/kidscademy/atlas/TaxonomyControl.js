$package("com.kidscademy.atlas");

com.kidscademy.atlas.TaxonomyControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._taxonomyClass = null;

		/**
		 * Atlas object taxonomy is an array of taxon objects.
		 * @type {Array}
		 */
		this._taxonomy = null;

		/**
		 * Array index for taxon object currently on edit.
		 * @type {Object}
		 */
		this._taxonEditIndex = 0;

		/**
		 * Pseudo-constants for current action performed on taxonomy control.
		 */
		this._EDIT_TAXONOMY_CLASS = 1;
		this._EDIT_TAXON_VALUE = 2;

		/**
		 * Current action performed on this taxonomy control.
		 * @type {Number}
		 */
		this._currentAction = this._EDIT_TAXON_VALUE;

		this._taxonomyView = this.getByCssClass("taxonomy-view");
		this._taxonomyView.on("click", this._onItemClick, this);

		this._taxonomyClassEditor = this.getByCss(".editor.taxonomy-class");
		this._taxonomyClassSelect = this.getByName("taxonomy-class");

		/**
		 * Current taxon value editor. It is initialized at this control creation, see {@link #onStart()}.
		 * @type {js.dom.Element}
		 */
		this._taxonValueEditor = null;
		this._taxonNameLabel = null;
		this._taxonValueControl = null;

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);
	}

	onCreate(formPage) {
		this._taxonomyClass = formPage.getCollection().taxonomyClass;
	}

	onStart() {
		switch (this._taxonomyClass) {
			case "FAMILY":
				const familyTaxonomy = com.kidscademy.atlas.TaxonomyControl.FamilyTaxonomy;
				this._taxonomyTemplate = familyTaxonomy.template;

				this._taxonValueEditor = this.getByCss(".editor.taxon-value.family");
				this._taxonValueControl = this._taxonValueEditor.getByName("value");
				this._taxonValueControl.setOptions(familyTaxonomy.values);
				break;

			case "BIOLOGICAL":
				this._taxonomyTemplate = com.kidscademy.atlas.TaxonomyControl.BiologicalTaxonomy.template;
				
				this._taxonValueEditor = this.getByCss(".editor.taxon-value.biological");
				this._taxonValueControl = this._taxonValueEditor.getByName("value");
				break;
		}

		this._taxonNameLabel = this._taxonValueEditor.getByName("name");
	}

	setValue(taxonomy) {
		this._taxonomy = taxonomy;
		this._taxonomyView.setObject(taxonomy);
	}

	getValue() {
		return this._taxonomy;
	}

	isValid() {
		return true;
	}

	_onItemClick(ev) {
		const item = ev.target.getParentByTag("tr");
		if (item) {
			this._currentAction = this._EDIT_TAXON_VALUE;
			this._taxonEditIndex = item.getChildIndex();
			this._taxonNameLabel.setValue(this._taxonomy[this._taxonEditIndex].name);
			this._taxonValueControl.setValue(this._taxonomy[this._taxonEditIndex].value);
			this._taxonValueEditor.show();
		}
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onAdd() {
		this.setValue(this._taxonomyTemplate);

		return;
		this._currentAction = this._EDIT_TAXONOMY_CLASS;
		this._taxonomyClassEditor.show();
		this._taxonomyClassSelect.reset();
	}

	_onDone() {
		switch (this._currentAction) {
			case this._EDIT_TAXONOMY_CLASS:
				switch (this._taxonomyClassSelect.getValue()) {
					case "@string/taxonomy-class-family":
						this._taxonValueEditor = this.getByCss(".editor.taxon-value.family");
						this.setValue(com.kidscademy.atlas.TaxonomyControl.FamilyTaxonomy.template);
						break;

					case "@string/taxonomy-class-biological":
						this._taxonValueEditor = this.getByCss(".editor.taxon-value.biological");
						this.setValue(com.kidscademy.atlas.TaxonomyControl.BiologicalTaxonomy.template);
						break;
				}
				break;

			case this._EDIT_TAXON_VALUE:
				this._taxonomy[this._taxonEditIndex] = {
					name: this._taxonNameLabel.getValue(),
					value: this._taxonValueControl.getValue()
				}
				this._taxonomyView.setObject(this._taxonomy);
				break;
		}
		this._onClose();
	}

	_onRemove() {
		this._taxonomy.splice(this._taxonEditIndex, 1);
		this._taxonValueEditor.hide();
		this._taxonomyView.setObject(this._taxonomy);
	}

	_onClose() {
		switch (this._currentAction) {
			case this._EDIT_TAXONOMY_CLASS:
				this._taxonomyClassEditor.hide();
				break;

			case this._EDIT_TAXON_VALUE:
				this._taxonValueEditor.hide();
				break;
		}
		this._currentAction = 0;
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

com.kidscademy.atlas.TaxonomyControl.FamilyTaxonomy = {
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
