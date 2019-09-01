$package("com.kidscademy.atlas");

com.kidscademy.atlas.SpreadingControl = class extends js.dom.Control {
	constructor(ownerDoc, node) {
		super(ownerDoc, node);

		this.AREA_DISPLAY = {
			"WHOLE": "",
			"CENTRAL": "Central",
			"NORTH": "North",
			"NORTH_EAST": "North-East",
			"EAST": "East",
			"SOUTH_EAST": "South-East",
			"SOUTH": "South",
			"SOUTH_WEST": "South-West",
			"WEST": "West",
			"NORTH_WEST": "North-West"
		};

		this._regions = [];

		this._regionsView = this.getByCssClass("regions-view");
		this._regionsView.on("click", this._onRegionsViewClick, this);

		this._editor = this.getByCssClass("editor");
		this._regionNameInput = this._editor.getByName("region-name");
		this._areaSelect = this._editor.getByName("area");
		this._editingRegionIndex = -1;

		this._actions = this.getByClass(com.kidscademy.Actions).bind(this);

		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------
	// CONTROL INTERFACE

	setValue(regions) {
		this._regions = regions;
		this._updateRegionsView();
	}

	getValue() {
		return this._regions;
	}

	isValid() {
		return true;
	}

	// --------------------------------------------------------------------------------------------
	// ACTION HANDLERS

	_onAdd(ev) {
		this._editingRegionIndex = -1;
		this._showEditor(true);
		this._regionNameInput.reset();
		this._areaSelect.reset();
	}

	_onImport() {
		js.ua.System.alert("Import not yet implemented.");
	}

	_onDone(ev) {
		if (this._editingRegionIndex === -1) {
			// not editing mode, that is, add a new region
			this._regions.push({
				id: 0,
				name: this._regionNameInput.getValue(),
				area: this._areaSelect.getValue()
			});
		}
		else {
			// editing mode; this._editingRegionIndex points to item that is editing
			this._regions[this._editingRegionIndex].name = this._regionNameInput.getValue();
			this._regions[this._editingRegionIndex].area = this._areaSelect.getValue();
		}

		this._updateRegionsView();
		this._showEditor(false);
	}

	_onRemove(ev) {
		this._regions.splice(this._editingRegionIndex, 1);
		this._updateRegionsView();
		this._showEditor(false);
	}

	_onClose(ev) {
		this._showEditor(false);
	}

	// --------------------------------------------------------------------------------------------
	
	_onRegionsViewClick(ev) {
		const li = ev.target.getParentByTag("li");
		if (li) {
			this._editingRegionIndex = li.getChildIndex();
			this._showEditor(true);

			var region = this._regions[this._editingRegionIndex];
			this._regionNameInput.setValue(region.name);
			this._areaSelect.setValue(region.area);
		}
	}

	_updateRegionsView() {
		this._regionsView.setObject(this._regions);
	}

	_showEditor(show) {
		this._actions.show(show, "done", "remove", "close");
		this._editor.show(show);
		if (show) {
			this._regionNameInput.focus();
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.atlas.SpreadingControl";
	}
};

$preload(com.kidscademy.atlas.SpreadingControl);
