$package("com.kidscademy.form");

com.kidscademy.form.FeaturesControl = class extends com.kidscademy.form.FormControl {
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
		this._featuresClass = null;

		/**
		 * Atlas object features is an array of feature objects.
		 * @type {Array}
		 */
		this._features = null;

		/**
		 * This view displays current value(s) of the atlas object features.
		 * @type {js.dom.Element}
		 */
		this._featuresView = this.getByCssClass("features-view");
		this._featuresView.on("click", this._onClick, this);

		this._currentItemView = null;

		this._editor = this.getByClass(com.kidscademy.FormData);
		this._editor.getByCss("[name='quantity']").on("change", this._onQuantityChange, this);
		this._editor.findByCss(".units").on("change", this._onUnitsChange, this);

		this._unitsSelect = null;

		this._linkSelect = this.getByClass(com.kidscademy.form.LinkSelect);

		/**
		 * This control actions handler.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this).hideAll();
	}

	setValue(features) {
		this._features = features;
		this._featuresView.setObject(features);
		this._updateActions();
		return this;
	}

	/**
	 * Handler called by atlas object form just after object loaded. Note that this callback is 
	 * invoked after {@link #setValue(Object)}.
	 */
	onStart() {
		this._atlasItem = this._formPage.getAtlasItem();
		this._featuresClass = this._formPage.getCollection().featuresClass;
		// update again actions visibility here because we need features class
		this._updateActions();
	}

	getValue() {
		const features = [];
		this._featuresView.getChildren().forEach(featureView => features.push(featureView.getUserData("value")));
		return features;
	}

	isValid() {
		return true;
	}

	/**
	 * Click event handler uses event delegation pattern to detect taxon object selected by user. This handler
	 * opens taxon value editor and initialize name and value controls.
	 * @param {js.event.Event} ev mouse click event. 
	 */
	_onClick(ev) {
		const itemView = ev.target.getParentByCssClass("item");
		if (!itemView) {
			return;
		}

		this._currentItemView = itemView;
		const feature = itemView.getUserData();
		this._updateUnitsSelect(feature.quantity);

		// create new object instance in order to avoid scalling value of the edited feature reference 
		this._editor.setObject({
			name: feature.name,
			value: (feature.value / this._unitsSelect.getValue()).toFixed(4),
			maximum: feature.maximum != null ? (feature.maximum / this._unitsSelect.getValue()).toFixed(4) : null,
			quantity: feature.quantity
		}).show();
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onImport() {
		const load = (link) => AtlasService.importObjectFeatures(link, features => this.setValue(features));

		const links = this._formPage.getLinks("features");
		switch (links.length) {
			case 0:
				js.ua.System.alert("No provider link for features.");
				break;

			case 1:
				load(links[0]);
				break;

			default:
				this._linkSelect.open(links, load);
				this._actions.show("close");
		}
	}

	_onLoad() {
		AtlasService.getFeatureTemplates(this._featuresClass, templates => this.setValue(templates));
	}

	_onAdd() {
		this._editor.setObject({
			name: null,
			value: null,
			maximum: null,
			quantity: null
		}).show();
		this._updateUnitsSelect();
		this._currentItemView = null;
	}

	_onDone() {
		if (!this._editor.isVisible()) {
			return;
		}
		if (!this._editor.isValid()) {
			return;
		}

		const feature = this._editor.getObject();
		feature.value = (feature.value * this._unitsSelect.getValue()).toFixed(4);
		if (feature.maximum) {
			feature.maximum = (feature.maximum * this._unitsSelect.getValue()).toFixed(4);
		}

		// udpate feature display - processed on server, before updating user interface
		AtlasService.updateFeatureDisplay(feature, feature => {
			if (this._currentItemView != null) {
				this._currentItemView.setObject(feature);
			}
			else {
				this._featuresView.addObject(feature);
			}
			this._onClose();
		});
	}

	_onRemove() {
		js.ua.System.confirm("@string/confirm-feature-remove", ok => {
			if (ok) {
				this._currentItemView.remove();
				this._onClose();
			}
		});
	}

	_onRemoveAll() {
		js.ua.System.confirm("@string/confirm-all-features-remove", ok => {
			if (ok) {
				this._featuresView.removeChildren();
				this._onClose();
			}
		});
	}

	_onClose() {
		this._currentItemView = null;
		this._editor.reset().hide();
	}

	// --------------------------------------------------------------------------------------------

	_updateActions() {

		this._actions.showAll();

	}

	_updateUnitsSelect(quantity) {
		this._editor.findByCss(".units").forEach(unitsSelect => unitsSelect.hide());
		if (!quantity) {
			quantity = this._editor.getByCss("select[name='quantity']").getValue();
		}
		const unitsSelector = quantity.toLowerCase();
		if (unitsSelector) {
			this._unitsSelect = this._editor.getByCssClass(unitsSelector).show();
		}
	}

	_onQuantityChange(ev) {
		this._updateUnitsSelect();
	}

	_onUnitsChange(ev) {
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.FeaturesControl";
	}
};

$preload(com.kidscademy.form.FeaturesControl);
