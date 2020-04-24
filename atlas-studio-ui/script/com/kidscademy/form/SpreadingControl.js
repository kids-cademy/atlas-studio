$package("com.kidscademy.form");

com.kidscademy.form.SpreadingControl = class extends com.kidscademy.form.FormControl {
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
		this._nameInput = this._editor.getByName("name");
		this._areaSelect = this._editor.getByName("area");
		this._lessInput = this._editor.getByName("less");
		this._lessAreaSelect = this._editor.getByName("less-area");
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
		this._nameInput.reset();
		this._areaSelect.reset();
		this._lessInput.reset();
		this._lessAreaSelect.reset();
	}

	_onImport() {
		js.ua.System.alert("Import not yet implemented.");
	}

	_onDone(ev) {
		if (this._editingRegionIndex === -1) {
			// not editing mode, that is, add a new region
			this._regions.push({
				id: 0,
				name: this._nameInput.getValue(),
				area: this._areaSelect.getValue(),
				less: this._lessInput.getValue(),
				lessArea: this._lessAreaSelect.getValue()
			});
		}
		else {
			// editing mode; this._editingRegionIndex points to item that is editing
			this._regions[this._editingRegionIndex].name = this._nameInput.getValue();
			this._regions[this._editingRegionIndex].area = this._areaSelect.getValue();
			this._regions[this._editingRegionIndex].less = this._lessInput.getValue();
			this._regions[this._editingRegionIndex].lessArea = this._lessAreaSelect.getValue();
		}

		this._updateRegionsView();
		this._showEditor(false);

		// do not fire 'input' explicitly since it is already fired by controls from editor
		// this._fireEvent("input");
	}

	_onRemove(ev) {
		js.ua.System.confirm("@string/confirm-area-remove", ok => {
			if (ok) {
				this._regions.splice(this._editingRegionIndex, 1);
				this._updateRegionsView();
				this._showEditor(false);
				// 'input' event is processed by form page to update dirty state
				this._fireEvent("input");
			}
		});
	}

	_onRemoveAll() {
		js.ua.System.confirm("@string/confirm-all-area-remove", ok => {
			if (ok) {
				this._regions.length = 0;
				this._updateRegionsView();
				this._showEditor(false);
				this._fireEvent("input");
			}
		});
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
			this._nameInput.setValue(region.name);
			this._areaSelect.setValue(region.area);
			this._lessInput.setValue(region.less);
			this._lessAreaSelect.setValue(region.lessArea);
		}
	}

	_updateRegionsView() {
		this._regionsView.setObject(this._regions);
	}

	_showEditor(show) {
		this._actions.show(show, "done", "remove", "close");
		this._editor.show(show);
		if (show) {
			this._nameInput.focus();
		}
	}

	/**
	 * Class string representation.
	 * 
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.form.SpreadingControl";
	}
};

$preload(com.kidscademy.form.SpreadingControl);
