$package("com.kidscademy.atlas.studio");

/**
 * Release service.
 */
com.kidscademy.atlas.studio.ReleaseService = {
	/**
	 * Get releases.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.ReleaseItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getReleases: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#getReleases", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#getReleases", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "getReleases");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Create release.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Release
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createRelease: function() {
		var __callback__ = arguments[0];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#createRelease", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#createRelease", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "createRelease");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get release.
	 *
	 * @param int releaseId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Release
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getRelease: function(releaseId) {
		$assert(typeof releaseId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#getRelease", "Release id argument is undefined.");
		$assert(js.lang.Types.isNumber(releaseId), "com.kidscademy.atlas.studio.ReleaseService#getRelease", "Release id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#getRelease", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#getRelease", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "getRelease");
		rmi.setParameters(releaseId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get release by name.
	 *
	 * @param java.lang.String releaseName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Release
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getReleaseByName: function(releaseName) {
		$assert(typeof releaseName !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#getReleaseByName", "Release name argument is undefined.");
		$assert(releaseName === null || js.lang.Types.isString(releaseName), "com.kidscademy.atlas.studio.ReleaseService#getReleaseByName", "Release name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#getReleaseByName", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#getReleaseByName", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "getReleaseByName");
		rmi.setParameters(releaseName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Save release.
	 *
	 * @param com.kidscademy.atlas.studio.model.Release release,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Release
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 saveRelease: function(release) {
		$assert(typeof release !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#saveRelease", "Release argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#saveRelease", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#saveRelease", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "saveRelease");
		rmi.setParameters(release);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove release.
	 *
	 * @param int releaseId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeRelease: function(releaseId) {
		$assert(typeof releaseId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#removeRelease", "Release id argument is undefined.");
		$assert(js.lang.Types.isNumber(releaseId), "com.kidscademy.atlas.studio.ReleaseService#removeRelease", "Release id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#removeRelease", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#removeRelease", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "removeRelease");
		rmi.setParameters(releaseId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Update release graphics.
	 *
	 * @param int releaseId,
	 * @param java.lang.String background,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 updateReleaseGraphics: function(releaseId, background) {
		$assert(typeof releaseId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#updateReleaseGraphics", "Release id argument is undefined.");
		$assert(js.lang.Types.isNumber(releaseId), "com.kidscademy.atlas.studio.ReleaseService#updateReleaseGraphics", "Release id argument is not a number.");
		$assert(typeof background !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#updateReleaseGraphics", "Background argument is undefined.");
		$assert(background === null || js.lang.Types.isString(background), "com.kidscademy.atlas.studio.ReleaseService#updateReleaseGraphics", "Background argument is not a string.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#updateReleaseGraphics", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#updateReleaseGraphics", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "updateReleaseGraphics");
		rmi.setParameters(releaseId, background);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Upload release image.
	 *
	 * @param js.tiny.container.http.form.Form imageForm,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.Image
	 * @throws java.io.IOException
	 * @throws js.rmi.BusinessException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 uploadReleaseImage: function(imageForm) {
		$assert(typeof imageForm !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#uploadReleaseImage", "Image form argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#uploadReleaseImage", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#uploadReleaseImage", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "uploadReleaseImage");
		rmi.setParameters(imageForm);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Create icon.
	 *
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 createIcon: function() {
		var __callback__ = arguments[0];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#createIcon", "Callback is not a function.");
		var __scope__ = arguments[1];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#createIcon", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "createIcon");
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get release items.
	 *
	 * @param int releaseId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.List<com.kidscademy.atlas.studio.model.AtlasItem>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getReleaseItems: function(releaseId) {
		$assert(typeof releaseId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#getReleaseItems", "Release id argument is undefined.");
		$assert(js.lang.Types.isNumber(releaseId), "com.kidscademy.atlas.studio.ReleaseService#getReleaseItems", "Release id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#getReleaseItems", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#getReleaseItems", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "getReleaseItems");
		rmi.setParameters(releaseId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Add release child.
	 *
	 * @param int releaseId,
	 * @param int childId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 addReleaseChild: function(releaseId, childId) {
		$assert(typeof releaseId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#addReleaseChild", "Release id argument is undefined.");
		$assert(js.lang.Types.isNumber(releaseId), "com.kidscademy.atlas.studio.ReleaseService#addReleaseChild", "Release id argument is not a number.");
		$assert(typeof childId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#addReleaseChild", "Child id argument is undefined.");
		$assert(js.lang.Types.isNumber(childId), "com.kidscademy.atlas.studio.ReleaseService#addReleaseChild", "Child id argument is not a number.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#addReleaseChild", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#addReleaseChild", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "addReleaseChild");
		rmi.setParameters(releaseId, childId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Add release children.
	 *
	 * @param int releaseId,
	 * @param java.util.List<java.lang.Integer> childIds,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 addReleaseChildren: function(releaseId, childIds) {
		$assert(typeof releaseId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#addReleaseChildren", "Release id argument is undefined.");
		$assert(js.lang.Types.isNumber(releaseId), "com.kidscademy.atlas.studio.ReleaseService#addReleaseChildren", "Release id argument is not a number.");
		$assert(typeof childIds !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#addReleaseChildren", "Child ids argument is undefined.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#addReleaseChildren", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#addReleaseChildren", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "addReleaseChildren");
		rmi.setParameters(releaseId, childIds);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove release child.
	 *
	 * @param int releaseId,
	 * @param int childId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeReleaseChild: function(releaseId, childId) {
		$assert(typeof releaseId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#removeReleaseChild", "Release id argument is undefined.");
		$assert(js.lang.Types.isNumber(releaseId), "com.kidscademy.atlas.studio.ReleaseService#removeReleaseChild", "Release id argument is not a number.");
		$assert(typeof childId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#removeReleaseChild", "Child id argument is undefined.");
		$assert(js.lang.Types.isNumber(childId), "com.kidscademy.atlas.studio.ReleaseService#removeReleaseChild", "Child id argument is not a number.");

		var __callback__ = arguments[2];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#removeReleaseChild", "Callback is not a function.");
		var __scope__ = arguments[3];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#removeReleaseChild", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "removeReleaseChild");
		rmi.setParameters(releaseId, childId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get android app for release.
	 *
	 * @param java.lang.String releaseName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AndroidApp
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getAndroidAppForRelease: function(releaseName) {
		$assert(typeof releaseName !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#getAndroidAppForRelease", "Release name argument is undefined.");
		$assert(releaseName === null || js.lang.Types.isString(releaseName), "com.kidscademy.atlas.studio.ReleaseService#getAndroidAppForRelease", "Release name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#getAndroidAppForRelease", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#getAndroidAppForRelease", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "getAndroidAppForRelease");
		rmi.setParameters(releaseName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Create android app.
	 *
	 * @param java.lang.String releaseName,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AndroidApp
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 createAndroidApp: function(releaseName) {
		$assert(typeof releaseName !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#createAndroidApp", "Release name argument is undefined.");
		$assert(releaseName === null || js.lang.Types.isString(releaseName), "com.kidscademy.atlas.studio.ReleaseService#createAndroidApp", "Release name argument is not a string.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#createAndroidApp", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#createAndroidApp", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "createAndroidApp");
		rmi.setParameters(releaseName);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get android app.
	 *
	 * @param int appId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AndroidApp
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getAndroidApp: function(appId) {
		$assert(typeof appId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#getAndroidApp", "App id argument is undefined.");
		$assert(js.lang.Types.isNumber(appId), "com.kidscademy.atlas.studio.ReleaseService#getAndroidApp", "App id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#getAndroidApp", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#getAndroidApp", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "getAndroidApp");
		rmi.setParameters(appId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Update android app.
	 *
	 * @param com.kidscademy.atlas.studio.model.AndroidApp app,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return com.kidscademy.atlas.studio.model.AndroidApp
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 updateAndroidApp: function(app) {
		$assert(typeof app !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#updateAndroidApp", "App argument is undefined.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#updateAndroidApp", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#updateAndroidApp", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "updateAndroidApp");
		rmi.setParameters(app);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Remove android app.
	 *
	 * @param int appId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 removeAndroidApp: function(appId) {
		$assert(typeof appId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#removeAndroidApp", "App id argument is undefined.");
		$assert(js.lang.Types.isNumber(appId), "com.kidscademy.atlas.studio.ReleaseService#removeAndroidApp", "App id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#removeAndroidApp", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#removeAndroidApp", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "removeAndroidApp");
		rmi.setParameters(appId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Clean android project.
	 *
	 * @param int appId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 cleanAndroidProject: function(appId) {
		$assert(typeof appId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#cleanAndroidProject", "App id argument is undefined.");
		$assert(js.lang.Types.isNumber(appId), "com.kidscademy.atlas.studio.ReleaseService#cleanAndroidProject", "App id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#cleanAndroidProject", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#cleanAndroidProject", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "cleanAndroidProject");
		rmi.setParameters(appId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Build android app.
	 *
	 * @param int appId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return void
	 * @throws java.io.IOException
	 * @assert callback is a {@link Function} and scope is an {@link Object}, if they are defined.
	 * @note since method return type is void, callback, and hence scope too, is optional.
	 */
	 buildAndroidApp: function(appId) {
		$assert(typeof appId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#buildAndroidApp", "App id argument is undefined.");
		$assert(js.lang.Types.isNumber(appId), "com.kidscademy.atlas.studio.ReleaseService#buildAndroidApp", "App id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(typeof __callback__ === "undefined" || js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#buildAndroidApp", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#buildAndroidApp", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "buildAndroidApp");
		rmi.setParameters(appId);
		rmi.exec(__callback__, __scope__);
	},

	/**
	 * Get android store listing.
	 *
	 * @param int appId,
	 * @param Function callback function to invoke on RMI completion,
	 * @param Object scope optional callback run-time scope, default to global scope.
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 * @assert callback is a {@link Function} and scope is an {@link Object}.
	 */
	 getAndroidStoreListing: function(appId) {
		$assert(typeof appId !== "undefined", "com.kidscademy.atlas.studio.ReleaseService#getAndroidStoreListing", "App id argument is undefined.");
		$assert(js.lang.Types.isNumber(appId), "com.kidscademy.atlas.studio.ReleaseService#getAndroidStoreListing", "App id argument is not a number.");

		var __callback__ = arguments[1];
		$assert(js.lang.Types.isFunction(__callback__), "com.kidscademy.atlas.studio.ReleaseService#getAndroidStoreListing", "Callback is not a function.");
		var __scope__ = arguments[2];
		$assert(typeof __scope__ === "undefined" || js.lang.Types.isObject(__scope__), "com.kidscademy.atlas.studio.ReleaseService#getAndroidStoreListing", "Scope is not an object.");
		if(!js.lang.Types.isObject(__scope__)) {
			__scope__ = window;
		}

		var rmi = new js.net.RMI();
		rmi.setMethod("com.kidscademy.atlas.studio.ReleaseService", "getAndroidStoreListing");
		rmi.setParameters(appId);
		rmi.exec(__callback__, __scope__);
	}
};

if(typeof ReleaseService === 'undefined') {
	ReleaseService = com.kidscademy.atlas.studio.ReleaseService;
}
