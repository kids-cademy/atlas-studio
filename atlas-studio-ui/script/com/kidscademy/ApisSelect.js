$package("com.kidscademy");

com.kidscademy.ApisSelect = class extends js.dom.Control {
    constructor(ownerDoc, node) {
        super(ownerDoc, node);

        this._apisListView = this.getByCssClass("apis-list");
        ApiService.getAvailableApis(apis => this._apisListView.setObject(apis));
    }

    // --------------------------------------------------------------------------------------------
    // CONTROL INTERFACE

    /**
     * 
     * @param {String} apis comma separated list of api names used by link.
     */
    setValue(apis) {
        const apisList = apis.split(',');
        // select only APIs that are present on given APIs list
        this._apisListView.findByTag("tr").forEach(tr => {
            tr.getByTag("input").check(apisList.includes(tr.getByCssClass("name").getText()));
        });
    }

    /**
     * @returns {String} comma separated list of api names.
     */
    getValue() {
        var apisList = [];
        this._apisListView.findByTag("tr").forEach(tr => {
            if (tr.getByTag("input").checked()) {
                apisList.push(tr.getByCssClass("name").getText());
            }
        });
        return apisList.join();
    }

    isValid() {
        return true;
    }

    // --------------------------------------------------------------------------------------------

    load(sourceApis, callback) {
        const sourceApisList = sourceApis.split(',');
        ApiService.getApiDescriptors(sourceApisList, apis => {
            this._apisListView.setObject(apis);
            callback();
        });
    }

    toString() {
        return "com.kidscademy.ApisSelect";
    }
};
