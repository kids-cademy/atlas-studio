$package("com.kidscademy.form");

com.kidscademy.form.FeaturesControl = class extends com.kidscademy.form.FormControl {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this._featuresMeta = null;

		/**
		 * This view displays current value(s) of the atlas object features.
		 * @type {js.dom.Element}
		 */
		this._featuresView = this.getByCssClass("features-view");
		this._featuresView.on("click", this._onClick, this);

		this._selectedFeatureView = null;

		this._featureForm = this.getByClass(com.kidscademy.FormData);

		this._featureNameSelect = this._featureForm.getByName("meta.name");
		this._featureNameSelect.on("change", this._onFeatureMetaNameChange, this);

		this._unitsSelect = this._featureForm.getByName("units");
		this._unitsSelect.on("change", this._onUnitsChange, this);

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
	onCreate(formPage) {
		super.onCreate(formPage);
		const collection = formPage.getCollection();
		this._featuresMeta = collection.featuresMeta;
		this._featuresType = collection.featuresType;
		this._updateActions();
	}

	setValue(features) {
		this._featuresView.setObject(features);
		this._updateActions();
		return this;
	}

	getValue() {
		return this._featuresView.getListData();
	}

	isValid() {
		return this._featuresView.hasChildren();
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onImport() {
		this._fireEvent("input");
		this._formPage.importFromLink(this._featuresType, features => this.setValue(features));
	}

	_onAdd() {
		// feature name select options should display only not already used names
		const currentNames = this._featuresView.getChildren().map(featureView => featureView.getUserData().meta.name);
		const options = this._featuresMeta.filter(featureMeta => currentNames.indexOf(featureMeta.name) == -1);
		options.unshift({ id: 0, name: "" });
		this._featureNameSelect.setOptions(options);

		this._featureForm.reset().show();
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
		// feature meta name select store id into option value and we need feature name, stored on text
		feature.meta.name = this._featureNameSelect.getText();
		feature.value = this._round(feature.value * this._getUnitsValue());
		if (feature.maximum) {
			feature.maximum = this._round(feature.maximum * this._getUnitsValue());
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
		this._initFeatureForm(feature.meta, () => {
			this._featureForm.setObject(feature);
			const factor = this._unitsSelect.getValue();
			this._featureForm.setValue("value", this._round(feature.value / factor));
			this._featureForm.setValue("maximum", feature.maximum != null ? this._round(feature.maximum / factor) : null);
			this._featureForm.show();
		});
	}

	_updateActions() {
		this._actions.showAll();
	}

	_onUnitsChange(ev) {
		const feature = this._featureForm.getObject();
		// feature meta name select store id into option value and we need feature name, stored on text
		feature.meta.name = this._featureNameSelect.getText();
		this._formPage.setPageAttr(feature.meta.name, this._unitsSelect.getText());

		const factor = this._unitsSelect.getValue();
		this._featureForm.setValue("value", this._round(feature.value * factor));
		this._featureForm.setValue("maximum", feature.maximum != null ? this._round(feature.maximum * factor) : null);
	}

	_onFeatureMetaNameChange(ev) {
		const featureMeta = ev.target.getObject();
		this._initFeatureForm(featureMeta);
	}

	/**
	 * Initialize feature form from given feature meta object. This method invokes services on server and
	 * need to be executed asynchronously. Callback function has no arguments.
	 * 
	 * @param {Object} featureMeta feature meta object,
	 * @param {Function} callback optional callback invoked after initialization completes.
	 */
	_initFeatureForm(featureMeta, callback = null) {
		if (featureMeta.id === 0) {
			this._unitsSelect.clearOptions();
			this._featureForm.reset();
			if (callback != null) {
				callback();
			}
			return;
		}

		const feature = { meta: featureMeta };
		this._featureForm.setObject(feature);

		if (featureMeta.quantity === "SCALAR") {
			this._unitsSelect.hide();
			if (callback != null) {
				callback();
			}
			return;
		}

		this._unitsSelect.show();
		AtlasService.getQuantityUnits(featureMeta.quantity, units => {
			this._unitsSelect.setOptions(units);

			var unitText = this._formPage.getPageAttr(featureMeta.name);
			if (unitText == null) {
				unitText = units[0].text;
				this._formPage.setPageAttr(featureMeta.name, unitText);
			}
			this._unitsSelect.setValue(unitText);
			if (callback != null) {
				callback();
			}
		});
	}

	_getUnitsValue() {
		return this._unitsSelect.isVisible() ? this._unitsSelect.getValue() : 1;
	}

	_round(number) {
		return Number(number.toFixed(9));
	}

	toString() {
		return "com.kidscademy.form.FeaturesControl";
	}
};

$preload(com.kidscademy.form.FeaturesControl);
