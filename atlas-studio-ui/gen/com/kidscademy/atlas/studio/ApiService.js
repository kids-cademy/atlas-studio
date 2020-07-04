$package("com.kidscademy.atlas.studio");

/**
 * Api service.
 */
com.kidscademy.atlas.studio.ApiService = {
	/**
	 * Get definition.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.lang.String
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getDefinition: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.ApiService#getDefinition", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ApiService#getDefinition", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ApiService#getDefinition", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ApiService", "getDefinition");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get description.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.LinkedHashMap<java.lang.String,java.lang.String>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getDescription: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.ApiService#getDescription", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ApiService#getDescription", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ApiService#getDescription", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ApiService", "getDescription");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get facts.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getFacts: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.ApiService#getFacts", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ApiService#getFacts", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ApiService#getFacts", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ApiService", "getFacts");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get taxonomy.
	 *
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.Taxon>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getTaxonomy: function(link) {
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.ApiService#getTaxonomy", "Link argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ApiService#getTaxonomy", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ApiService#getTaxonomy", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ApiService", "getTaxonomy");
		rmi.setParameters(link);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get features.
	 *
	 * @param int collectionId,
	 * @param com.kidscademy.atlas.studio.model.Link link,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.Feature>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getFeatures: function(collectionId, link) {
		$assert(typeof collectionId !== "undefined", "com.kidscademy.atlas.studio.ApiService#getFeatures", "Collection id argument is undefined.");
		$assert(js.lang.Types.isNumber(collectionId), "com.kidscademy.atlas.studio.ApiService#getFeatures", "Collection id argument is not a number.");
		$assert(typeof link !== "undefined", "com.kidscademy.atlas.studio.ApiService#getFeatures", "Link argument is undefined.");

		var __callback__ = arguments[2];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ApiService#getFeatures", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ApiService#getFeatures", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ApiService", "getFeatures");
		rmi.setParameters(collectionId, link);
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof ApiService === 'undefined') {
	ApiService = com.kidscademy.atlas.studio.ApiService;
}
