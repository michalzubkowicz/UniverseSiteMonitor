define(['AbstractModel','knockout','plugins/router'], function(AbstractModel,ko,router) {
    return function (id) {
        var self=this;
        AbstractModel.apply(self,[id]);
        self.crudBaseURL="/admin/service/";
        self.serializableFields=["_id","name","address","guestaccess"];
        self.name = ko.observable("");
        self.address = ko.observable("");
        self.guestaccess = ko.observable("");
        self.expectedtext = ko.observable("");
        self.interval = ko.observable(1);
        self.lastcheck = ko.observable("");
        self.lastresponse = ko.observable("");
        self.lastresponsecode = ko.observable("");
        self.notified = ko.observable(true);
        self.ok=ko.observable(true);

        self.Edit = function() {
            router.navigate("service/"+self._id());
        };

        self.Cancel = function() {
            router.navigate("");
        };

        self.afterSave = function() {
            router.navigate("");
        };
    }
});