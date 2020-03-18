$package("com.kidscademy.form");

com.kidscademy.form.FeaturesControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._featuresMeta = null;

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

		this._selectedFeatureView = null;

		this._featureForm = this.getByClass(com.kidscademy.FormData);

		this._featureNameSelect = this._featureForm.getByName("name");
		this._featureNameSelect.on("change", this._onFeatureNameChange, this);

		this._unitsSelect = this._featureForm.getByName("units");
		this._unitsSelect.on("change", this._onUnitsChange, this);

		this._linkSelect = this.getByClass(com.kidscademy.form.LinkSelect);

		/**
		 * This control actions handler.
		 * @type {com.kidscademy.Actions}
		 */
		this._actions = this.getByClass(com.kidscademy.Actions).bind(this).hideAll();
	}

	/**
	 * Handler called by atlas object form just after object loaded. Note that this callback is 
	 * invoked after {@link #setValue(Object)}.
	 */
	onStart() {
		this._featuresMeta = this._formPage.getAtlasItem().collection.featuresMeta;
		this._updateActions();
	}

	setValue(features) {
		this._features = features;
		this._featuresView.setObject(features);
		this._updateActions();
		return this;
	}

	getValue() {
		return this._featuresView.getChildren().map(featureView => featureView.getUserData());
	}

	isValid() {
		return this._featuresView.hasChildren();
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
		// feature name select options should display only not already used names
		const currentNames = this._featuresView.getChildren().map(featureView => featureView.getUserData().name);
		const options = this._featuresMeta.filter(featureMeta => currentNames.indexOf(featureMeta.name) == -1);
		options.unshift({ id: 0, name: "" });
		this._featureNameSelect.setOptions(options);

		this._featureForm.setObject({
			name: null,
			value: null,
			maximum: null,
			quantity: null
		}).show();
		this._selectedFeatureView = null;
	}

	_onDone() {
		if (!this._featureForm.isVisible()) {
			return;
		}
		if (!this._featureForm.isValid()) {
			return;
		}

		const feature = this._featureForm.getObject();
		// feature name select store id into option value and we need feature name, stored on text
		feature.name = this._featureNameSelect.getText();
		feature.value = this._round(feature.value * this._unitsSelect.getValue());
		if (feature.maximum) {
			feature.maximum = this._round(feature.maximum * this._unitsSelect.getValue());
		}

		// udpate feature display - processed on server, before updating user interface
		AtlasService.updateFeatureDisplay(feature, feature => {
			if (this._selectedFeatureView != null) {
				this._selectedFeatureView.setObject(feature);
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
				this._selectedFeatureView.remove();
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
		this._selectedFeatureView = null;
		this._featureForm.reset().hide();
	}

	// --------------------------------------------------------------------------------------------

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
		this._selectedFeatureView = itemView;

		// ensure feature name select contains the name of the currently selected feature view by reloading all
		this._featureNameSelect.setOptions(this._featuresMeta);

		const feature = this._selectedFeatureView.getUserData();
		this._updateFeatureForm(feature, (ok) => {
			if (!ok) {
				js.ua.System.alert("@string/alert-inconsistent-db");
				this._selectedFeatureView.remove();
				return;
			}

			// create new object instance in order to avoid scalling value of the edited feature reference 
			this._featureForm.setObject({
				name: feature.name,
				definition: feature.definition,
				value: this._round(feature.value / this._unitsSelect.getValue()),
				maximum: feature.maximum != null ? this._round(feature.maximum / this._unitsSelect.getValue()) : null,
				quantity: feature.quantity
			}).show();
		});
	}

	_updateActions() {
		this._actions.showAll();
	}

	_onQuantityChange(ev) {
		this._updateFeatureForm();
	}

	_onUnitsChange(ev) {
		const feature = this._featureForm.getObject();
		this._formPage.setContextAttr(feature.quantity, this._unitsSelect.getText());

		const factor = this._unitsSelect.getValue();
		this._featureForm.setValue("value", this._round(feature.value * factor));
		this._featureForm.setValue("maximum", feature.maximum != null ? this._round(feature.maximum * factor) : null);
	}

	_onFeatureNameChange(ev) {
		const feature = ev.target.getObject();
		this._updateFeatureForm(feature);
	}

	_updateFeatureForm(feature, callback) {
		this._featureForm.setValue("name", feature.name);
		this._featureForm.setValue("quantity", feature.quantity);

		// feature definition is found on meta
		const featureMeta = this._featuresMeta.find(featureMeta => featureMeta.name === feature.name);
		if (!featureMeta) {
			if (callback) { callback(false); }
			return;
		}
		this._featureForm.setValue("definition", featureMeta.definition);

		AtlasService.getQuantityUnits(feature.quantity, units => {
			this._unitsSelect.setOptions(units);

			var unitText = this._formPage.getContextAttr(feature.quantity);
			if (unitText == null) {
				unitText = units[0].text;
				this._formPage.setContextAttr(feature.quantity, unitText);
			}
			this._unitsSelect.setValue(unitText);

			if (callback) { callback(true); }
		});
	}

	_round(number) {
		return Number(number.toFixed(9));
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
