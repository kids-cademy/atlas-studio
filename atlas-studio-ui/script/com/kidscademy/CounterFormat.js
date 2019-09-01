$package("com.kidscademy");

/**
 * CounterFormat class.
 *
 * @author Iulian Rotaru
 */
com.kidscademy.CounterFormat = class extends js.format.Number {
	/**
	 * Construct an CounterFormat instance.
	 */
	constructor() {
		super();
		
		/**
		 * Number format used internally to render numeric values.
		 * 
		 * @type js.format.NumberFormat
		 */
		this._numberFormat = new js.format.NumberFormat();

		this._numberFormat.setGroupingUsed(true);
		this._numberFormat.setMinimumFractionDigits(0);
		this._numberFormat.setMaximumFractionDigits(0);
	}

	/**
	 * Class string representation.
	 *
	 * @return this class string representation.
	 */
	toString() {
		return "com.kidscademy.CounterFormat";
	}
};
